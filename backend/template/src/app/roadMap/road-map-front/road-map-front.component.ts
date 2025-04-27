import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { TopbarWidget } from '../../pages/landing/components/topbarwidget.component';
import { HeroWidget } from '../../pages/landing/components/herowidget';
import { FeaturesWidget } from '../../pages/landing/components/featureswidget';
import { HighlightsWidget } from '../../pages/landing/components/highlightswidget';
import { PricingWidget } from '../../pages/landing/components/pricingwidget';
import { FooterWidget } from '../../pages/landing/components/footerwidget';
import { RippleModule } from 'primeng/ripple';
import { StyleClassModule } from 'primeng/styleclass';
import { ButtonModule  } from 'primeng/button';
import { ButtonGroup } from 'primeng/buttongroup';
import { DividerModule } from 'primeng/divider';
import { DialogModule } from 'primeng/dialog';
import { CardModule } from 'primeng/card';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { SplitterModule } from 'primeng/splitter';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { MultiSelectModule } from 'primeng/multiselect';
import { AccordionModule } from 'primeng/accordion';
import { ToggleSwitchModule } from 'primeng/toggleswitch';
import { ToggleButtonModule } from 'primeng/togglebutton'
import { ToolbarModule } from 'primeng/toolbar';
import { RoadMapService } from '../../services/road-map.service';
import { RoadMap } from '../../models/RoadMap';
import { FormsModule } from '@angular/forms';
import { User } from '../../models/User';
import { Step } from '../../models/Step';









@Component({
  selector: 'app-road-map-front',
  imports: [RouterModule,
            TopbarWidget,
            FooterWidget,
            RippleModule,
            StyleClassModule,
            ButtonModule,
            DividerModule,
            DialogModule,
            CardModule,
            IconFieldModule,
            InputIconModule,
            SplitterModule,
            InputTextModule,
            FloatLabelModule,
            MultiSelectModule,
            FormsModule,
            AccordionModule,
            ToggleSwitchModule,
            ToggleButtonModule,
            ToolbarModule],
  templateUrl: './road-map-front.component.html',
  styleUrl: './road-map-front.component.scss'
})

export class RoadMapFrontComponent implements OnInit{
 
  
  user: User = new User({ userId: 1 });

  /* crudStep:Step={id:1,isStrict:false,requiredPapers:"",serviceDescription:"",serviceName:"",serviceOutput:"",stepOrder:0};    
  */ 
  
  roadMapList :RoadMap []=[];
  detailVisible =false;
  addVisible =false;
  enhance=false;
  selectedRoadMap:RoadMap=new RoadMap();
  crudRoadMap:RoadMap= new RoadMap();
/*   crudStep:Step = new Step(); */
  stepsCount=0;

  /* api ai */

  userMessage = 'what time is it ';
  messages: {role: string, content: string}[] = [{role:"admin",content:"what time it is" }];
  isLoading = false;

  /* api ai */
  constructor(private roadMapSercice :RoadMapService , public router :Router ){

  }
  ngOnInit(): void {
    this.loadData();
    
  }
  loadData(){
   
    
    
    this.roadMapSercice.getAll().subscribe(
      res=>{this.roadMapList=res;
            console.log(res)
      } ,
      err=>{console.log(err)}
    )
  }

   getRandomImage(): string {
    return "assets/road-map-assets/"+String(Math.floor(Math.random() * 4) + 1)+".jpg";
}

hideDetail(){
  this.detailVisible=!this.detailVisible;
}
showDetail(roadMap :RoadMap){
  this.detailVisible=true;
  this.selectedRoadMap=roadMap;
  console.log(roadMap);
}
showAddDialog(){
  this.crudRoadMap= new RoadMap() ;
  this.crudRoadMap.steps=[];
  this.enhance=false;
this.addVisible=true;
this.stepsCount=0;
}


addStep(){
  this.stepsCount++;
  this.crudRoadMap.steps.push(new Step({id:null}))
  console.log(this.crudRoadMap.steps)

}
deleteStep(){
  this.stepsCount--;
  this.crudRoadMap.steps.pop()
  console.log(this.crudRoadMap.steps);

}
generateRange(n: number): number[] {
  return [...Array(n).keys()]; 
}
saveRoadMap(){
  this.crudRoadMap.creator=this.user;
  console.log(this.crudRoadMap);
  this.roadMapSercice.add(this.crudRoadMap)
                      .subscribe(res=>{console.log(res);
                                console.log("***")
                                this.loadData();
                                this.addVisible=false;
                      },
                                err=>{console.log(err)});
  this.crudRoadMap=new RoadMap();  
  this.crudRoadMap.steps=[];    
                       
}
approve(id:number|null){
  if(id==null)
  {console.log("error")}
  else{
  this.roadMapSercice.approve(id,this.user).subscribe(
    (response) => {
      console.log('Success:', response); // 200 OK
      this.loadData();
    },
    (error) => {
      if (error.status === 404) {
        console.error('Not Found:', error); // 404
        
      } else {
        console.error('Error:', error); // Other errors (500, 400, etc.)
        }
     }
    );
  }
  this.loadData();
  }
  disapprove(id:number|null){
    if(id==null)
      console.log("error")
    else{
    this.roadMapSercice.disapprove(id,this.user).subscribe(
      (response) => {
        console.log('Success:', response); // 200 OK
        this.loadData();
        
      },
      (error) => {
        if (error.status === 404) {
          console.error('Not Found:', error); // 404
         
          
        } else {
          console.error('Error:', error); // Other errors (500, 400, etc.)
          
          }
       }
      );
      
    }
    }

    

}   
