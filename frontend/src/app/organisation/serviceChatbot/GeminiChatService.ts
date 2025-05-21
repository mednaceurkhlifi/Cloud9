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


//* Prompt pour le modèle Gemini 2.0 Flash */

        const prompt = `
    Tu es un assistant expert uniquement en Organisation, Services et Bureaux.
    Réponds uniquement aux questions concernant ces sujets.
    Si une question est hors sujet, dis simplement : "Je ne suis spécialisé que dans l'Organisation, le Service et le Bureau."
    Voici la question de l'utilisateur : "${message}"
  `;

        const body = {
            contents: [
                {
                    parts: [
                        {
                            text: prompt,
                        },
                    ],
                },
            ],
        };
        return this.http.post(`${this.apiUrl}?key=${this.apiKey}`, body, { headers });
    }

}
