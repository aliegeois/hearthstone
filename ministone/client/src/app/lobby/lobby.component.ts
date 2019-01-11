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

  constructor() { }

  ngOnInit() {
    console.log(AppComponent.stompClient.isConnected());
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
