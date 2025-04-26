import { Injectable } from '@angular/core';
import { PostDTO } from '../api';

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
      });
      return sum;
  }
}
