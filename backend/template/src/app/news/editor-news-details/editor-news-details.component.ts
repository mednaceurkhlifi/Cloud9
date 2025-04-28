import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import {  IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { FloatLabelModule } from 'primeng/floatlabel';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { FileUploadModule } from 'primeng/fileupload';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { ProgressBar, ProgressBarModule } from 'primeng/progressbar';
import { BadgeModule } from 'primeng/badge';
import { CommonModule } from '@angular/common';
import { MessageService } from 'primeng/api';
import { ChipModule } from 'primeng/chip';
import { NewsService } from '../../services/news.service';
import { elementAt } from 'rxjs';
import { News } from '../../models/News';
import { Empty } from '../../pages/empty/empty';


@Component({
  providers:[MessageService],
  selector: 'app-editor-news-details',
  imports: [Empty,InputTextModule,IconFieldModule,InputIconModule,FloatLabelModule,FormsModule,
    FileUploadModule,ToastModule,ButtonModule,ProgressBarModule,BadgeModule,CommonModule,ChipModule
  ],
  templateUrl: './editor-news-details.component.html',
  styleUrl: './editor-news-details.component.scss'
})
export class EditorNewsDetailsComponent {

  constructor(private newsService:NewsService){}
  value1:any
  totalSize = 0;
totalSizePercent = 0;
articleCategories:string[]=[];
selectedCategories:string[]=[];
loading = false;
selectedFile: File | null = null;

title=""
@Input()
news!:News;
 @Output()
  event=new EventEmitter();

  sendDetails()
  {
    this.setUpNews()
      this.event.emit(this.news);
  }
 
ngOnInit()
{
  
  if(this.news.newsId!=null || this.news.title!=null){
    //console.log(this.news.title)
    this.title=this.news.title!;
    this.selectedCategories=this.safeStringToArray(this.news.articleCategory || "[]") as string[];
   
  }
  
   

  this.newsService.getArticleCategories().subscribe(element=>{this.articleCategories=element as string[]
    this.articleCategories=this.articleCategories.filter(item=>!this.selectedCategories.includes(item))}
  )
}

chipSelected(i:number)
{
  this.selectedCategories.push(this.articleCategories[i])
  this.articleCategories.splice(i,1)
}
removeSelection(i:number)
{
  this.articleCategories.push(this.selectedCategories[i])
  console.log(this.articleCategories)

  this.articleCategories.splice(i,1)
}
onTemplatedUpload() {
  // Handle upload complete
}

onSelectedFiles(event: any) {
  const files = event.files;
  let file=files?.[0];
  if (file) {
    this.selectedFile=file
  }
  let total = file.size || 0;
  this.totalSize = total;
  this.totalSizePercent = 100;
}

choose(event: any, callback: Function) {
  callback();
}

uploadEvent(callback: Function) {
  callback();
}

onRemoveTemplatingFile(event: any, file: any, callback: Function, index: number) {
  callback(index);
}
removeUploadedFile(index: number,callback: Function) {
  this.totalSizePercent=0;
  callback(index);
}
formatSize(bytes: number): string {
  if (bytes === 0) return '0 B';
  let k = 1024;
  let sizes = ['B', 'KB', 'MB'];
  let i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

setUpNews()
{
  if(this.title.length==0)
    this.title="No-Title"
this.news.articleCategory=JSON.stringify(this.selectedCategories);
//this.news.articleType="BlogPost";
this.news.title=this.title.length>0 ? this.title : 'No-Title'
}

load(index: number) {
  const formData = new FormData();
  this.setUpNews();
  
  if (this.selectedFile) {
    formData.append('image', this.selectedFile);
    
  }

  formData.append('news', new Blob([JSON.stringify(this.news)], {
    type: 'application/json'
  }));

  if(this.news.newsId!=null)
    this.newsService.updateNews(formData).subscribe()
else
{
this.newsService.addTest(formData).subscribe()
}
  this.loading = true;

  setTimeout(() => (this.loading = false), 1000,
);
console.log(this.news)
}

safeStringToArray(input: string): string[] {
  try {
    const parsed = JSON.parse(input);

    if (Array.isArray(parsed) && parsed.every(item => typeof item === 'string')) {
      return parsed;
    }
  } catch (e) {
    return []
  }

  return [];
}

}

