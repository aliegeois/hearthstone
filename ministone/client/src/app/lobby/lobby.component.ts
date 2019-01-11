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


  constructor() {
	  AppComponent.addListener(this);
  }

  ngOnInit() {}

  onConnect() {
	  console.info('wesh t\'es connecté bro');
	  
  }

  setConnected(connected): void {
	document.getElementById('connected-content').style.setProperty('display', connected ? 'block' : 'none');
	document.getElementById('players').innerHTML = '';
  }


}
