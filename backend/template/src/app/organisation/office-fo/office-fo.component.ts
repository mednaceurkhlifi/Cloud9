import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { PaginatorModule } from 'primeng/paginator';
import { TopbarWidget } from '../pages/landing/components/topbarwidget.component';
import { FooterWidget } from '../pages/landing/components/footerwidget';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Office } from '../../api/services/models';
import { OfficeControllerService } from '../../api/services/services';

@Component({
  selector: 'app-office-fo',
  imports: [CommonModule, PaginatorModule, TopbarWidget, FooterWidget, RouterModule],
  templateUrl: './office-fo.component.html',
  styleUrl: './office-fo.component.scss'
})
export class OfficeFOComponent {
  organisationId: number | null = null;
  offices: Office[] = [];
  totalRecords: number = 0;
  rows: number = 10;
  first: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private officeService: OfficeControllerService
) {}

  ngOnInit() {
      this.organisationId = Number(this.route.snapshot.paramMap.get('id'));
      if (this.organisationId) {
          this.loadOffices(this.organisationId);
      }
  }

  loadOffices(organisationId: number) {
    this.officeService.getAllOfficesByOrganisationId({ organisationId }).subscribe({
        next: (data) => {
            console.log('Offices:', data); // Vérifiez les données ici
            this.offices = data;
            this.totalRecords = data.length;
        },
        error: (error) => {
            console.error('Error loading offices:', error);
        }
    });
}

  onPageChange(event: any) {
      this.first = event.first;
      this.rows = event.rows;
  }

  navigateToServices(officeId: number | undefined) {
    if (officeId !== undefined) {
        this.router.navigate(['/offices', officeId, 'services']);
    } else {
        console.error('Office ID is undefined');
    }
}

}
