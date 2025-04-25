import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoadMapFrontComponent } from './road-map-front.component';

describe('RoadMapFrontComponent', () => {
  let component: RoadMapFrontComponent;
  let fixture: ComponentFixture<RoadMapFrontComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoadMapFrontComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoadMapFrontComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
