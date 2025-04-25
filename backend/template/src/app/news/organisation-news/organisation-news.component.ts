import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Button } from 'primeng/button';
import { NewsService } from '../../services/news.service';
import { News } from '../../models/News';
import { Organisation } from '../../models/Organisation';
import { Card } from 'primeng/card';

@Component({
  selector: 'app-organisation-news',
  imports: [Button,CommonModule,Card,RouterModule],
  templateUrl: './organisation-news.component.html',
  styleUrl: './organisation-news.component.scss'
})
export class OrganisationNewsComponent {
  constructor(private newsService:NewsService,private router:Router){}
  listNews:News[]=[];
  organisation:Organisation={organizationId:1,name:"test"}
  ngOnInit()
  {
      this.newsService.getOrganisationNews(this.organisation.organizationId).subscribe((res)=>{
        this.listNews=res as News[]
     /* this.listNews.forEach(element => {
        element.content=JSON.parse(element.content).content?.map((item:any)=>item.content?.map((contentItem:any)=> contentItem.text).join('')).filter((text:any)=>text).slice(1)
     } )*/
  })
  }
}
