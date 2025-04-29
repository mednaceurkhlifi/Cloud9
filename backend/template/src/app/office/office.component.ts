import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule, RouterOutlet } from '@angular/router';
import { Office } from '../../api/services/models';
import { OfficeControllerService } from '../../api/services/services';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';

@Component({
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
  selector: 'app-office',
  templateUrl: './office.component.html',
  styleUrls: ['./office.component.scss'],

  providers: [MessageService]
})
export class OfficeComponent implements OnInit {
  organisationId!: number;
  offices: Office[] = [];
  office: Office = {};
  selectedOffices: Office[] = [];
  productDialog = false;
  submitted = false;
  rows = 10;
  toastService: any;

  isValidPhone(phone: string): boolean {
    const phonePattern = /^[0-9]{8,15}$/;
    return phonePattern.test(phone);
  }
  constructor(
    private route: ActivatedRoute,
    private officeService: OfficeControllerService,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      const orgId = +params['organizationId'];
      if (orgId) {
        this.organisationId = orgId;
        this.loadOfficesForOrganization(this.organisationId);
      }
    });
  }


  loadOfficesForOrganization(orgId: number) {
    this.officeService.getAllOfficesByOrganisationId({ organisationId: orgId })
      .subscribe(
        data => {
          this.offices = data;
        },
        error => {
          console.error("Erreur lors de la récupération des bureaux:", error);
        }
      );
  }

  openNew() {
    this.office = {};
    this.submitted = false;
    this.productDialog = true;
  }

  editOffice(office: Office) {
    this.office = { ...office };
    this.productDialog = true;
  }

  deleteOffice(office: Office) {
    this.officeService.deleteOffice({ id: office.officeId! }).subscribe(
      () => {
        this.getOffices();
        this.messageService.add({severity: 'success', summary: 'Bureau supprimé', detail: 'Le bureau a été supprimé avec succès.'});
      },
      error => {
        this.messageService.add({severity: 'error', summary: 'Erreur', detail: 'Une erreur est survenue lors de la suppression du bureau.'});
      }
    );
  }

  saveOffice() {
    this.submitted = true;
    if (
      !this.office.name ||
      !this.office.location ||
      (this.office.phoneNumber && !this.isValidPhone(this.office.phoneNumber))
    ) {
      this.toastService.error('Please fill out all fields correctly.');
      return;
    }

    if (!this.office.name || !this.office.location) return;

    if (this.office.officeId) {
      this.officeService.updateOffice({ id: this.office.officeId, body: this.office }).subscribe(
        () => this.getOffices(),
        (error) => console.error(error)  // Ajoute un gestionnaire d'erreurs pour mieux voir ce qui ne va pas
      );
    } else {
      this.officeService.addOfficeAndAssignToOrganisation({ organisationId: this.organisationId, body: this.office })
        .subscribe(
          () => this.getOffices(),
          (error) => console.error(error)  // Ajoute un gestionnaire d'erreurs pour mieux voir ce qui ne va pas
        );
    }

    this.productDialog = false;
    this.office = {};
  }


  hideDialog() {
    this.productDialog = false;
    this.submitted = false;
  }

  getOffices() {
    // Recharge les bureaux après toute modification (ajout, mise à jour, suppression)
    this.loadOfficesForOrganization(this.organisationId);
  }
}
