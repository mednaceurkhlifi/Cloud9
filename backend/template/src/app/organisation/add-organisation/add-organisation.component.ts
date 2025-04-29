import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {Router, RouterModule} from '@angular/router';

import {MenuItem, MessageService} from 'primeng/api';
import { FeedbackControllerService, OrganisationControllerService } from '../../../api/services/services';
import { Organization } from '../../../api/services/models/organization';

import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ToolbarModule } from 'primeng/toolbar';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { ToastModule } from 'primeng/toast';
import { Feedback } from '../../../api/services/models/feedback';
import { NotificationService } from '../../socket/NotificationService';

@Component({
  selector: 'app-add-organisation',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    DialogModule,
    ToolbarModule,
    ButtonModule,
    InputTextModule,
    TextareaModule,
    SelectModule,
    ToastModule,
    RouterModule
  ],
  templateUrl: './add-organisation.component.html',
  styleUrls: ['./add-organisation.component.scss'],
  providers: [MessageService]
})
export class AddOrganisationComponent implements OnInit {


  organizations: Organization[] = [];
  allOrganizations: Organization[] = [];
  selectedOrganizations: Organization[] = [];
  organization: Organization = this.initOrganization();
  submitted = false;
  productDialog = false;
  totalOrganizations: number = 0;
  first = 0;
  rows = 10;
  selectedImageFile?: File;
  toastService: any;

    globalFilter: string = '';

  feedbackDialog = false;
  feedbacks: Feedback[] = [];
  organizationIdForFeedbacks: number | undefined;
  notifications: string[] = [];


notificationDialogVisible: boolean = false;

  constructor(
    private service: OrganisationControllerService,
    private messageService: MessageService,
    private feedbackService: FeedbackControllerService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadOrganizations();
    this.notificationService.getNotifications().subscribe((msg) => {
      this.notifications.push(msg);
      this.notificationDialogVisible = true;
    });
  }
    filteredOrganizations: Organization[] = [];
    searchTerm: string = '';
    filterOrganizations() {
        const term = this.searchTerm.toLowerCase();
        this.filteredOrganizations = this.organizations.filter(
            (org) =>
                org.name?.toLowerCase().includes(term) ||
                org.address?.toLowerCase().includes(term) ||
                org.email?.toLowerCase().includes(term)
        );

    }
  clearNotifications() {
    this.notifications = [];
    this.notificationDialogVisible = false;
  }
    navigateToCharts(): void {
        this.router.navigate(['/organisation-stats']); // Naviguer vers la route des graphiques
    }

  initOrganization(): Organization {
    return {
      name: '',
      email: '',
      phone_number: '',
      address: '',
      is_deleted: false,
      image: ''
    };

  }

    ouvrerNotificationDialog() {
      this.notificationDialogVisible = true;
    }


  loadOrganizations(): void {
    this.service.getAllOrganisations().subscribe({
      next: (data) => {
        this.allOrganizations = data.filter(org => !org.is_deleted);
        this.totalOrganizations = this.allOrganizations.length;
        this.organizations = this.allOrganizations.slice(this.first, this.first + this.rows);
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Impossible de charger les données.' });
      }
    });
  }


  onPageChange(event: any): void {
    this.first = event.first;
    this.rows = event.rows;
    this.organizations = this.allOrganizations.slice(this.first, this.first + this.rows);
  }

  openNew(): void {
    this.organization = this.initOrganization();
    this.submitted = false;
    this.productDialog = true;
  }

  hideDialog(): void {
    this.productDialog = false;
    this.submitted = false;
  }
  isValidEmail(email: string): boolean {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
  }

  isValidPhone(phone: string): boolean {
    const phonePattern = /^[0-9]{8,15}$/;
    return phonePattern.test(phone);
  }
  saveOrganization(): void {
    this.submitted = true;

    if (this.organization.name && this.organization.email && this.organization.phone_number && this.organization.address) {
      const organizationDetails: Organization = {
        name: this.organization.name,
        email: this.organization.email,
        phone_number: this.organization.phone_number,
        address: this.organization.address,
        is_deleted: false
      };

      const formData = new FormData();
formData.append('name', this.organization.name);
formData.append('email', this.organization.email);
formData.append('phone_number', this.organization.phone_number);
formData.append('address', this.organization.address);
formData.append('is_deleted', String(this.organization.is_deleted)); // Convert boolean to string
      formData.append('image', this.organization.image || ''); // Append image as base64 string or empty string

      if (this.selectedImageFile) {
        formData.append('image', this.selectedImageFile, this.selectedImageFile.name);
      }
      if (
        !this.organization.name ||
        !this.organization.email ||
        !this.isValidEmail(this.organization.email) ||
        !this.organization.phone_number ||
        !this.isValidPhone(this.organization.phone_number) ||
        !this.organization.address
      ) {
        this.toastService.error('Please fill out all fields correctly.');
        return;
      }

      const imageFile = this.organization.image
        ? this.dataURLToBlob(this.organization.image)
        : undefined;

      const requestBody: any = { ...organizationDetails };
      if (imageFile) {
        requestBody.imageFile = imageFile;
      }

      if (this.organization.organizationId) {
        // Mise à jour
        this.service.updateOrganisation({
          id: this.organization.organizationId,
          body: requestBody
        }).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Mis à jour', detail: 'Organisation mise à jour.' });
            this.loadOrganizations();
            this.productDialog = false;
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la mise à jour.' });
          }
        });
      } else {
        // Création
        this.service.createOrganisation({
          body: requestBody
        }).subscribe({
          next: (org) => {
            this.organizations.push(org);
            this.messageService.add({ severity: 'success', summary: 'Créée', detail: 'Organisation ajoutée.' });
            this.productDialog = false;
            this.loadOrganizations();
          },
          error: () => {
            this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la création.' });
          }
        });
      }
    } else {
      this.messageService.add({
        severity: 'warn',
        summary: 'Champs requis',
        detail: 'Veuillez remplir tous les champs obligatoires.'
      });
    }
  }


  onImageSelected(event: any) {
    const file: File = event.target.files[0];
    if (file && this.isValidImage(file)) {
      this.selectedImageFile = file; // on garde le fichier avec son nom et extension

      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.organization.image = e.target.result; // base64
      };
      reader.readAsDataURL(file);
    } else {
      alert('Please select a valid image file');
    }
  }


  isValidImage(file: File): boolean {
    const allowedExtensions = ['image/jpeg', 'image/png', 'image/gif'];
    return allowedExtensions.includes(file.type); // Vérifie si le type MIME correspond à une image
  }


  dataURLToBlob(dataURL: string): Blob {
    const byteString = atob(dataURL.split(',')[1]);
    const mimeString = dataURL.split(',')[0].split(':')[1].split(';')[0];
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], { type: mimeString });
  }

  editOrganization(org: Organization): void {
    this.organization = { ...org };
    this.productDialog = true;
  }

  deleteOrganization(org: Organization): void {
    if (!org.organizationId) return;

    this.service.deleteOrganisation({ id: org.organizationId }).subscribe({
      next: () => {
        this.organizations = this.organizations.filter(o => o.organizationId !== org.organizationId);
        this.messageService.add({ severity: 'success', summary: 'Supprimée', detail: 'Organisation supprimée.' });
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la suppression.' });
      }
    });
  }
  get activeOrganizations(): Organization[] {
    return this.organizations.filter(org => !org.is_deleted);
  }

viewFeedbacks(organisationId: number): void {
  this.organizationIdForFeedbacks = organisationId; // Stocker l'ID de l'organisation
  this.feedbackService.getFeedbacksByOrganization({ organisationId }).subscribe({
    next: (data) => {
      // Filtrer les feedbacks où isRead === false
      this.feedbacks = data.filter(feedback => !feedback.read);
      this.feedbackDialog = true;
    },
    error: () => {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load feedbacks.' });
    }
  });
}
  closeFeedbackDialog(): void {
    this.feedbackDialog = false;
    this.feedbacks = [];
  }

  respondToFeedback(feedbackId: number | undefined, response: string): void {
    if (!feedbackId) {
      this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Invalid feedback ID.' });
      return;
    }

    if (!response || response.trim() === '') {
      this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Response cannot be empty.' });
      return;
    }

    this.feedbackService.respondToFeedback({ feedbackId, body: response }).subscribe({
      next: (res) => {
        console.log('API Response:', res); // Affichez la réponse brute
        this.messageService.add({
          severity: 'success',
          summary: 'Response Sent',
          detail: res['message'] || 'Response sent successfully' // Extract a string property or provide a fallback
        });
        this.closeFeedbackDialog()
      },
      error: (err) => {
        console.error('API Error:', err); // Vérifiez l'erreur ici
        if (err.status === 200 && typeof err.error === 'string') {
          // Si le statut est 200 mais que le parsing JSON a échoué, utilisez la réponse brute
          this.messageService.add({
            severity: 'success',
            summary: 'Response Sent',
            detail: err.error // Affichez la réponse brute
          });
          this.closeFeedbackDialog()
        } else {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to submit response.' });
        }
      }
    });
  }
}
