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

      getByUserId(id:any): Observable<[FollowedRoadMap]>{
        return this.http.get<[FollowedRoadMap]>(this.url+"/get-by-user-id/"+id) ;

      }

      add(followedRoadMap : FollowedRoadMap):Observable<any>{
        return this.http.post(this.url+"/add",followedRoadMap)
      }
      update(followedRoadMap :FollowedRoadMap):Observable<any>{
        return this.http.put(this.url+"/update/"+followedRoadMap.id,followedRoadMap);
      }
    }
