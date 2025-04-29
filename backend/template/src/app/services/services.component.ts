import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterOutlet, RouterModule, ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { TextareaModule } from 'primeng/textarea';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';
import { ServiceControllerService } from '../../api/services/services';
import { Services } from '../../api/services/models';

@Component({
    selector: 'app-services',
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
    templateUrl: './services.component.html',
    styleUrls: ['./services.component.scss'],
    providers: [MessageService]
})
export class ServicesComponent {
    officeId!: number;
    services: Services[] = [];
    service: Services = {};
    selectedServices: Services[] = [];
    productDialog = false;
    submitted = false;
    rows = 10;
    toastService: any;

    constructor(
        private route: ActivatedRoute,
        private serviceService: ServiceControllerService,
        private messageService: MessageService
    ) {}

    ngOnInit(): void {
        this.route.params.subscribe(params => {
            this.officeId = +params['officeId'];
            if (this.officeId) {
                this.loadServicesForOffice();
            }
        });
    }

    loadServicesForOffice() {
        this.serviceService.getAllServices({ officeId: this.officeId }).subscribe(
            (services) => {
                this.services = services;
            },
            (error) => {
                console.error('Erreur lors du chargement des services:', error);
            }
        );
    }

    openNew() {
        this.service = {};
        this.submitted = false;
        this.productDialog = true;
    }

    editService(service: Services) {
        this.service = { ...service };
        this.productDialog = true;
    }

    saveService() {
        this.submitted = true;
        if (!this.service.serviceName || !this.service.type) {
            this.toastService.error('Please fill all required fields.');
            return;
        }

        if (!this.service.serviceName || !this.service.type || !this.service.description) return;

        const params = {
            id: this.service.serviceId!,
            body: this.service
        };

        if (this.service.serviceId) {
            this.serviceService.updateService(params).subscribe(
                (response) => {
                    this.loadServicesForOffice();
                    this.productDialog = false;
                    this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Service updated successfully!' });
                },
                (error) => {
                    console.error('Error updating service:', error);
                    this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to update the service.' });
                }
            );
        } else {
            this.serviceService.createService({ officeId: this.officeId, body: this.service }).subscribe(
                (response) => {
                    this.loadServicesForOffice();
                    this.productDialog = false;
                    this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Service created successfully!' });
                },
                (error) => {
                    console.error('Error creating service:', error);
                    this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to create the service.' });
                }
            );
        }

        this.service = {};
    }

    deleteService(service: Services) {
        this.serviceService.deleteService({ id: service.serviceId! }).subscribe(
            () => {
                this.loadServicesForOffice();
                this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Service deleted successfully!' });
            },
            (error) => {
                console.error('Error deleting service:', error);
                this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to delete the service.' });
            }
        );
    }

    hideDialog() {
        this.productDialog = false;
        this.submitted = false;
    }
}
