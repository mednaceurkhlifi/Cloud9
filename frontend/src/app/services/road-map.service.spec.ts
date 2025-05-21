import { TestBed } from '@angular/core/testing';

import { RoadMapService } from './road-map.service';

describe('RoadMapService', () => {
  let service: RoadMapService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoadMapService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
