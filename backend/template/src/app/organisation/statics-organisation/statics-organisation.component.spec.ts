import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaticsOrganisationComponent } from './statics-organisation.component';

describe('StaticsOrganisationComponent', () => {
  let component: StaticsOrganisationComponent;
  let fixture: ComponentFixture<StaticsOrganisationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StaticsOrganisationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StaticsOrganisationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
