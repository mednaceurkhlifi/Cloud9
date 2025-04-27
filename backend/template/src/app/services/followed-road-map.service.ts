import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FollowedRoadMap } from '../models/FollowedRoadMap';

@Injectable({
  providedIn: 'root'
})
export class FollowedRoadMapService {

   url : string = "http://localhost:8000/api/v1/followed-road-map";
      constructor(private http:HttpClient) {}

      getAll(): Observable<[FollowedRoadMap]>{
        return this.http.get<[FollowedRoadMap]>(this.url+"/gel-all") ;

      }
      add(followedRoadMap : FollowedRoadMap):Observable<any>{
        return this.http.post(this.url+"/add",followedRoadMap)
      }
    }
