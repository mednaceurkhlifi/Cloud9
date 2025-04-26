import { Routes } from '@angular/router';
import { BookingListComponent } from './component/booking-list/booking-list.component';
import { BookingFormComponent } from './component/booking-form/booking-form.component';
import { BookingUserListComponent } from './component/booking-user-list/booking-user-list.component';

export const bookingRoutes: Routes = [
  {
    path: '',
    component: BookingListComponent,
    title: 'Bookings'
  },
  {
    path: 'new',
    component: BookingFormComponent,
    title: 'New Booking'
  },
  {
    path: ':id',
    component: BookingFormComponent,
    title: 'Edit Booking'
  }, 

]; 