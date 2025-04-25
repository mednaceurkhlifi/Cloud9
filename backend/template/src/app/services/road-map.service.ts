import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/User';
import { RoadMap } from '../models/RoadMap';

@Injectable({
  providedIn: 'root'
})
export class RoadMapService {


    url : string = "http://localhost:8082/api/v1/road-map";
    constructor(private http:HttpClient) {}

    getAll() : Observable<RoadMap[]>{
        return this.http.get<RoadMap[]>(this.url+"/get-all");
    }

    add(roadMap :RoadMap) : Observable<any>{
      return this.http.post<Observable<any>>(this.url+"/add",roadMap);
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
