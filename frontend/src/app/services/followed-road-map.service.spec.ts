import { TestBed } from '@angular/core/testing';

import { FollowedRoadMapService } from './followed-road-map.service';

describe('FollowedRoadMapService', () => {
  let service: FollowedRoadMapService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FollowedRoadMapService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
