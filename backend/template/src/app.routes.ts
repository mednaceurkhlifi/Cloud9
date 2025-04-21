import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/component/app.layout';
import { Dashboard } from './app/pages/dashboard/dashboard';
import { Documentation } from './app/pages/documentation/documentation';
import { Landing } from './app/pages/landing/landing';
import { Notfound } from './app/pages/notfound/notfound';
import { PostComponent } from './app/forum/posts/post/post.component';
import { PostListComponent } from './app/forum/posts/post-list/post-list.component';
import { CreatePostComponent } from './app/forum/posts/create-post/create-post.component';

export const appRoutes: Routes = [
    {
        path: '',
        component: AppLayout,
        children: [
            { path: '', component: Dashboard },
            { path: 'post/:id',component: PostComponent},
            {
                path: 'posts',
                component: PostListComponent ,
                children:[
                    {
                        path:"post",
                        component:CreatePostComponent,
                        outlet:"create"
                    },
                    {
                        path:"post/:id",
                        component:CreatePostComponent,
                        outlet:"create"
                    },
                    {
                        path: 'view/:id',
                        component: PostComponent                     }

                ]

            },
            { path: 'uikit', loadChildren: () => import('./app/pages/uikit/uikit.routes') },
            { path: 'documentation', component: Documentation },
            { path: 'pages', loadChildren: () => import('./app/pages/pages.routes') }
        ]
    },
    { path: 'landing', component: Landing },
    {
        path: 'postList',
        component: PostListComponent ,
        children:[
            {
                path:"post",
                component:CreatePostComponent,
                outlet:"create"
            },
            {
                path:"post/:id",
                component:CreatePostComponent,
                outlet:"create"
            },
            {
                path: 'view/:id',
                component: PostComponent

            }

        ]

    },
    { path: 'postInfo/:id', component: PostComponent },
    { path: 'notfound', component: Notfound },
    { path: 'auth', loadChildren: () => import('./app/pages/auth/auth.routes') },
        { path: '**', redirectTo: '/notfound' }
];
