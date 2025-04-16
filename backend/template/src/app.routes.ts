import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Dashboard } from './app/pages/dashboard/dashboard';
import { Documentation } from './app/pages/documentation/documentation';
import { Landing } from './app/pages/landing/landing';
import { Notfound } from './app/pages/notfound/notfound';
import { OrganisationComponent } from './app/organisation/organisation/organisation.component';
import { AddOrganisationComponent } from './app/organisation/add-organisation/add-organisation.component';
import { OfficeComponent } from './app/office/office.component';
import { ServicesComponent } from './app/services/services.component';

export const appRoutes: Routes = [
    {
        path: '',
        component: AppLayout,
        children: [
            { path: '', component: Dashboard },
            { path: 'uikit', loadChildren: () => import('./app/pages/uikit/uikit.routes') },
            { path: 'documentation', component: Documentation },
            { path: 'pages', loadChildren: () => import('./app/pages/pages.routes') },

        ]
    }  ,{
        path: 'Organisation',
        component: OrganisationComponent,
        children: [
            { path: '', component: AddOrganisationComponent },
            { path: 'offices/:organizationId', component: OfficeComponent },
            { path: 'services/:officeId', component: ServicesComponent },
        ]
    },
];
