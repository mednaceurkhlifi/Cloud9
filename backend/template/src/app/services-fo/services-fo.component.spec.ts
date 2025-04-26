import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServicesFOComponent } from './services-fo.component';

describe('ServicesFOComponent', () => {
  let component: ServicesFOComponent;
  let fixture: ComponentFixture<ServicesFOComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServicesFOComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServicesFOComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
