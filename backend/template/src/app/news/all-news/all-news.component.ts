import { Component } from '@angular/core';
import { TopbarWidget } from '../../pages/landing/components/topbarwidget.component';
import { FooterWidget } from '../../pages/landing/components/footerwidget';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TrendingNewsComponent } from "../trending-news/trending-news.component";
import { NewsComponent } from '../news/news.component';
import { ActivatedRoute, NavigationEnd, Router, RouterModule } from '@angular/router';
import { filter } from 'rxjs';

@Component({
  selector: 'app-all-news',
  imports: [TopbarWidget, FooterWidget, CommonModule, FormsModule,RouterModule],
  templateUrl: './all-news.component.html',
  styleUrls: ['./all-news.component.scss'],
  
})
export class AllNewsComponent {
  constructor(private router:Router,private route: ActivatedRoute){}
  selectedTab: 'all' | 'trending' = 'all';

}
