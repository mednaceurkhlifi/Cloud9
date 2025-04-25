import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TotalLikesComponent } from './total-likes.component';

describe('TotalLikesComponent', () => {
  let component: TotalLikesComponent;
  let fixture: ComponentFixture<TotalLikesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TotalLikesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TotalLikesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
