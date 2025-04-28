
import { Routes } from '@angular/router';
import { RoadMapFrontComponent } from './road-map-front.component';
import { MyFollowedRoadMapComponent } from './my-followed-road-map/my-followed-road-map.component';



export default [
    {
        path: '',
        component: RoadMapFrontComponent } ,
        {path:'followed-road-map/:id' , component:MyFollowedRoadMapComponent }
] satisfies Routes;
