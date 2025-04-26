import { CommonModule } from '@angular/common';
import { Component, Input, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-total-likes',
    standalone: true,
  imports: [CommonModule],
  templateUrl: './total-likes.component.html',
  styleUrl: './total-likes.component.scss'
})
export class TotalLikesComponent {

@Input()
reactions:string[]=[]
@Input()
totalLikes:any=0;

}
