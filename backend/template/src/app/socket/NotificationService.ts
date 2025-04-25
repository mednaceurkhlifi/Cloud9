// notification.service.ts
import { Injectable } from '@angular/core';

import { Observable, Subject } from 'rxjs';
import { Client, IMessage, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private stompClient: Client;
  private notificationSubject = new Subject<string>();

  constructor() {
    const socket = new SockJS('http://localhost:8082/api/v1/ws');
    this.stompClient = Stomp.over(socket);

    this.stompClient.onConnect = () => {
      this.stompClient.subscribe('/topic/notifications', (message: IMessage) => {
        this.notificationSubject.next(message.body);
      });
    };
    this.stompClient.activate();
  }

  getNotifications(): Observable<string> {
    return this.notificationSubject.asObservable();
  }
}
