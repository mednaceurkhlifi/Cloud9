import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkspaceOverviewComponent } from './workspace-overview.component';

describe('WorkspaceOverviewComponent', () => {
  let component: WorkspaceOverviewComponent;
  let fixture: ComponentFixture<WorkspaceOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkspaceOverviewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkspaceOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
