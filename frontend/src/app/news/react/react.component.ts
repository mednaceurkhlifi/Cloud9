import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { common } from 'lowlight';

@Component({
  selector: 'app-react',
    standalone: true,
  imports: [CommonModule],
  templateUrl: './react.component.html',
  styleUrl: './react.component.css'
})
export class ReactComponent {
  @Output()
  event=new EventEmitter<string>()
  @Input() id:number=0;
  ck:boolean=false;
  sendSelectedReaction(react : string)

  {
this.event.emit(react)
  }

  reactClicked(){
    this.ck=!this.ck;

  }
}
