import { Injectable } from '@angular/core';
import { News } from '../models/News';
import { HttpClient } from '@angular/common/http';
import { ReadLater } from '../models/ReadLater';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  constructor(private http:HttpClient) { }

  url:string="http://localhost:8082/api/v1/news"
  /*news!:News;

  getNewsToUpdate()
  {
    return this.news;
  }
  setNewsToUpdate(news:News)
  {
    this.news=news;
  }
*/
  addNews(news:News)
  {
    return this.http.post(this.url+"/addNews",news);
  }

  addDraft(news:News)
  {
    return this.http.post(this.url+"/addDraft",news);
  }

  addTest(formData:any)
  {
    return this.http.post(this.url+"/addNews",formData);
  }
  getArticleCategories()
  {
    return this.http.get(this.url+"/getArticleCategories");
  }
  getAllNews()
  {
    return this.http.get(this.url+"/getAll");
  }

  getImage(filename: string) {
    const url = `${this.url}/images/${filename}`;
    return this.http.get(url, { responseType: 'blob' });
  }
  getStats(orgId:Number)
  {
    return this.http.get(this.url+"/getStats/"+orgId);
  }

  generateArticleAi(description:string)
  {
    return this.http.post(this.url+"/generateNews",description);
  }

  getNewsById(id:number)
  {
    return this.http.get(this.url+"/get/"+id);
  }

  getOrganisationNews(orgId:Number)
  {
    return this.http.get(this.url+"/getOrganisationNews?orgId="+orgId)
  }

  updateNewsStatus(newsId:Number,status:String)
  {
    status.toUpperCase();
    return this.http.put(this.url+"/changeNewsStatus",{newsId,status})
  }

  updateNews(formData:any)
  {
      return this.http.put(this.url+"/update",formData);
  }


  addSaved(readLater:ReadLater)
  {
    return this.http.post(this.url+"/addReadLater",readLater)
  }

  getSavedByUser(userId:Number)
  {
    return this.http.get(this.url+"/getSaved?userId="+userId)
  }

  getChecked(userId:Number,newsId:Number)
  {
    return this.http.get(this.url+"/getNewsChecked?userId="+userId+"&newsId="+newsId)
  }

  removeChecked(userId:Number,newsId:Number)
  {
    return this.http.delete(this.url+"/removeChecked?userId="+userId+"&newsId="+newsId);
  }

  removeReadLater(readLaterId:Number)
  {
    return this.http.delete(this.url+"/removeReadLater?readLaterId="+readLaterId);
  }
}
