import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficeFOComponent } from './office-fo.component';

describe('OfficeFOComponent', () => {
  let component: OfficeFOComponent;
  let fixture: ComponentFixture<OfficeFOComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OfficeFOComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OfficeFOComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
