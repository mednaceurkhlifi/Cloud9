import { Component } from '@angular/core';
import { TopbarWidget } from '../../pages/landing/components/topbarwidget.component';
import { FooterWidget } from '../../pages/landing/components/footerwidget';
import { TrendingService } from '../../services/trending.service';
import { News } from '../../models/News';
import { CommonModule } from '@angular/common';
import { NewsService } from '../../services/news.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-trending-news',
  imports: [CommonModule,RouterModule],
  templateUrl: './trending-news.component.html',
  styleUrl: './trending-news.component.scss'
})

export class TrendingNewsComponent {
  constructor(private trendingService:TrendingService,private newsService:NewsService){}
  news:News[]=[]
  images: { [key: string]: string } = {};

  ngOnInit()
  {
    this.trendingService.getTrending().subscribe(res=>{
     
      this.news=res as News[]
      this.news.forEach(element => {
        this.newsService.getImage(element.image!).subscribe(
          (response: Blob) => {
            // Create an object URL for the image Blob and store it in the images object
            this.images[element.image!] = URL.createObjectURL(response);
          },
         
        );
        element.content=element.content=JSON.parse(element.content).content?.map((item:any)=>item.content?.map((contentItem:any)=> contentItem.text).join('')).filter((text:any)=>text).slice(0)

        
      });
    })
  }

}
