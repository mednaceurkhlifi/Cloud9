import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MeetPlanComponent } from './meet-plan.component';

describe('MeetPlanComponent', () => {
  let component: MeetPlanComponent;
  let fixture: ComponentFixture<MeetPlanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MeetPlanComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MeetPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
