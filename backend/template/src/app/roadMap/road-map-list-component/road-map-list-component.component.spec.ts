import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoadMapListComponentComponent } from './road-map-list-component.component';

describe('RoadMapListComponentComponent', () => {
  let component: RoadMapListComponentComponent;
  let fixture: ComponentFixture<RoadMapListComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoadMapListComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoadMapListComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
