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
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ReactionsListComponent } from '../reactions-list/reactions-list.component';
import { ReactComponent } from '../react/react.component';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { Empty } from "../../pages/empty/empty";
import { TotalLikesComponent } from '../total-likes/total-likes.component';
import { Footer } from 'primeng/api';
import { DropdownModule } from 'primeng/dropdown';
import { Trending } from '../../models/Trending';
import { TrendingService } from '../../services/trending.service';
import { FloatLabelModule } from 'primeng/floatlabel';
import { TokenService } from '../../token-service/token.service';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { InputTextModule } from 'primeng/inputtext';
import { ExploreTopicComponent } from '../explore-topic/explore-topic.component';

@Component({
  selector: 'app-news',
    standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, ReactionsListComponent, ReactComponent, ButtonModule, CardModule,InputIconModule, IconFieldModule, InputTextModule, FloatLabelModule,
    Empty, TotalLikesComponent,DropdownModule],
  templateUrl: './news.component.html',
  styleUrl: './news.component.css'
})
export class NewsComponent {
  constructor(private newsService:NewsService,private reactionService:ReactionService,private trendingService:TrendingService,private tokenService:TokenService,private route:ActivatedRoute){}
  categories: any[] | undefined;

  selectedCategory: { name: string, code: string } | undefined;
  originalList: News[] = [];
    search!:string;
  listchecked:Boolean[]=[];
  listNews:News[]=[];
  user!:User;
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
    this.route.paramMap.subscribe(
      param=>{
        this.newsService.getAllNews().subscribe(res=>{this.listNews=res as News[]
          this.originalList=this.listNews;
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
     const category=param.get('category')
        
     if(category)
     {
       this.listNews=this.originalList.filter(n=>n.articleCategory?.toLowerCase().includes(category.toLowerCase()))

     }
     const check=this.listNews.map(news=>this.newsService.getChecked(this.user.userId!,news.newsId!))
     forkJoin(check).subscribe(results=>{  //el fork join testanna el api calls yekmlou mba3d tlanci el subscribe bech mayjiwnich les elements mta3 tableau listNews kol marra kfifech khater el subscribe matestanech li 9balha
      this.listchecked=results.map(res=> res ==1)

     })

      this.test()
      });

       
          
      }
    )
    this.newsService.getArticleCategories().subscribe(res => {
      this.categories = (res as any)?.map((cat: string) => ({
        name: cat,
        code: cat.toUpperCase().slice(0, 2)
      }));
    });
    this.user={userId:Number(this.tokenService.getUserId())}
        


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
        trending.user=this.user
        trending.news={newsId:nId}
        trending.type=$event;

  let reaction:Reaction;
  this.trendingService.addAction(trending).subscribe()
  this.reactionService.getReactionByUserAndNews(this.user.userId!,nId).subscribe((res:any)=>
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

      this.newsService.removeChecked(this.user.userId!,newsId).subscribe(rest=>this.listchecked[index]=false)
      }

  }

  onSearchChange(search:string)
  {
    if(search.length>0)
    {
      this.listNews=this.originalList.filter(n=>n.title?.toLowerCase().includes(search.toLowerCase()))
    }
    else
    this.listNews=this.originalList
    
  }

  onSelectedCategory(eve:any)
  {
  
    if(eve.value)
    {
    if(eve.value.name)
    this.listNews=this.originalList.filter(n=>n.articleCategory?.toLowerCase().includes(eve.value.name.toLowerCase()))
    }
    else
    this.listNews=this.originalList
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
