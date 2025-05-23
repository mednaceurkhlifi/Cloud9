import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganisationComponent } from './organisation.component';

describe('OrganisationComponent', () => {
  let component: OrganisationComponent;
  let fixture: ComponentFixture<OrganisationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrganisationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrganisationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
