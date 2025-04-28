import { Component, Input, ViewChild } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { StepperModule } from 'primeng/stepper';
import { EditorComponent } from '../editor/editor.component';
import { EditorNewsDetailsComponent } from '../editor-news-details/editor-news-details.component';
import { animate, style, transition, trigger } from '@angular/animations';
import { News } from '../../models/News';
import { TokenService } from '../../token-service/token.service';
import { CommonModule } from '@angular/common';
import { MessageModule } from 'primeng/message';

@Component({
  selector: 'app-stepper',
  imports: [ButtonModule, StepperModule, EditorComponent, EditorNewsDetailsComponent,CommonModule,MessageModule],
  templateUrl: './stepper.component.html',
  styleUrl: './stepper.component.scss',
  animations: [
    trigger('slideInOut', [
      transition(':enter', [
        style({ transform: 'translateX({{enterFrom}})', opacity: 0 }),
        animate('300ms ease', style({ transform: 'translateX(0)', opacity: 1 }))
      ], { params: { enterFrom: '100%' } }),

      transition(':leave', [
        animate('300ms ease', style({ transform: 'translateX({{leaveTo}})', opacity: 0 }))
      ], { params: { leaveTo: '-100%' } })
    ])
  ]
})
export class StepperComponent {
  @ViewChild(EditorComponent) editor!:EditorComponent;
  @ViewChild(EditorNewsDetailsComponent) editorNewsDetails!:EditorNewsDetailsComponent;

  constructor(private tokenService:TokenService){}
  
  orgId=-1;
  isOrgExists=false;
  currentStep = 1;
  previousStep = 1;
   allDetails=new Map();
   @Input()
    news!:News;
    news2!:News;

    ngOnInit()
    {
      this.orgId=Number(this.tokenService.getOrganizationId())
      if(this.orgId!=-1)
        this.isOrgExists=true;
    }
  onStepChange(newStep: number) {
    this.previousStep = this.currentStep;
    this.currentStep = newStep;
  }

  persistDetails($event:any)
    {
      
      this.news=$event;
    }
    gotBackNews($event:any)
    {
      this.news=$event;
    }

    goNext(i:number,callback:Function)
    {
      this.editor.sendDetails('persisting')
      callback(i);
    }
    goBack(i:number,callback:Function)
    {
      this.editorNewsDetails.sendDetails()
      callback(i)
    }

   
}
