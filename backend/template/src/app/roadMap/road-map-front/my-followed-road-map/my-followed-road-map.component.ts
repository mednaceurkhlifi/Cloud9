import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule  } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { CardModule } from 'primeng/card';
import { ProgressBarModule } from 'primeng/progressbar';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { AccordionModule } from 'primeng/accordion';
import { DividerModule } from 'primeng/divider';
import { ToggleSwitchModule } from 'primeng/toggleswitch';
import { ToolbarModule } from 'primeng/toolbar';
import { ActivatedRoute } from '@angular/router';
import { FollowedRoadMap } from '../../../models/FollowedRoadMap';
import { FollowedRoadMapService } from '../../../services/followed-road-map.service';

@Component({
  selector: 'app-my-followed-road-map',
  imports: [CommonModule,
            ButtonModule,
            MenuModule,
            CardModule,
            ScrollPanelModule,
            ProgressBarModule,
            AccordionModule,
            DividerModule,
            ToggleSwitchModule,
            FormsModule,
            ToolbarModule],
  templateUrl: './my-followed-road-map.component.html',
  styleUrl: './my-followed-road-map.component.scss'
})
export class MyFollowedRoadMapComponent implements OnInit {

  constructor(private router: Router,private followedRoadMapService :FollowedRoadMapService,private ac:ActivatedRoute){}

 followedRoadMapList :FollowedRoadMap[]=[];
 selected:FollowedRoadMap= new FollowedRoadMap();
 firstSelection=false;
 


id:any;
 ngOnInit(): void {
  this.ac.paramMap.subscribe(
    params=>{
     this.id=params.get('id')
     this.loadData(this.id)
    }
  );
 }
 loadData(id:string){
  this.followedRoadMapService.getByUserId(id).subscribe(
    res=>{this.followedRoadMapList=res;
          console.log(res)
    },
    err=>{console.log(err)}
  )

 }
 selectFollowedRoadMap(fl:FollowedRoadMap)
 {
  this.firstSelection=true;
  console.log(fl);
  this.selected=fl;
 }
 update(){
  this.followedRoadMapService.update(this.selected).subscribe(
    res=>{console.log("here"+res)},
    err=>{console.log(err)}
  )
 }

 unfollow(id:number|null){
  if(id==null)
    console.log("id can not be null")
  else{
  this.followedRoadMapService.unfollow(id).subscribe(
    res=>{console.log(res),this.loadData(this.id),this.firstSelection=false},
    err=>{console.log(err)}

  );
  }

 }
 goBack(){
  this.router.navigate(['/road-map-front']);
 }
}
