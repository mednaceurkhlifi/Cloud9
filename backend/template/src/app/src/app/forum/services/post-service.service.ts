import { Injectable } from '@angular/core';
import { Post, PostDTO } from '../../../../forum/api';

@Injectable({
  providedIn: 'root'
})
export class PostServiceService {

  constructor() { }
  getPostVotes(p :PostDTO){
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
