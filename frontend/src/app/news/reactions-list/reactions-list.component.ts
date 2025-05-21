import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Reaction } from '../../models/Reaction';

@Component({
  selector: 'app-reactions-list',
    standalone: true,
  imports: [CommonModule],
  templateUrl: './reactions-list.component.html',
  styleUrl: './reactions-list.component.css'
})
export class ReactionsListComponent {
  @Input() reaction:Reaction[]=[];
  numberReactions=new Map();
  ngOnChanges()
  {
    this.numberReactions.set('All',this.reaction.length)
    this.reaction.forEach(element => {
      let rea=1;
      if(this.numberReactions.has(element.reactionType))
        rea=this.numberReactions.get(element.reactionType)+1


      this.numberReactions.set(element.reactionType,rea);


    });
    console.log(this.numberReactions)
  }

}
