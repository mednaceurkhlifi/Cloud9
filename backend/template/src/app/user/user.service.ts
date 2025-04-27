import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { catchError, Observable,of,tap, throwError } from 'rxjs';

import {  Router } from '@angular/router';
import { User } from '../models/User';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl ='http://localhost:8082/api/v1/User';
  public isAuth = false;
  private currentUser: User | null = null;
  constructor(private http : HttpClient, private route:Router) { }
  addUser(user :User) : Observable<User>{return this.http.post<User>(`${this.apiUrl}/addUser`,user);}
  getAll(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }
  getUserById(id:number):Observable<User>{return this.http.get<User>(`${this.apiUrl}/${id}`);}

  public authenticate(email: string, password: string): Observable<string> {
    this.isAuth = false;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    this.setToken(email);

    return this.http.get(`${this.apiUrl}/sign-in?email=${email}&password=${password}`, {
      headers: headers,
      responseType: 'text'
    });
  }

  login(email: string, password: string): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(
      `${this.apiUrl}/sign-in`,
      { email, password },
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    ).pipe(
      tap(response => {
        this.handleAuthentication(response.token);
        this.fetchCurrentUser().subscribe();

      })
    );
  }
  private fetchCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/current`).pipe(
      tap(user => {
        this.currentUser = user;
        localStorage.setItem('currentUser', JSON.stringify(user));
      }), catchError(error => {
        console.error('Error fetching current user:', error);
        // If there's an error, try to use cached user if available
        const cachedUser = this.getCurrentUser();
        if (cachedUser) {
          return of(cachedUser);
        }
        return throwError(() => new Error('Failed to fetch current user'));
      })
    );
  }
  getCurrentUser(): User | null {
    if (!this.currentUser) {
      const userStr = localStorage.getItem('currentUser');
      if (userStr) {
        this.currentUser = JSON.parse(userStr);
      }
    }
    return this.currentUser;
  }
  private handleAuthentication(token: string): void {
    this.setToken(token);
    this.isAuth = true;
    localStorage.setItem('isAuthenticated', 'true');

    // You can decode the token to get user role if needed
    const decodedToken = this.decodeToken(token);
    if (decodedToken) {
      localStorage.setItem('userRole', decodedToken.roles);
    }
  }

  setToken(token: string): void {
    localStorage.setItem('token', token);
  }


// Add this to properly handle token expiration
isTokenExpired(token: string): boolean {
  try {
    const decoded = this.decodeToken(token);
    return decoded.exp < Date.now() / 1000;
  } catch (e) {
    return true;
  }
}

// Improved isLoggedIn check
isLoggedIn(): boolean {
  const token = this.getToken();
  console.log(token);
  return token !== null && !this.isTokenExpired(token);
}

private decodeToken(token: string): any {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (e) {
    console.error('Error decoding JWT token', e);
    return null;
  }
}



  getToken(): string | null {
    return localStorage.getItem('token');
  }


  getCurrentUserRole(): string | null {
    return localStorage.getItem('userRole');
  }

  logout(): void {
    // Clear all auth-related data
    localStorage.removeItem('token');
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('userRole');
    this.isAuth = false;
    this.route.navigate(['/auth/login']);
  }
  findByEmail():Observable<User> {return this.http.get<User>(`${this.apiUrl}/${this.getToken()}`);}

  sendResetEmail(email: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/reset-password-request?email=${email}`, null, {
      responseType: 'text'
    });
  }
  public searchprofile(code: string): Observable<User[]> {
    return this.http.get<any>(`${this.apiUrl}/searchprofile/${code}`);
  }
  isAuthenticated(): boolean {
    const token = this.getToken();
  const hasValidToken = token && !this.isTokenExpired(token);

  localStorage.setItem('isAuthenticated', String(hasValidToken));

    localStorage.setItem('isAuthenticated', 'true');
this.isAuth = true;
    return this.isAuth || localStorage.getItem('isAuthenticated') === 'true';
  }
  resetPassword(token: string, newPassword: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/reset-password`, null, {
      params: {
        token,
        newPassword
      },
      responseType: 'text'
    });
  }

  getCurrentUserWithFallback(): Observable<User> {
    return new Observable(observer => {
      const cachedUser = this.getCurrentUser();
      if (cachedUser) {
        observer.next(cachedUser);
        observer.complete();
      } else {
        this.fetchCurrentUser().subscribe({
          next: user => {
            observer.next(user);
            observer.complete();
          },
          error: err => {
            observer.error(err);
          }
        });
      }
    });
  }



}
