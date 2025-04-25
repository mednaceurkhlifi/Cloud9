import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NewsService } from '../../services/news.service';
import { News } from '../../models/News';
import { generateHTML } from '@tiptap/core';
import StarterKit from '@tiptap/starter-kit';
import TextStyle from '@tiptap/extension-text-style';
import Underline from '@tiptap/extension-underline';
import Color from '@tiptap/extension-color';
import Highlight from '@tiptap/extension-highlight';
import Blockquote from '@tiptap/extension-blockquote';
import CodeBlockLowlight from '@tiptap/extension-code-block-lowlight';
import { all, createLowlight } from 'lowlight';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import OrderedList from '@tiptap/extension-ordered-list'
import BulletList from '@tiptap/extension-bullet-list'
import { ActivatedRoute } from '@angular/router';
import { Empty } from "../../pages/empty/empty";
import { AppTopbar } from "../../layout/component/app.topbar";
import Image from '@tiptap/extension-image';
import { CustomImage } from '../editor/customImage';
import { TrendingService } from '../../services/trending.service';
import { Trending } from '../../models/Trending';
import ListItem from '@tiptap/extension-list-item';

@Component({
  selector: 'app-news-details',
  imports: [CommonModule, Empty],
  templateUrl: './news-details.component.html',
  styleUrl: './news-details.component.css'
})
export class NewsDetailsComponent {
  htmlcontent!:any;
  news!:News;
  constructor(private newsService:NewsService, private sanitizer: DomSanitizer,private ac:ActivatedRoute,
    private trendingService:TrendingService
  ){}
  ngOnInit()
  {
 
    const lowlight = createLowlight(all)

  
    this.ac.paramMap.subscribe((res)=>{this.newsService.getNewsById(Number(res.get('id'))).subscribe((res)=>{this.news=res as News
      let trending=new Trending()
      trending.type='visit';
      trending.user={userId:1}
      trending.news={newsId:this.news.newsId}
      this.trendingService.addAction(trending).subscribe(res=>console.log(res))

      console.log(this.news);
      const parsedContent=JSON.parse(this.news.content)
      this.htmlcontent=generateHTML(parsedContent,[StarterKit,ListItem,TextStyle,Underline,Color,Highlight.configure({multicolor:  true}), CodeBlockLowlight.configure({lowlight}),
        CustomImage
      ])
      this.htmlcontent=this.sanitizer.bypassSecurityTrustHtml(this.htmlcontent)

    })
  })
  }
}
