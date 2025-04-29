import { TestBed } from '@angular/core/testing';
import { ProfanityService } from './profanity.service';


describe('ProfanityService', () => {
  let service: ProfanityService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProfanityService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
