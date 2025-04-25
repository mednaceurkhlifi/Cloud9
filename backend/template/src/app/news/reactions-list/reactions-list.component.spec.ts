import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReactionsListComponent } from './reactions-list.component';

describe('ReactionsListComponent', () => {
  let component: ReactionsListComponent;
  let fixture: ComponentFixture<ReactionsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactionsListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReactionsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
