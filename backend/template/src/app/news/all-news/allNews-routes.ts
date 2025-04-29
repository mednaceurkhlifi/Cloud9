import { Routes } from "@angular/router";
import { AllNewsComponent } from "./all-news.component";
import { NewsComponent } from "../news/news.component";
import { TrendingNewsComponent } from "../trending-news/trending-news.component";

export default[
    {
        path:'',
        component:AllNewsComponent,
        children:[
        { path: '', redirectTo: 'all', pathMatch: 'full' },
        { path: 'all', component: NewsComponent },
        { path: 'trending', component: TrendingNewsComponent},
        { path: ':category', component: NewsComponent },

        ]
    }
]satisfies Routes