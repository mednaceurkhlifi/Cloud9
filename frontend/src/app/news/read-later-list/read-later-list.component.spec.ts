import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReadLaterListComponent } from './read-later-list.component';

describe('ReadLaterListComponent', () => {
  let component: ReadLaterListComponent;
  let fixture: ComponentFixture<ReadLaterListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReadLaterListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReadLaterListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
