import { Component, OnInit } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

import { AppComponent } from '../app.component';
import { TouchSequence } from 'selenium-webdriver';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss']
})
export class LobbyComponent implements OnInit {

  name: String; // Nom du client

  constructor() {
    AppComponent.addListener(this);
  }

  ngOnInit() {
    let formList = document.getElementsByTagName('form');

    for(let form of <any>formList) {
      form.addEventListener('submit', e => {
        e.preventDefault();
      });
    }
    
    
    document.getElementById('send-name').addEventListener('click', this.sendName);
    document.getElementById('search-game').addEventListener('click', () => {
      AppComponent.stompClient.send('/app/lobby/searchGame');
    });

  }

  onConnect() {
    console.log('wesh t\'es connecté bro');
    console.log("SessionId : " + AppComponent.sessionId);
    // Confirmation du nom
    AppComponent.stompClient.subscribe(`/topic/lobby/${AppComponent.sessionId}/confirmName`, data => {
      console.log(`event: confirmName, data: ${data.body}`);
      this.name = JSON.parse(data.body).name;
    });
  }

  setConnected(connected): void {
    document.getElementById('connected-content').style.setProperty('display', connected ? 'block' : 'none');
    document.getElementById('players').innerHTML = '';
  }

  sendName() {
    document.getElementById('ask-for-name').style.setProperty('display', 'none');
      document.getElementById('your-name').style.setProperty('display', 'block');
  
      const value = (<HTMLInputElement>document.getElementById('name')).value;
      AppComponent.stompClient.send('/app/lobby/join', {}, JSON.stringify({name: value.trim()}));
    }
}