import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfanityService {
  private openRouterUrl = 'https://openrouter.ai/api/v1/chat/completions';
  private apiKey = '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'; // À sécuriser dans un environnement

  constructor(private http: HttpClient) {}

  checkBadWords(text: string): Observable<{ isProfanity: boolean }> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.apiKey}`,
      'HTTP-Referer': 'http://localhost:4200',
      'X-Title': 'Modération de contenu',
      'Content-Type': 'application/json'
    });

    const body = {
      model: "openai/gpt-3.5-turbo",
      messages: [
        {
          role: "system",
          content: "Tu es un modérateur de contenu. Réponds UNIQUEMENT par '1' si le texte contient des mots inappropriés ou par '0' sinon. Aucune autre réponse n'est acceptée."
        },
        {
          role: "user",
          content: `Le texte suivant contient-il des mots inappropriés (injures, vulgarités, etc.) ? Réponds UNIQUEMENT par '1' pour oui ou '0' pour non : "${text}"`
        }
      ],
      temperature: 0.0,
      max_tokens: 1
    };

    return this.http.post<any>(this.openRouterUrl, body, { headers }).pipe(
      map(response => {
        const answer = response.choices[0]?.message?.content?.trim();
        return { isProfanity: answer === '1' };
      }),
      catchError(error => {
        console.error('Erreur OpenRouter:', error);
        return of({ isProfanity: false }); // En cas d'erreur, on laisse passer
      })
    );
  }
}