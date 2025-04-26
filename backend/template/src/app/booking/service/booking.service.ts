import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Booking {
    id: number;
    reference: string;
    status: string;
    userId: number;
    userName: string;
    serviceId: number;
    serviceName: string;
    createdAt: string;
    updatedAt: string;
}

export interface BookingRequest {
    userId: number;
    serviceId: number;
    status: string;
}

@Injectable({
    providedIn: 'root'
})
export class BookingService {
    private apiUrl = `http://localhost:8082/api/v1/api/bookings`;

    constructor(private http: HttpClient) { }

    createBooking(booking: BookingRequest): Observable<Booking> {
        return this.http.post<Booking>(this.apiUrl, booking);
    }

    getBookingById(id: number): Observable<Booking> {
        return this.http.get<Booking>(`${this.apiUrl}/${id}`);
    }

    getAllBookings(): Observable<Booking[]> {
        return this.http.get<Booking[]>(this.apiUrl);
    }

    updateBooking(id: number, booking: BookingRequest): Observable<Booking> {
        return this.http.put<Booking>(`${this.apiUrl}/${id}`, booking);
    }

    deleteBooking(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }


    getBookingsByUserId(userId: number): Observable<Booking[]> {
        return this.http.get<Booking[]>(`${this.apiUrl}/user/${userId}`);
    }

    getBookingsByServiceId(serviceId: number): Observable<Booking[]> {
        return this.http.get<Booking[]>(`${this.apiUrl}/service/${serviceId}`);
    }
}
