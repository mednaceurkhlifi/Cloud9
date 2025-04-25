import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganisationNewsComponent } from './organisation-news.component';

describe('OrganisationNewsComponent', () => {
  let component: OrganisationNewsComponent;
  let fixture: ComponentFixture<OrganisationNewsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrganisationNewsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrganisationNewsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
