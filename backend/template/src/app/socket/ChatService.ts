import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ChatService {
    private stompClient: any;

    connect() {
        const socket = new SockJS('http://localhost:8080/api/v1/ws');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, () => {
            this.stompClient.subscribe('/topic/messages', (message: any) => {
                console.log(JSON.parse(message.body)); // handle received msg
            });
        });
    }

    sendMessage(sender: string, content: string) {
        this.stompClient.send(
            '/app/chat.send',
            {},
            JSON.stringify({ sender, content })
        );
    }
}
