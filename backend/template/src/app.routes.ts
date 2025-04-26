import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Dashboard } from './app/pages/dashboard/dashboard';
import { Documentation } from './app/pages/documentation/documentation';
import { Landing } from './app/pages/landing/landing';
import { Notfound } from './app/pages/notfound/notfound';
import { FrontOfficeComponent } from './app/front-office/front-office.component';
import { BookingUserListComponent } from './app/booking/component/booking-user-list/booking-user-list.component';
import { MapComponent } from './app/map/map.component';
export const appRoutes: Routes = [
    {
        path: '',
        component: AppLayout,
        children: [
            { path: '', component: Dashboard },
            {
                path: 'bookings', loadChildren: () => import('./app/booking/booking.routes').then(m => m.bookingRoutes)
            },
            { path: 'uikit', loadChildren: () => import('./app/pages/uikit/uikit.routes') },
            { path: 'documentation', component: Documentation },
            { path: 'pages', loadChildren: () => import('./app/pages/pages.routes') }
        ]
    },
    {
        path: 'user-bookings',
        component: BookingUserListComponent,
        title: 'User Bookings'
    },
    {
        path: 'app-map',
        component: MapComponent,
        title: 'App Map'
    },
    { path: 'front-office', component: FrontOfficeComponent },
    { path: 'landing', component: Landing },
    { path: 'notfound', component: Notfound },
    { path: 'auth', loadChildren: () => import('./app/pages/auth/auth.routes') },
    { path: '**', redirectTo: '/notfound' }
];
