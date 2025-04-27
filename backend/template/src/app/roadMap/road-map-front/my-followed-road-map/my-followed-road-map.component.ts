import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';

@Component({
  selector: 'app-my-followed-road-map',
  imports: [CommonModule,
            ButtonModule,
            MenuModule],
  templateUrl: './my-followed-road-map.component.html',
  styleUrl: './my-followed-road-map.component.scss'
})
export class MyFollowedRoadMapComponent {
  menu = null;

  items = [
      { label: 'Add New', icon: 'pi pi-fw pi-plus' },
      { label: 'Remove', icon: 'pi pi-fw pi-trash' }
  ];

}
