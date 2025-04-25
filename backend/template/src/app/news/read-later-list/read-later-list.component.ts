import { Component } from '@angular/core';
import { ReadLater } from '../../models/ReadLater';
import { User } from '../../models/User';
import { NewsService } from '../../services/news.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-read-later-list',
  imports: [CommonModule],
  templateUrl: './read-later-list.component.html',
  styleUrl: './read-later-list.component.scss'
})
export class ReadLaterListComponent {
  constructor(private newsService:NewsService){}


  listReadLater:ReadLater[]=[]
  user:User={userId:1};
  toggleState:boolean[]=[];
  ngOnInit()
  { 
   this.getReadLater()
    
  }

  getReadLater()
  {
    this.newsService.getSavedByUser(this.user.userId).subscribe((res)=>{this.listReadLater=res as ReadLater[]
      this.listReadLater.forEach(element => {
        element.news.content=JSON.parse(element.news.content).content?.map((item:any)=>item.content?.map((contentItem:any)=> contentItem.text).join('')).filter((text:any)=>text).slice(1)
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
