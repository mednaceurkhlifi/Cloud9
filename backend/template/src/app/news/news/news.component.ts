import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Reaction } from '../../models/Reaction';
import { User } from '../../models/User';
import { News } from '../../models/News';
import { NewsService } from '../../services/news.service';
import { ReactionService } from '../../services/reaction.service';
import { elementAt, forkJoin } from 'rxjs';
import { ReadLater } from '../../models/ReadLater';
import { RouterModule } from '@angular/router';
import { ReactionsListComponent } from '../reactions-list/reactions-list.component';
import { ReactComponent } from '../react/react.component';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { Empty } from "../../pages/empty/empty";
import { TotalLikesComponent } from '../total-likes/total-likes.component';
import { Footer } from 'primeng/api';
import { FooterWidget } from '../../pages/landing/components/footerwidget';
import { Trending } from '../../models/Trending';
import { TrendingService } from '../../services/trending.service';
import { TopbarWidget } from '../../pages/landing/components/topbarwidget.component';

@Component({
  selector: 'app-news',
  imports: [CommonModule, FormsModule, RouterModule, ReactionsListComponent, ReactComponent, ButtonModule, CardModule,
    Empty, TotalLikesComponent],
  templateUrl: './news.component.html',
  styleUrl: './news.component.css'
})
export class NewsComponent {
  constructor(private newsService:NewsService,private reactionService:ReactionService,private trendingService:TrendingService){}
  listchecked:Boolean[]=[];
  listNews:News[]=[];
  user:User={userId:1}
  reactionClickedCheckList:Boolean[]=[]
  reactionList:Reaction[]=[]
  overlayToggle:Boolean=false;
  allReactionsPerNews=new Map();
  mostReactedOnPost=new Map()
  refreshLikesTrigger = 0;
  totalLikes:number[]=[];
  images: { [key: string]: string } = {};


  ngOnInit()
  {
        this.newsService.getAllNews().subscribe(res=>{this.listNews=res as News[]
      this.listNews.forEach(element => {
        this.newsService.getImage(element.image!).subscribe(
          (response: Blob) => {
            // Create an object URL for the image Blob and store it in the images object
            this.images[element.image!] = URL.createObjectURL(response);
          },
         
        );
        if(element.content!=null)
        element.content=JSON.parse(element.content).content?.map((item:any)=>item.content?.map((contentItem:any)=> contentItem.text).join('')).filter((text:any)=>text).slice(0)
     } )

     const check=this.listNews.map(news=>this.newsService.getChecked(this.user.userId,news.newsId!))
     forkJoin(check).subscribe(results=>{  //el fork join testanna el api calls yekmlou mba3d tlanci el subscribe bech mayjiwnich les elements mta3 tableau listNews kol marra kfifech khater el subscribe matestanech li 9balha
      this.listchecked=results.map(res=> res ==1)

     })
      
      this.test()
      });
     
     
      //console.log(this.listNews[2].content)
    
  }

  test() ////nkharej fi map fiha el newsId 9odemha el lista mta3 el reactions w n3ayet lel getMostReactedPerNews
  {
    const getReaction=this.listNews.map(news=>this.reactionService.getAllReactionsByNews(news.newsId!))

    forkJoin(getReaction).subscribe(res=>{
      //nkharej fi map fiha el newsId 9odemha el lista mta3 el reactions
      let i=-1;
     res.forEach(element => {
      i++;
      let r=element as Reaction[]
      if(r.length>0)
      this.allReactionsPerNews.set(r[0].news.newsId,r)
      else
      {
        this.allReactionsPerNews.set(this.listNews[i].newsId,r)
      } 
     });
      
     //console.log(this.mostReactedOnPost)

     this.getMostReactedPerNews()
    
     })
  }

  getMostReactedPerNews() { //tkharajli  map fiha el newsid w 9domha el reactions(l marra hedhi moch object ama mathalan [lk-1,ht-1] ma3neha news hedhi fiha like wahda w heart 1)
    const updatedMap=new Map(); //bel3ani nasna3 fi map jdida w mba3d naffectiha leli 3andi khater angular idecti elli sar changement wa9telli el reference mta3 haja ena badaltha tetbadel
    this.totalLikes=[];
    this.listNews.forEach(news => {
      const reactions = this.allReactionsPerNews.get(news.newsId) as Reaction[] || [];
      let mostReacted: string[] = [];
  
      if (reactions.length === 0) {
        this.totalLikes.push(0)
        mostReacted.push('em-1');
      } else {
        this.totalLikes.push(reactions.length)
        mostReacted.push("lk-" + reactions.filter(r => r.reactionType === 'LIKE').length.toString());
        mostReacted.push("ht-" + reactions.filter(r => r.reactionType === 'HEART').length.toString());
        mostReacted.push("wo-" + reactions.filter(r => r.reactionType === 'WOWEMOJI').length.toString());
        mostReacted.push("sd-" + reactions.filter(r => r.reactionType === 'SAD').length.toString());
  
        mostReacted.sort((a, b) => {
          const numA = parseInt(a.split('-')[1]);
          const numB = parseInt(b.split('-')[1]);
          return numB - numA;
        });
  
        mostReacted = mostReacted.filter(item => {
          const num = parseInt(item.split('-')[1]);
          return num !== 0;
        });
      }
  
      updatedMap.set(news.newsId, mostReacted);
    });
    this.mostReactedOnPost = updatedMap;
  }
  
  isOverlayToggle()
  {
    this.overlayToggle=!this.overlayToggle
  }
  reactionClickedCheck(i:number)
  {
    return this.reactionClickedCheckList[i];

  }

  reactionClicked(i:number,newsId:Number)
  {
    this.reactionService.getAllReactionsByNews(newsId!).subscribe(res=>{this.reactionList=res as Reaction[]})    
    if(this.reactionList.length>0)
        this.reactionClickedCheckList[i]=true

    this.isOverlayToggle()
    


    }
  
  
  addReaction($event:any,nId:Number)
{
   let trending=new Trending()
        trending.user={userId:1}
        trending.news={newsId:nId}
        trending.type=$event;

  let reaction:Reaction;
  this.trendingService.addAction(trending).subscribe()
  this.reactionService.getReactionByUserAndNews(this.user.userId,nId).subscribe((res:any)=>
  {
    
    if(res!==null) //ma3neha deja reacta
    {

      reaction=res as Reaction;
      if(reaction.reactionType.toLowerCase()==$event.toLowerCase())
      {
      this.reactionService.removeReaction(reaction.reactionId!).subscribe(res=>this.test()) //n3ayet lel this.test bech nrefrechi el compoenent (ki 3malte refrence jdida bel newMap fel fonction li lfou9)
      }
      else
      {
        reaction.reactionType=$event
      this.reactionService.updateReaction(reaction).subscribe(res=>this.test());
      }
    }
    else
    {    

      reaction=new Reaction()
      reaction.user=this.user;
      reaction.news={newsId:nId};
      reaction.reactionType=$event;
      this.reactionService.addReaction(reaction).subscribe((rest)=>this.test())
    
    }
    


}

  );

 

}
  onSavedClicked(newsId:Number,index:number)
  {
      if(this.listchecked[index]==false)
      {
      let readLater:ReadLater;
      readLater=new ReadLater();
      readLater.news=new News()
      readLater.news.newsId=newsId; 
      readLater.user=this.user;
      readLater.status="SAVED";
      this.newsService.addSaved(readLater).subscribe((res)=>this.listchecked[index]=true);
      
      }
      else
      {
      
      this.newsService.removeChecked(this.user.userId,newsId).subscribe(rest=>this.listchecked[index]=false)
      }

  }
  
}
