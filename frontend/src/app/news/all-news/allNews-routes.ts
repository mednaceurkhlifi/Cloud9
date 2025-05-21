import { Routes } from '@angular/router';
import { AllNewsComponent } from './all-news.component';
import { NewsComponent } from '../news/news.component';
import { TrendingNewsComponent } from '../trending-news/trending-news.component';
import { ReadLaterListComponent } from '../read-later-list/read-later-list.component';

export default [
    {
        path: '',
        component: AllNewsComponent,
        children: [
            { path: 'all', component: NewsComponent },
            { path: 'saved', component: ReadLaterListComponent },
            { path: 'trending', component: TrendingNewsComponent },
            { path: ':category', component: NewsComponent },
            { path: '', redirectTo: 'all', pathMatch: 'full' }
        ]
    }
] satisfies Routes;
