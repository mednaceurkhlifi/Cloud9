import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { PaginatorModule } from 'primeng/paginator';
import { TopbarWidget } from '../pages/landing/components/topbarwidget.component';
import { FooterWidget } from '../pages/landing/components/footerwidget';
import { Services } from '../../api/services/models';
import { ActivatedRoute } from '@angular/router';
import { ServiceControllerService } from '../../api/services/services';
import { BookingService } from '../booking/service/booking.service';
import { TokenService } from '../token-service/token.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-services-fo',
  imports: [CommonModule, PaginatorModule, TopbarWidget, FooterWidget, ButtonModule],
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
      private serviceController: ServiceControllerService,
      private bookingService: BookingService,
      private tokenService: TokenService,
      private snackBar: MatSnackBar,
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

  
  bookService(service: Services): void {
    this.bookingService.createBooking({
        userId: +this.tokenService.getUserId(), 
        serviceId: service.serviceId!, 
        status: 'PENDING'
    }).subscribe({
        next: (booking) => {
            this.snackBar.open('Booking created successfully!', 'Close', {
                duration: 3000,
                horizontalPosition: 'right',
                verticalPosition: 'top'
            });
        },
        error: (err) => {
            this.snackBar.open('Failed to create booking. Please try again.', 'Close', {
                duration: 3000,
                horizontalPosition: 'right',
                verticalPosition: 'top',
                panelClass: ['error-snackbar']
            });
            console.error(err);
        }
    });
}

  

  onPageChange(event: any) {
      this.first = event.first;
      this.rows = event.rows;
  }

}
