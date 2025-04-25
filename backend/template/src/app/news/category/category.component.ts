import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Button } from 'primeng/button';
import { FloatLabel } from 'primeng/floatlabel';

@Component({
  selector: 'app-category',
  imports: [FormsModule,CommonModule,Button],
  templateUrl: './category.component.html',
  styleUrl: './category.component.css'
})
export class CategoryComponent {
  constructor(){}
  listCategory:string[]=["Medical","It","Finance"];
  type:string="";

  @Output()
  event=new EventEmitter<string>();

  sendSelectedType(cat : string)

  {
this.event.emit(cat)
let index=this.listCategory.indexOf(cat)
    this.listCategory.splice(index,1)
  }

  onEnter()
  {
    this.listCategory.push(this.type)
    this.sendSelectedType(this.type)
    
  }
}
