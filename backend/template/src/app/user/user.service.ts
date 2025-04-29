import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
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
    getUserById(id: number): Observable<User> {
        return this.http.get<User>(`${this.apiUrl}/retrieveUser/${id}`);
    }
    updateUser(id: number, user: User): Observable<User> {
        const token = this.getToken();
        const headers = new HttpHeaders({
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        });

        return this.http.put<User>(`${this.apiUrl}/updateUser/${id}`, user, { headers }).pipe(
            tap(updatedUser => {
                // Update the current user in memory and localStorage if it's the same user
                if (this.currentUser && this.currentUser.userId === updatedUser.userId) {
                    this.currentUser = updatedUser;
                    localStorage.setItem('currentUser', JSON.stringify(updatedUser));
                }
            }),
            catchError(error => {
                console.error('Error updating user:', error);
                return throwError(() => new Error('Failed to update user'));
            })
        );
    }

    updateUserWithImage(id: number, user: User, image: File): Observable<User> {
        const token = this.getToken();
        const formData = new FormData();

        // Append the image file
        formData.append('image', image);

        // Stringify and append user data
        formData.append('userData', JSON.stringify(user));

        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
            // Note: Don't set Content-Type - let browser set it with boundary
        });

        return this.http.put<User>(`${this.apiUrl}/updateUserWithImage/${id}`, formData, { headers }).pipe(
            tap(updatedUser => {
                if (this.currentUser && (this.currentUser.userId === updatedUser.userId || this.currentUser.userId === updatedUser.userId)) {
                    this.currentUser = updatedUser;
                    localStorage.setItem('currentUser', JSON.stringify(updatedUser));
                }
            }),
            catchError(error => {
                console.error('Error updating user with image:', error);
                if (error.status === 400) {
                    return throwError(() => new Error('Invalid request format. Please check your data.'));
                }
                return throwError(() => new Error('Failed to update user with image'));
            })
        );
    }
    public updatePassword(id: number, oldPwd: string, newPwd: string): Observable<string> {
        const token = this.getToken(); // if you have auth token
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'text/plain'
        });

        const params = new HttpParams()
            .set('oldPwd', oldPwd)
            .set('newPwd', newPwd);

        return this.http.put<string>(`${this.apiUrl}/updatepassword/${id}`, null, { headers,
            params,
            responseType: 'text' as 'json' });
    }

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
        const token =localStorage.getItem('token');
        const headers = new HttpHeaders({
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        });
        return this.http.get<User>(`${this.apiUrl}/current`,{ headers }).pipe(
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
        return token !== null ;
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
