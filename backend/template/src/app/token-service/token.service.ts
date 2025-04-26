import { Injectable } from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class TokenService {

    private isLocalStorageAvailable(): boolean {
        return typeof window !== 'undefined' && !!window.localStorage;
    }

    set token(token: string) {
        if (this.isLocalStorageAvailable()) {
            localStorage.setItem('token', token);
        } else {
            console.warn('localStorage is not available.');
        }
    }

    get token(): string | null {
        return this.isLocalStorageAvailable() ? localStorage.getItem('token') : null;
    }

    removeToken() {
        if (this.isLocalStorageAvailable()) {
            localStorage.removeItem('token');
        }
    }

    isTokenNotValid(): boolean {
        return !this.isTokenValid();
    }

    getUserFullName(): string {
        const payload = this.getDecodedToken();
        return payload?.fullName || "";
    }

    getUserEmail(): string {
        const payload = this.getDecodedToken();
        return payload?.sub || "";
    }

    getUserId(): string {
        const payload = this.getDecodedToken();
        return payload?.userId || "";
    }

    getOrganizationId(): string {
        const payload = this.getDecodedToken();
        return payload?.organizationId || "";
    }

    private getDecodedToken(): any {
        const token = this.token;
        if (!token) return null;

        try {
            return JSON.parse(atob(token.split('.')[1]));
        } catch (error) {
            console.error("Invalid Token:", error);
            return null;
        }
    }

    getCurrentUserRole(): string | null {
        return localStorage.getItem('userRole');
    }

    private isTokenValid(): boolean {
        const token = this.token;

        if (!token) {
            return false;
        }

        try {
            const jwtHelper = new JwtHelperService();
            const isTokenExpired = jwtHelper.isTokenExpired(token);

            if (isTokenExpired) {
                this.removeToken();
                return false;
            }

            return true;
        } catch (error) {
            this.removeToken();
            return false;
        }
    }
}
