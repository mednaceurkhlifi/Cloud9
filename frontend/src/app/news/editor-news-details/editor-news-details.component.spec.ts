import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorNewsDetailsComponent } from './editor-news-details.component';

describe('EditorNewsDetailsComponent', () => {
  let component: EditorNewsDetailsComponent;
  let fixture: ComponentFixture<EditorNewsDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditorNewsDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditorNewsDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
