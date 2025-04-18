import { Injectable } from '@angular/core';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

@Injectable({ providedIn: 'root' })
export class ChatService {
    connect(): any {
        let socketClient: any;
        const socket = new SockJS('http://localhost:8082/api/v1/ws');
        socketClient = Stomp.over(socket);

        socketClient.connect({},() => {
            console.log('Connected to WebSocket');
        });
        return socketClient;
    }
}
