import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

    private isLocalStorageAvailable(): boolean {
        return typeof window !== 'undefined' && !!window.localStorage;
    }

    get email(): string | null {
        return this.isLocalStorageAvailable() ? localStorage.getItem('user_email') : null;
    }

    get userId(): string | null {
        return this.isLocalStorageAvailable() ? localStorage.getItem('user_id') : null;
    }
}
