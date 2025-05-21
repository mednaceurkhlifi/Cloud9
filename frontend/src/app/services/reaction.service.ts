import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Reaction } from '../models/Reaction';

@Injectable({
  providedIn: 'root'
})
export class ReactionService {

  constructor(private http:HttpClient) { }

  url:string="http://172.189.160.215:8082/api/v1/react"
  addReaction(reaction:Reaction)
  {
    return this.http.post(this.url+"/addReaction",reaction);
  }

  getReactionByUserAndNews(userId:Number,newsId:Number)
  {
    return this.http.get(this.url+"/getReaction?userId="+userId+"&newsId="+newsId)
  }

  removeReaction(reactionId:Number)
  {
    return this.http.delete(this.url+"/removeReaction/"+reactionId)
  }

  updateReaction(reaction:Reaction)
  {
    return this.http.put(this.url+"/updateReaction",reaction)
  }

  getAllReactionsByNews(newsId:Number)
  {
    return this.http.get(this.url+"/getAllReactionsByNews/"+newsId)
  }
}
