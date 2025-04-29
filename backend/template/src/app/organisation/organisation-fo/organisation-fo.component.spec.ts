import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganisationFOComponent } from './organisation-fo.component';

describe('OrganisationFOComponent', () => {
  let component: OrganisationFOComponent;
  let fixture: ComponentFixture<OrganisationFOComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrganisationFOComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrganisationFOComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
