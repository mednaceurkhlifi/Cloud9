import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { TopbarWidget } from '../../pages/landing/components/topbarwidget.component';
import { FooterWidget } from '../../pages/landing/components/footerwidget';
import { RippleModule } from 'primeng/ripple';
import { StyleClassModule } from 'primeng/styleclass';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { PaginatorModule } from 'primeng/paginator';
import { TableModule } from 'primeng/table';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { HttpClientModule } from '@angular/common/http';
import { RatingModule } from 'primeng/rating';
import { FeedBackDto, Organization } from '../../../api/services/models';
import { FeedbackControllerService, OrganisationControllerService } from '../../../api/services/services';
import { ChatbotComponent } from '../chatbot/chatbot.component';
import { AddFeedbackToOrganization$Params } from '../../../api/services/fn/feedback-controller/add-feedback-to-organization';
import {MessageService} from "primeng/api";
import {Toast} from "primeng/toast";
import { ProfanityService } from '../Profanity/profanity.service';


@Component({
  selector: 'app-organisation-fo',
    imports: [
        ChatbotComponent,
        CommonModule,
        RouterModule,
        TopbarWidget,
        FooterWidget,
        RippleModule,
        StyleClassModule,
        ButtonModule,
        DividerModule,
        PaginatorModule,
        TableModule,
        FormsModule,
        RatingModule,
        DialogModule,
        HttpClientModule,
        Toast
    ],
  templateUrl: './organisation-fo.component.html',
  styleUrl: './organisation-fo.component.scss',
    providers: [MessageService,ProfanityService]
})
export class OrganisationFOComponent {
  organizations: Organization[] = [];
  filteredOrganizations: Organization[] = [];
  totalRecords: number = 0;
  rows: number = 10;
  first: number = 0;
  searchTerm: string = '';
  isChatbotOpen: boolean = false;
  rateDialog: boolean = false;
  submitted: boolean = false;
  feedback: { rating: number; comment: string; organizationId: number | null } = {
    rating: 0,
    comment: '',
    organizationId: null,
  };

  feedbacks: FeedBackDto[] = []; // Liste des feedbacks
   showFeedbackDialog: boolean = false;


  constructor(
    private organisationService: OrganisationControllerService,
    private feedbackService: FeedbackControllerService,
    private router: Router,
    private messageService: MessageService,
    private profanityService: ProfanityService
  ) {}

  ngOnInit() {
    this.loadOrganizations();
  }

  loadOrganizations() {
    this.organisationService.getAllOrganisations().subscribe({
      next: (data) => {
        console.log('Organisations chargées :', data); // Vérifiez les données ici
        this.organizations = data;
        this.filteredOrganizations = data; // Initialiser la liste filtrée
        this.totalRecords = data.length;
        this.loadAllAverageRates(); // Charger les notes moyennes
      },
      error: (error) => {
        console.error('Erreur lors du chargement des organisations :', error);
      },
    });
  }

  loadAverageRate(organizationId: number): void {
    this.organisationService.getAverageRate({ organizationId }).subscribe({
      next: (rate) => {
        console.log(`Note moyenne pour l'organisation ${organizationId} :`, rate);
        const org = this.organizations.find((o) => o.organizationId === organizationId);
        if (org) {
          org.averageRate = rate; // Ajouter la note moyenne à l'organisation
        }
      },
      error: (error) => {
        console.error(
          `Erreur lors du chargement de la note moyenne pour l'organisation ${organizationId} :`,
          error
        );
      },
    });
  }

  applyFilter(filterType: string): void {
    switch (filterType) {
      case 'mostRated':
        this.filteredOrganizations = [...this.organizations].sort(
          (a, b) => (b.averageRate || 0) - (a.averageRate || 0)
        );
        break;
      case 'newest':
        this.filteredOrganizations = [...this.organizations].sort(
          (a, b) => new Date(b.created_at || 0).getTime() - new Date(a.created_at || 0).getTime()
        );
        break;
      case 'latestUpdated':
        this.filteredOrganizations = [...this.organizations].sort(
          (a, b) => new Date(b.updated_at || 0).getTime() - new Date(a.updated_at || 0).getTime()
        );
        break;
      case 'all':
      default:
        this.filteredOrganizations = [...this.organizations];
        break;
    }
    this.totalRecords = this.filteredOrganizations.length;
    this.first = 0;
  }
  

  loadAllAverageRates(): void {
    this.organizations.forEach((org) => {
      if (org.organizationId) {
        this.loadAverageRate(org.organizationId);
      }
    });
  }

  filterOrganizations() {
    const term = this.searchTerm.toLowerCase();
    this.filteredOrganizations = this.organizations.filter(
      (org) =>
        org.name?.toLowerCase().includes(term) ||
        org.address?.toLowerCase().includes(term) ||
        org.email?.toLowerCase().includes(term)
    );
    this.totalRecords = this.filteredOrganizations.length;
    // Mettre à jour le total
  }

  navigateToOffices(organizationId: number | undefined) {
    if (organizationId) {
      this.router.navigate(['organisation', organizationId, 'offices']);
    }
  }

  onPageChange(event: any) {
    this.first = event.first;
    this.rows = event.rows;
  }

  openRateDialog(org: Organization): void {
    this.feedback.organizationId = org.organizationId || null;
    this.feedback.rating = 0;
    this.feedback.comment = '';
    this.rateDialog = true;
  }

  hideRateDialog(): void {
    this.rateDialog = false;
    this.submitted = false;
  }

// submit feedback  with filtering bad words

  submitFeedback(): void {
    this.submitted = true;
  
    if (!this.feedback.rating || !this.feedback.comment || !this.feedback.organizationId) {
      return;
    }
  
    console.log('Début de la vérification du commentaire...');
    
    this.profanityService.checkBadWords(this.feedback.comment).subscribe({
      next: ({ isProfanity }) => {
        console.log('Résultat de la vérification:', isProfanity);
        
        if (isProfanity) {
          this.messageService.add({
            severity: 'error',
            summary: 'Contenu inapproprié',
            detail: 'Votre commentaire contient des termes inappropriés et ne peut être soumis.',
            life: 5000
          });
          return;
        }
  
        this.submitValidFeedback();
      },
      error: (error) => {
        console.error('Erreur lors de la vérification:', error);
        this.messageService.add({
          severity: 'warn',
          summary: 'Avertissement',
          detail: 'La vérification du contenu a échoué. Veuillez vérifier votre commentaire avant soumission.',
          life: 5000
        });
        // Option: soumettre quand même ou non
        this.submitValidFeedback();
      }
    });
  }
  
  private submitValidFeedback(): void {
    const params: AddFeedbackToOrganization$Params = {
      organisationId: this.feedback.organizationId!,
      body: {
        note: this.feedback.rating,
        comment: this.feedback.comment,
      },
    };
  
    this.feedbackService.addFeedbackToOrganization(params).subscribe({
      next: () => {
        console.log('Feedback soumis avec succès');
        this.loadAllAverageRates();
        this.hideRateDialog();
        this.messageService.add({
          severity: 'success',
          summary: 'Merci !',
          detail: 'Votre feedback a été enregistré.',
          life: 3000
        });
      },
      error: (error) => this.handleFeedbackError(error)
    });
  }
  



  private handleFeedbackError(error: any): void {
    console.error('Erreur soumission feedback:', error);
    let errorMessage = 'Une erreur est survenue lors de la soumission.';
  
    if (error.status === 500 && error.error?.message?.includes("already submitted")) {
      errorMessage = 'Vous avez déjà soumis un feedback pour cette organisation.';
    }
  
    this.messageService.add({
      severity: 'error',
      summary: 'Erreur',
      detail: errorMessage,
      life: 5000
    });
  }

    toggleChatbot(): void {
  this.isChatbotOpen = !this.isChatbotOpen;
}

closeChatbot(): void {
  this.isChatbotOpen = false;
}

openFeedbackDialog(): void {
  this.feedbackService.getFeedbacks().subscribe({
    next: (data) => {
      this.feedbacks = data;
      this.showFeedbackDialog = true;
    },
    error: (error) => {
      console.error('Erreur lors du chargement des feedbacks :', error);
    },
  });
}

closeFeedbackDialog(): void {
  this.showFeedbackDialog = false;
}


}
