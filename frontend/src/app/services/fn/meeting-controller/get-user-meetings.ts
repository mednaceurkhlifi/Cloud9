/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { MeetingResponse } from '../../models/meeting-response';

export interface GetUserMeetings$Params {
  user_email: string;
  size: number;
  page_no: number;
}

export function getUserMeetings(http: HttpClient, rootUrl: string, params: GetUserMeetings$Params, context?: HttpContext): Observable<StrictHttpResponse<MeetingResponse>> {
  const rb = new RequestBuilder(rootUrl, getUserMeetings.PATH, 'get');
  if (params) {
    rb.path('user_email', params.user_email, {});
    rb.path('size', params.size, {});
    rb.path('page_no', params.page_no, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<MeetingResponse>;
    })
  );
}

getUserMeetings.PATH = '/meeting/get-user-meetings/{user_email}/{size}/{page_no}';
