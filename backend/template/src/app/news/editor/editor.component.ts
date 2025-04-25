import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { Editor } from '@tiptap/core';
import StarterKit from '@tiptap/starter-kit';
import Underline from '@tiptap/extension-underline'
import Color from '@tiptap/extension-color';
import TextStyle from '@tiptap/extension-text-style'; 
import Highlight from '@tiptap/extension-highlight';
import Blockquote from '@tiptap/extension-blockquote';
import CodeBlockLowlight from '@tiptap/extension-code-block-lowlight';
import { all, createLowlight } from 'lowlight';
import { NewsService } from '../../services/news.service';
import OrderedList from '@tiptap/extension-ordered-list'
import { ActivatedRoute, Router } from '@angular/router';
import Image from '@tiptap/extension-image';
import { CategoryComponent } from '../category/category.component';
import { CommonModule } from '@angular/common';
import { News } from '../../models/News';
import { AppSidebar } from '../../layout/component/app.sidebar';
import { AppTopbar } from '../../layout/component/app.topbar';
import { AppMenu } from "../../layout/component/app.menu";
import { Button, ButtonModule } from 'primeng/button';
import { OverlayPanel, OverlayPanelModule } from 'primeng/overlaypanel';
import { Overlay } from 'primeng/overlay';
import { FileUpload } from 'primeng/fileupload';
import { animate, style, transition, trigger } from '@angular/animations';
import { CustomImage } from './customImage';
import { InputTextModule } from 'primeng/inputtext';
import { StepperModule } from 'primeng/stepper';
import { Title } from '@angular/platform-browser';
import { Dialog } from 'primeng/dialog';
import { ImageModule } from 'primeng/image';
import { FloatLabel, FloatLabelModule } from 'primeng/floatlabel';
import { FormsModule } from '@angular/forms';
import { TextareaModule } from 'primeng/textarea';

@Component({
  selector: 'app-editor',
  imports: [ InputTextModule,CommonModule,OverlayPanelModule,
    StepperModule,ButtonModule,Dialog,ImageModule,FormsModule,TextareaModule,FloatLabel

  ],
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.css',
 
})
export class EditorComponent {
  visible!: boolean
  showNewPage!:boolean
  description!:string;
  editor?:Editor
  news!:News
  selectedTypes:string[]=[];
  imageWidth = 100;
  selectedFile: File | null = null;
  newsId!:Number;
  loading:boolean=false;
  @Input()
  givenBackNews!:News;
  @Output()
  event=new EventEmitter();

   @ViewChild('op') op!: any;
   selectedImage: HTMLElement | null = null;

   ngAfterViewInit() {
   
    this.setupImageClickHandler();
  }
  @ViewChild('editorElement', { static: true }) editorElement!: ElementRef<HTMLDivElement>;
  constructor(private newsService:NewsService,private router:Router,private ac:ActivatedRoute){}
  //newsToModify= this.newsService.getNewsToUpdate() //jey mel organisation news
  //newsToModify=this.router.getCurrentNavigation()?.extras.state
  newsToModify?:News;
  content=`<p>Hello</p>`
  ngOnInit() {

  
   
      this.showNewPage = false;
      this.description="";
      this.visible=true;
      this.loading=false;
    
   

    const lowlight = createLowlight(all)
          this.editor = new Editor({
            element: this.editorElement.nativeElement, // Attach Tiptap to the div
            extensions: [StarterKit,Underline,Color,TextStyle,Highlight.configure({multicolor:  true}),
             CustomImage
              //CodeBlockLowlight.configure({lowlight}),
        
            ],
            content: this.content,
          });
          if(this.givenBackNews==null) //idha ken manzeletech back w raj3tli newsti
{
    this.ac.paramMap.subscribe(res=>{
      if(res.get('id'))
      {
        this.visible=false;
        this.newsService.getNewsById(Number(res.get('id'))).subscribe(news=>{this.newsToModify=news as News
      try{
          this.content=JSON.parse(this.newsToModify.content) || "{}";
      }catch(e)
      {
          this.content='<p>Error Loading Content</p>'
      }
  
          this.editor?.commands.setContent(this.content);
      })
      }
      else
      {
        /*let news=new News();
        news.content=this.getContent();
        news.organisation={orgId:1,orgName:"test"};   
        news.status='Draft';
        news.title="No-Title"
        
        this.newsService.addDraft(news).subscribe((newsa)=>
        {
          this.newsToModify=newsa as News;
        }
        )*/

         
      }
    })
   
}
else{
  this.visible=false;
  this.newsToModify=this.givenBackNews;
  this.editor.commands.setContent( JSON.parse(this.givenBackNews.content) || "Hello");
}
   
  }

 /* onFileSelected(event:any)
  {
    console.log(event);
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      this.selectedFile=file
      console.log('Selected:', file.name);
    }
  }
*/
 

  setupImageClickHandler() {
    const container = document.querySelector('.tiptap');
    container?.addEventListener('click', (event: any) => {
      const target = event.target as HTMLElement;
      if (target.tagName === 'IMG') {
        this.selectedImage = target;

        this.op.toggle(event, target);
      }
    });
  }
 
  resizeImage(percentage: number) {
    if (this.selectedImage) {
      this.selectedImage.style.width = `${percentage}%`;
    this.imageWidth=percentage;
    this.editor?.chain().focus().updateAttributes('image', {
      style: `width: ${percentage}%;`,
    }).run();

    console.log(this.editor?.getJSON())

  }
}



  addImage() {
    const url = prompt('Enter image URL');
    if (url) {
      //this.editor?.chain().focus().insertContent(`'<img src="${url}" />'`).run();
      this.editor?.chain().focus().setImage({src:url}).run()
    }
  }
  
  addSelectedType($event:any)
  {
    if(!this.selectedTypes.includes($event))
    this.selectedTypes.push($event)
    console.log(this.selectedTypes);
  }
 
  sendDetails(type:string)
  {
    
    this.news=new News();
    this.news.content=this.getContent();
    this.news.organisation={organizationId:1,name:"test"};
    if(type.toLowerCase()=='persisting')
    {
    if(this.newsToModify!=null)
    {
        this.newsToModify.content=this.news.content;
        this.event.emit(this.newsToModify)

    }
      else
    this.event.emit(this.news)
    }
    else if(type.toLowerCase()=='givingBack')
    {
      this.editor?.commands.setContent('hiiiiii');
    }
    
   

  }
  save(){
    
    if(this.newsToModify!=null)
    {
      const formData = new FormData();
      
      this.newsToModify.content=this.getContent();
      formData.append('news', new Blob([JSON.stringify(this.newsToModify)], {
        type: 'application/json'
      }));
      this.newsService.updateNews(formData).subscribe((res)=>console.log(res));
    }
    else
    {
      let news=new News();
        news.content=this.getContent();
        news.organisation={organizationId:1,name:"test"};   
        news.status='Draft';
        news.title="No-Title"
        
        this.newsService.addDraft(news).subscribe((newsa)=>
        {
          this.newsToModify=newsa as News;
        }
        )
    }
   
    /*this.news=new News();
    const formData = new FormData();
   
    this.news.title=this.getTitle();
    this.news.content=this.getContent();
    this.news.articleCategory="SHORTPOST";
    this.news.articleType=JSON.stringify(this.selectedTypes);
    this.news.organisation={orgId:1,orgName:"test"};
    formData.append('news', new Blob([JSON.stringify(this.news)], {
      type: 'application/json'
    }));
    if (this.selectedFile) {
      formData.append('image', this.selectedFile);
    }
    if(this.newsToModify!=null)
    {
      this.news.newsId=this.newsToModify.newsId;
      this.newsService.updateNews(this.news).subscribe();
    }
  else
  {
    console.log(this.editor?.getHTML())
  this.newsService.addTest(formData).subscribe((data)=>console.log(data));
  }
    */
  }
  getTitle()
  {
    return (this.editor?.getJSON().content?.[0].content?.[0].text) || ''
  }
  getContent()
  {
    return JSON.stringify(this.editor?.getJSON())
  }
  ngOnDestroy(): void {
    this.editor?.destroy();
  }
  
  updateNews()
  {
    this.save()
  }
  
  toggleBulletList()
  {
    this.editor?.chain().focus().toggleBulletList().run();
  
  }
  
  toggleOrderedList()
  {
    
    this.editor?.chain().focus().toggleOrderedList().run();
  
  }
  
  toggleBold()
  {
    this.editor?.chain().focus().toggleBold().run();
  
  }
  
  toggleItalic()
  {
    this.editor?.chain().focus().toggleItalic().run();
  
  }
  
  toggleHeading(level:any)
  {
    this.editor?.chain().focus().toggleHeading({level:level}).run()
  }
  
  toggleUnderline()
  {
    this.editor?.chain().focus().toggleUnderline().run()
  
  }
  getColor()
  {
    const color=this.editor?.getAttributes('textStyle')?.['color'];
    return color;
  }
  setColor(event :any)
  {
    this.editor?.chain().focus().setColor(event.target.value).run()
  }
  
  toggleHighlight()
  {
    if(!this.editor?.isActive('highlight'))
     this.editor?.chain().focus().setHighlight({color:'#FAF594'}).run()
    else
    this.editor.chain().focus().unsetHighlight().run()
  }
  
  toggleQuote()
  {
    this.editor?.chain().focus().toggleBlockquote().run()
  }
  
  toggleCodeBlock()
  {
    this.editor?.chain().focus().toggleCodeBlock().run()
  }

  showDialog() {
    this.visible = false;
}

generateArticleAi()
{
  if(this.description.length>0)
  {
    this.loading = true;
    this.newsService.generateArticleAi(this.description).subscribe(res=>
    {
      this.editor?.commands.setContent(res)
      this.loading=false
      this.visible=false;

    }
    );
  }
}
  }
  
