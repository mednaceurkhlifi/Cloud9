import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { PaginatorModule } from 'primeng/paginator';
import { TopbarWidget } from '../pages/landing/components/topbarwidget.component';
import { FooterWidget } from '../pages/landing/components/footerwidget';
import { Services } from '../../api/services/models';
import { ActivatedRoute } from '@angular/router';
import { ServiceControllerService } from '../../api/services/services';

@Component({
  selector: 'app-services-fo',
  imports: [CommonModule, PaginatorModule, TopbarWidget, FooterWidget],
  templateUrl: './services-fo.component.html',
  styleUrl: './services-fo.component.scss'
})
export class ServicesFOComponent {
  officeId: number | null = null;
  services: Services[] = [];
  totalRecords: number = 0;
  rows: number = 10;
  first: number = 0;

  constructor(
      private route: ActivatedRoute,
      private serviceController: ServiceControllerService
  ) {}

  ngOnInit() {
      this.officeId = Number(this.route.snapshot.paramMap.get('officeId'));
      if (this.officeId) {
          this.loadServices(this.officeId);
      }
  }

  loadServices(officeId: number) {
      this.serviceController.getAllServices({ officeId }).subscribe({
          next: (data) => {
              this.services = data;
              this.totalRecords = data.length;
          },
          error: (error) => {
              console.error('Error loading services:', error);
          }
      });
  }

  onPageChange(event: any) {
      this.first = event.first;
      this.rows = event.rows;
  }

}
