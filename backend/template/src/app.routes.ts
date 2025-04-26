import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Dashboard } from './app/pages/dashboard/dashboard';
import { Documentation } from './app/pages/documentation/documentation';
import { Landing } from './app/pages/landing/landing';
import { Notfound } from './app/pages/notfound/notfound';
import { FrontOfficeComponent } from './app/front-office/front-office.component';
import { BookingUserListComponent } from './app/booking/component/booking-user-list/booking-user-list.component';
import { MapComponent } from './app/map/map.component';
import { WorkspaceComponent } from './app/workspace/workspace/workspace.component';
import { WorkspaceOverviewComponent } from './app/workspace/workspace-overview/workspace-overview.component';
import { ProjectDetailsComponent } from './app/workspace/project-details/project-details.component';
import { ModuleDetailsComponent } from './app/workspace/module-details/module-details.component';
import { TaskDetailsComponent } from './app/workspace/task-details/task-details.component';
import { ProfileComponent } from './app/workspace/profile/profile.component';
import { MeetComponent } from './app/workspace/meeting/meet/meet.component';

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
            { path: 'pages', loadChildren: () => import('./app/pages/pages.routes') },
            { path: 'stepper', loadChildren: () => import('./app/news/stepper/stepper-routes') },
            { path: 'organisationNews', loadChildren: () => import('./app/news/organisation-news/organisation-news-routes') },
            {path:'allStats',loadChildren:()=>import('./app/news/all-stats/all-stats-routes')},
            {
                path: 'roadmap', loadChildren: () => import('./app/roadMap/road-map-list-component/road-map-routes.module')
            },

        ]
    },
    {path:'news',loadChildren:()=>import('./app/news/all-news/allNews-routes')},
    {path:'newsDetails/:id',loadChildren:()=>import('./app/news/news-details/news-details-routes')},
    {
        path: 'road-map-front', loadChildren: () => import('./app/roadMap/road-map-front/road-map-front-routes.module')
    },
    {
        path: 'workspace',
        component: WorkspaceComponent,
        children: [
            { path: '', component: WorkspaceOverviewComponent },
            { path: 'project/:project_id', component: ProjectDetailsComponent },
            { path: 'module/:module_id', component: ModuleDetailsComponent },
            { path: 'task/:task_id', component: TaskDetailsComponent },
            {
                path: 'profile', component: ProfileComponent
            }
        ]
    },
    {
        path: 'meeting/:room_id/:user_id/:username',
        component: MeetComponent
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
