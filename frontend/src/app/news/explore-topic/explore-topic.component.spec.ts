import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExploreTopicComponent } from './explore-topic.component';

describe('ExploreTopicComponent', () => {
  let component: ExploreTopicComponent;
  let fixture: ComponentFixture<ExploreTopicComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExploreTopicComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExploreTopicComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
