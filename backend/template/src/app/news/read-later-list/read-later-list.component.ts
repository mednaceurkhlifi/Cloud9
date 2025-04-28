import { Component } from '@angular/core';
import { ReadLater } from '../../models/ReadLater';
import { User } from '../../models/User';
import { NewsService } from '../../services/news.service';
import { CommonModule } from '@angular/common';
import { TokenService } from '../../token-service/token.service';
import { TopbarWidget } from '../../pages/landing/components/topbarwidget.component';
import { FooterWidget } from '../../pages/landing/components/footerwidget';

@Component({
  selector: 'app-read-later-list',
  imports: [CommonModule,TopbarWidget,FooterWidget],
  templateUrl: './read-later-list.component.html',
  styleUrl: './read-later-list.component.scss'
})
export class ReadLaterListComponent {
  constructor(private newsService:NewsService,private tokenService:TokenService){}

  images: { [key: string]: string } = {};

  listReadLater:ReadLater[]=[]
  user!:User;
  toggleState:boolean[]=[];
  ngOnInit()
  { 
    this.user={userId:Number(this.tokenService.getOrganizationId())}
   this.getReadLater()
    
  }

  getReadLater()
  {
    this.newsService.getSavedByUser(this.user.userId!).subscribe((res)=>{this.listReadLater=res as ReadLater[]
      this.listReadLater.forEach(element => {
        this.newsService.getImage(element.news.image!).subscribe(
          (response: Blob) => {
            // Create an object URL for the image Blob and store it in the images object
            this.images[element.news.image!] = URL.createObjectURL(response);
          },

        );
        element.news.content=JSON.parse(element.news.content).content?.map((item:any)=>item.content?.map((contentItem:any)=> contentItem.text).join('')).filter((text:any)=>text).slice(0)
     } )
   
    console.log(this.listReadLater.length);
    
    /*for(let i=0;i<this.listReadLater.length;i++)
      this.toggleState[i]=false;*/
  }

   
  )
  }
  removeReadLater(readLaterId:Number,i:number)
  {
    setTimeout(()=>
    {
          this.newsService.removeReadLater(readLaterId).subscribe(()=>
          {
            this.getReadLater()
            this.toggleState.splice(i,1)
          }
          )
         
    },500)
 
    //this.newsService.removeReadLater(readLaterId).subscribe()
  }


}
