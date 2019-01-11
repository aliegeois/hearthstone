import { Component, OnInit } from '@angular/core';
 import * as Stomp from 'stompjs';
 import * as SockJS from 'sockjs-client';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  private serverUrl = 'http://localhost:8080/ministone-socket';
  private title = 'WebSockets chat';
  private stompClient;

  constructor() {
     this.initializeWebSocketConnection();
  }

  ngOnInit(): void {
    document.getElementById('button').addEventListener('click', () => {
      this.sendMessage('testMessage');
    });
  }

  initializeWebSocketConnection() {
    const ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);

    this.stompClient.connect({}, frame => {

      this.stompClient.subscribe('/chat', message => {
        if (message.body) {
          console.log('Message reçu du serveur : ' + message.body);
        }
      });

    });
  }

  sendMessage(message) {
    this.stompClient.send('/app/send/message' , {}, message);
  }

}
