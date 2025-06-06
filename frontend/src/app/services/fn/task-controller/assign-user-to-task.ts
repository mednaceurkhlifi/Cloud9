/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { UserDto } from '../../models/user-dto';

export interface AssignUserToTask$Params {
  task_id: number;
  user_email: string;
}

export function assignUserToTask(http: HttpClient, rootUrl: string, params: AssignUserToTask$Params, context?: HttpContext): Observable<StrictHttpResponse<UserDto>> {
  const rb = new RequestBuilder(rootUrl, assignUserToTask.PATH, 'patch');
  if (params) {
    rb.path('task_id', params.task_id, {});
    rb.path('user_email', params.user_email, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<UserDto>;
    })
  );
}

assignUserToTask.PATH = '/task/assign-user/{task_id}/{user_email}';
