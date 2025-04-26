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
    HttpClientModule
  ],
  templateUrl: './organisation-fo.component.html',
  styleUrl: './organisation-fo.component.scss'
})
export class OrganisationFOComponent {
  organizations: Organization[] = [];
  filteredOrganizations: Organization[] = []; // Liste filtrée
  totalRecords: number = 0;
  rows: number = 10;
  first: number = 0;
  searchTerm: string = ''; // Terme de recherche
  isChatbotOpen: boolean = false;
  rateDialog: boolean = false; // Contrôle de la visibilité du formulaire
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
    private router: Router
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
        const org = this.organizations.find((o) => o.id === organizationId);
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

  loadAllAverageRates(): void {
    this.organizations.forEach((org) => {
      if (org.id) {
        this.loadAverageRate(org.id);
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
    this.totalRecords = this.filteredOrganizations.length; // Mettre à jour le total
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
    this.feedback.organizationId = org.id || null;
    this.feedback.rating = 0;
    this.feedback.comment = '';
    this.rateDialog = true;
  }

  hideRateDialog(): void {
    this.rateDialog = false;
    this.submitted = false;
  }
submitFeedback(): void {
  this.submitted = true;

  if (this.feedback.rating && this.feedback.comment && this.feedback.organizationId) {
    const params: AddFeedbackToOrganization$Params = {
      organisationId: this.feedback.organizationId, 
      body: {
        note: this.feedback.rating, // Note
        comment: this.feedback.comment, // Commentaire
      },
    };

    this.feedbackService.addFeedbackToOrganization(params).subscribe({
      next: () => {
        console.log('Feedback submitted successfully');
        this.loadAllAverageRates(); 
        this.hideRateDialog(); 
      },
      error: (error) => {
        console.error('Erreur lors de l\'envoi du feedback :', error);
      },
    });
  }
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
