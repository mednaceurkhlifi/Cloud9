import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/User';
import { RoadMap } from '../models/RoadMap';
import { RoadMapCreatorScore } from '../models/RoadMapCreatorScore';

@Injectable({
  providedIn: 'root'
})
export class RoadMapService {


    url : string = "http://172.189.160.215:8082/api/v1/road-map";
    constructor(private http:HttpClient) {}

    getAll() : Observable<RoadMap[]>{
        return this.http.get<RoadMap[]>(this.url+"/get-all");
    }
    getScores() : Observable<RoadMapCreatorScore[]>{
      return this.http.get<RoadMapCreatorScore[]>(this.url+"/scores");
  }

    add(roadMap :RoadMap,enhance:boolean) : Observable<any>{
      const params = new HttpParams().set('enhance', enhance.toString());
      return this.http.post<Observable<any>>(this.url+"/add",roadMap,{params});
    }

    deleteById(id:number) : Observable<any>{
      return this.http.delete<Observable<any>>(this.url+"/delete/"+id);
  }
  approve(id:number,user:User) : Observable<any>{
    return this.http.put<Observable<any>>(this.url+"/approve/"+id,user);
  }
  disapprove(id:number,user:User) : Observable<any>{
  return this.http.put<Observable<any>>(this.url+"/disapprove/"+id,user);
}




}
