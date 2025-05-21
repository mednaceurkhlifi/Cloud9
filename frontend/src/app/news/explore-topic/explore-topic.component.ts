import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { NewsService } from '../../services/news.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-explore-topic',
  imports: [CommonModule],
  templateUrl: './explore-topic.component.html',
  styleUrl: './explore-topic.component.scss',
    standalone: true
})
export class ExploreTopicComponent {
  constructor(private newsService:NewsService,private router:Router){}
  categories:string[]=[];

  ngOnInit()
  {
    this.newsService.getArticleCategories().subscribe(cat=>{
      this.categories=cat as string[]
    })
  }

  onCategorySelected(cat:string)
  {
    this.router.navigate(['/news', cat]);
  }
}
