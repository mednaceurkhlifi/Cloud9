import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class GeminiChatService {
  private apiUrl = 'https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent';
  private apiKey = 'AIzaSyB9qX9fFoKvZ9UdXUpuMCmGegGVTwhgXXs';

  constructor(private http: HttpClient) {}

  sendMessage(message: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    const body = {
      contents: [
        {
          parts: [
            {
              text: message, // Le message utilisateur est placé ici
            },
          ],
        },
      ],
    };

    return this.http.post(`${this.apiUrl}?key=${this.apiKey}`, body, { headers });
  }
}