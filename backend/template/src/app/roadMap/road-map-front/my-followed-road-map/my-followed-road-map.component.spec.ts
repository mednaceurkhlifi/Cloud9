import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyFollowedRoadMapComponent } from './my-followed-road-map.component';

describe('MyFollowedRoadMapComponent', () => {
  let component: MyFollowedRoadMapComponent;
  let fixture: ComponentFixture<MyFollowedRoadMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyFollowedRoadMapComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyFollowedRoadMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
