import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Dashboard } from './app/pages/dashboard/dashboard';
import { Documentation } from './app/pages/documentation/documentation';
import { Landing } from './app/pages/landing/landing';
import { Notfound } from './app/pages/notfound/notfound';

export const appRoutes: Routes = [
    {
        path: '',
        component: AppLayout,
        children: [
            { path: '', component: Dashboard },
            { path: 'uikit', loadChildren: () => import('./app/pages/uikit/uikit.routes') },
            { path: 'documentation', component: Documentation },
            { path: 'pages', loadChildren: () => import('./app/pages/pages.routes') },
            { path: 'stepper', loadChildren: () => import('./app/news/stepper/stepper-routes') },
            { path: 'organisationNews', loadChildren: () => import('./app/news/organisation-news/organisation-news-routes') },
            {path:'allStats',loadChildren:()=>import('./app/news/all-stats/all-stats-routes')},


        ]
    },
    {path:'news',loadChildren:()=>import('./app/news/all-news/allNews-routes')},
    {path:'newsDetails/:id',loadChildren:()=>import('./app/news/news-details/news-details-routes')},

    { path: 'landing', component: Landing },
    { path: 'notfound', component: Notfound },
    { path: 'auth', loadChildren: () => import('./app/pages/auth/auth.routes') },
    { path: '**', redirectTo: '/notfound' }
];
