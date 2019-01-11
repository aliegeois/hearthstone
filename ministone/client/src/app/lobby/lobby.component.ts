import { Component, OnInit } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

import { AppComponent } from '../app.component';

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss']
})
export class LobbyComponent implements OnInit {


  constructor() { }

  ngOnInit() {
    console.log(AppComponent.stompClient.isConnected());
  }

  setConnected(connected): void {
    document.getElementById('connected-content').style.setProperty('display', connected ? 'block' : 'none');
    document.getElementById('players').innerHTML = '';
  }


}
