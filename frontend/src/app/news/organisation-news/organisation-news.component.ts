import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Button } from 'primeng/button';
import { NewsService } from '../../services/news.service';
import { News } from '../../models/News';
import { Organisation } from '../../models/Organisation';
import { Card } from 'primeng/card';
import { TokenService } from '../../token-service/token.service';

@Component({
  selector: 'app-organisation-news',
  imports: [Button,CommonModule,Card,RouterModule],
  templateUrl: './organisation-news.component.html',
  styleUrl: './organisation-news.component.scss'
})
export class OrganisationNewsComponent {
  constructor(private newsService:NewsService,private router:Router,private tokenService:TokenService){}
  listNews:News[]=[];
  organisation!:Organisation;
  images: { [key: string]: string } = {};

  ngOnInit()
  {
    this.organisation={organizationId:Number(this.tokenService.getOrganizationId()),name:""}
      this.newsService.getOrganisationNews(this.organisation.organizationId).subscribe((res)=>{
        this.listNews=res as News[]
        this.listNews.forEach(element => {
          this.newsService.getImage(element.image!).subscribe(
            (response: Blob) => {
              // Create an object URL for the image Blob and store it in the images object
              this.images[element.image!] = URL.createObjectURL(response);
            },
           
          );
          element.content=element.content=JSON.parse(element.content).content?.map((item:any)=>item.content?.map((contentItem:any)=> contentItem.text).join('')).filter((text:any)=>text).slice(0)
  
        });
     /* this.listNews.forEach(element => {
        element.content=JSON.parse(element.content).content?.map((item:any)=>item.content?.map((contentItem:any)=> contentItem.text).join('')).filter((text:any)=>text).slice(1)
     } )*/
  })
  }

  getFormattedCategories(jsonString: string): string {
    try {
      const arr = JSON.parse(jsonString);
      return Array.isArray(arr) ? arr.join(', ') : jsonString;
    } catch {
      return jsonString;
    }
  }
}
