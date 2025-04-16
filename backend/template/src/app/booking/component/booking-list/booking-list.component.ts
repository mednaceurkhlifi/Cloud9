import { Component, OnInit } from '@angular/core';
import { BookingService, Booking } from '../../service/booking.service';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-booking-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatProgressSpinnerModule,
    MatTooltipModule
  ],
  providers: [DatePipe],
  templateUrl: './booking-list.component.html',
  styleUrls: ['./booking-list.component.scss']
})
export class BookingListComponent implements OnInit {
  bookings: Booking[] = [];
  loading = true;
  error: string | null = null;
  displayedColumns: string[] = ['reference', 'userName', 'serviceName', 'status', 'createdAt', 'actions'];

  constructor(private bookingService: BookingService) {}

  ngOnInit(): void {
    this.loadBookings();
  }

  loadBookings(): void {
    this.loading = true;
    this.bookingService.getAllBookings().subscribe({
      next: (bookings) => {
        this.bookings = bookings;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load bookings';
        this.loading = false;
        console.error(err);
      }
    });
  }

  deleteBooking(id: number): void {
    if (confirm('Are you sure you want to delete this booking?')) {
      this.bookingService.deleteBooking(id).subscribe({
        next: () => {
          this.bookings = this.bookings.filter(booking => booking.id !== id);
        },
        error: (err) => {
          this.error = 'Failed to delete booking';
          console.error(err);
        }
      });
    }
  }
} 