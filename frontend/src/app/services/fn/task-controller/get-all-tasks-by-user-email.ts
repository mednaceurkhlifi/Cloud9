/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Task } from '../../models/task';

export interface GetAllTasksByUserEmail$Params {
  user_email: string;
}

export function getAllTasksByUserEmail(http: HttpClient, rootUrl: string, params: GetAllTasksByUserEmail$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<Task>>> {
  const rb = new RequestBuilder(rootUrl, getAllTasksByUserEmail.PATH, 'get');
  if (params) {
    rb.path('user_email', params.user_email, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<Task>>;
    })
  );
}

getAllTasksByUserEmail.PATH = '/task/get-user-all-tasks/{user_email}';
