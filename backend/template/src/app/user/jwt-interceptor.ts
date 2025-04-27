import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from './user.service';
@Injectable()

export class JwtInterceptor implements HttpInterceptor {
    constructor(private userService: UserService) {}

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
       if (request.url.includes('/sign-in') || request.url.includes('/addUser')) {
        return next.handle(request);
      }
  
      const token = this.userService.getToken();
      if (token) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
      }
      return next.handle(request);
    }
}
