import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Dashboard } from './app/pages/dashboard/dashboard';
import { Documentation } from './app/pages/documentation/documentation';
import { Landing } from './app/pages/landing/landing';
import { Notfound } from './app/pages/notfound/notfound';
import { WorkspaceComponent } from './app/workspace/workspace/workspace.component';
import { WorkspaceOverviewComponent } from './app/workspace/workspace-overview/workspace-overview.component';
import { ProjectDetailsComponent } from './app/workspace/project-details/project-details.component';
import { ModuleDetailsComponent } from './app/workspace/module-details/module-details.component';
import { TaskDetailsComponent } from './app/workspace/task-details/task-details.component';
import { MeetComponent } from './app/workspace/meeting/meet/meet.component';
import { ProfileComponent } from './app/workspace/profile/profile.component';

export const appRoutes: Routes = [
    {
        path: '',
        component: AppLayout,
        children: [
            { path: '', component: Dashboard },
            { path: 'uikit', loadChildren: () => import('./app/pages/uikit/uikit.routes') },
            { path: 'documentation', component: Documentation },
            { path: 'pages', loadChildren: () => import('./app/pages/pages.routes') }
        ]
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
    { path: 'landing', component: Landing },
    { path: 'notfound', component: Notfound },
    { path: 'auth', loadChildren: () => import('./app/pages/auth/auth.routes') },
    { path: '**', redirectTo: '/notfound' }
];
