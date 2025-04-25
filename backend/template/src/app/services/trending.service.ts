import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Trending } from '../models/Trending';

@Injectable({
  providedIn: 'root'
})
export class TrendingService {

  constructor(private http:HttpClient) { }

   url:string="http://localhost:8080/trending";
  addAction(trending:Trending)
  {
   return this.http.post(this.url+"/addTrending",trending)
  }

  getTrending()
  {
    return this.http.get(this.url+"/getTrending")
  }
}
