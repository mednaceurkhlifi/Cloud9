import { Component, ElementRef, HostListener, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GeminiChatService } from '../serviceChatbot/GeminiChatService';

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.scss'],
})
export class ChatbotComponent {
  @ViewChild('chatbotContainer') chatbotContainer!: ElementRef;

  messages: { sender: 'user' | 'bot'; text: string }[] = [];
  userMessage: string = '';
  isChatbotOpen = true;
  errorMessage: string | null = null;

  constructor(private geminiChatService: GeminiChatService) {}

  sendMessage(): void {
    if (this.userMessage.trim()) {
      this.messages.push({ sender: 'user', text: this.userMessage });

      this.geminiChatService.sendMessage(this.userMessage).subscribe({
        next: (response) => {
          const botReply = response.candidates?.[0]?.content?.parts?.[0]?.text || 'No response from Gemini.';
          this.messages.push({ sender: 'bot', text: botReply });
          this.errorMessage = null;
        },
        error: (error) => {
          console.error('Erreur lors de l\'envoi du message :', error);
          this.errorMessage = 'An error occurred. Please try again later.';
        },
      });

      this.userMessage = '';
    }
  }

  closeChatbot(): void {
    this.isChatbotOpen = false;
    console.log('Chatbot fermé');
  }

  @HostListener('document:click', ['$event'])
  handleDocumentClick(event: MouseEvent): void {
    if (
      this.isChatbotOpen &&
      this.chatbotContainer &&
      !this.chatbotContainer.nativeElement.contains(event.target)
    ) {
      this.closeChatbot();
    }
  }
}