import { TestBed } from '@angular/core/testing';

import { CurrUserServiceService } from './curr-user-service.service';

describe('CurrUserServiceService', () => {
  let service: CurrUserServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CurrUserServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
