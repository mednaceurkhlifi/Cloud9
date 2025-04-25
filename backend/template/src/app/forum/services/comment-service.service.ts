import { Injectable } from '@angular/core';
import { CommentDTO } from '../api';

@Injectable({
  providedIn: 'root'
})
export class CommentServiceService {

  constructor() { }

  getCommentVotes(p :CommentDTO){
      let sum = 0;
      p.votes?.forEach(e=>{
          if(e.voteType=="UPVOTE"){
              sum++;
          }
          else
              sum--;
      });p
      return sum;
  }
}
