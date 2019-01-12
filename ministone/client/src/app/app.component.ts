import { Component, OnInit } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

//import { SingleTargetEffect, MultipleTargetEffect, GlobalEffect } from './effect.service';
// import { HeroMage, HeroPaladin, HeroWarrior } from './heroes.service';
//import { ConstantesService } from './constantes.service';



@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor() {
    AppComponent.title = 'client-angular';
    this.currentPage = 'home';
    //this.demarreGame = false;

    AppComponent.socket = new SockJS(AppComponent.serverUrl);
    AppComponent.stompClient = Stomp.over(AppComponent.socket);

    this.initializeWebSocketConnection();

    
  }

  static listeners = [];

  static serverUrl = 'http://localhost:8080/ministone-socket';
  static stompClient;
  static socket: SockJS;
  static sessionId = '';

  static title: String;

  public currentPage: String;
  //public demarreGame: boolean;

  initializeWebSocketConnection() {

    AppComponent.stompClient.connect({}, frame => {

      for (let l of AppComponent.listeners)
			  l.onConnect();

      console.log('Connected:' + frame);
      AppComponent.sessionId = AppComponent.socket._transport.url.split('/').slice(-2, -1)[0]; // The magic happens
      console.log('[AppComponent] sessionId = ' + AppComponent.sessionId);

      
      // Partie lancée, on switch sur le composant game
		  AppComponent.stompClient.subscribe(`/topic/lobby/${AppComponent.sessionId}/startGame`, data => {
			  console.log(`event: startGame, data: ${data.body}`);
			  data = JSON.parse(data.body);
        //window.location.replace('/game.html');
        this.currentPage = 'game';
			  // TODO lancer la partie, genre redirection vers /game
      });
      
      AppComponent.stompClient.subscribe('/topic/chat', message => {
        if (message.body) {
          console.log('Message reçu du serveur : ' + message.body);
        }
      });
      
    });
  }

  static addListener(obj) {
        AppComponent.listeners.push(obj);
        if(AppComponent.sessionId != '')
            obj.onConnect();
  }

  ngOnInit(): void {
    document.getElementById('button').addEventListener('click', () => {
      this.sendMessage('testMessage');
    });
  }

  change() {
    console.log('change_page');
    switch (this.currentPage) {
        case 'home':
            this.currentPage = 'lobby';
            break;
        case 'lobby':
            this.currentPage = 'game';
            break;
        }
    }

  sendMessage(message) {
    AppComponent.stompClient.send('/app/send/message' , {}, message);
  }

  

}