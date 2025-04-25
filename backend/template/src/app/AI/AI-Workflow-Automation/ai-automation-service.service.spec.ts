import { TestBed } from '@angular/core/testing';

import { AiAutomationServiceService } from './ai-automation-service.service';

describe('AiAutomationServiceService', () => {
  let service: AiAutomationServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AiAutomationServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
