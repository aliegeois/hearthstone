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

	
	name: string; // Nom du client
	level: string; //Level du client (novice, regular, expert)
	//nodeUsers = new Map();
	connectedPlayers: Array<[string, string, boolean]>; // List of players. The string is the player name ; the second string is the player level (Novice, Regular, Expert) ; the boolean indicated if the player is searching for a game

  constructor() {
	  	this.level = "regular";
		AppComponent.addListener(this);
		this.connectedPlayers = new Array<[string, string, boolean]>();
  }

  ngOnInit() {

    let formList = document.getElementsByTagName('form');
	
    for(let form of <any>formList) {
      form.addEventListener('submit', e => {
        e.preventDefault();
      });
    }
    
    //document.getElementById('send-name').addEventListener('click', this.sendName);
    document.getElementById('search-game').addEventListener('click', () => {
    	AppComponent.stompClient.send('/app/lobby/searchGame');
    });
  }

  onConnect() {
    console.log('wesh t\'es connecté bro');
    console.log("[LobbyComponent.onConnect] SessionId : " + AppComponent.sessionId);
		// Confirmation du nom
		AppComponent.stompClient.subscribe(`/topic/lobby/${AppComponent.sessionId}/confirmName`, data => {
		console.log(`event: confirmName, data: ${data.body}`);
		this.name = JSON.parse(data.body).name;
		this.level = JSON.parse(data.body).level;
		});
		// Récupère les clients déjà connecté
		AppComponent.stompClient.subscribe(`/topic/lobby/${AppComponent.sessionId}/usersBefore`, data => {
			console.log(`event: usersBefore, data: ${data.body}`);
			let users = JSON.parse(data.body);
			
			for(let user of users) {
				console.log("Niveau utilisateur " + user.name + " = " + user.level);
				this.addPlayer(user.name, user.level);
			}
				
		});
		// Nouvel utilisateur se connecte
		AppComponent.stompClient.subscribe(`/topic/lobby/${AppComponent.sessionId}/userJoined`, data => {
			console.log(`event: userJoined, data: ${data.body}`);
			let user = JSON.parse(data.body);
			this.addPlayer(user.name, user.level);
		});
		// Un utilisateur se déconnecte
		AppComponent.stompClient.subscribe(`/topic/lobby/${AppComponent.sessionId}/userLeaved`, data => {
			console.log(`event: userLeaved, data: ${data.body}`);
			let user = JSON.parse(data.body);
			this.removePlayer(user.name);
		});
		// 
		AppComponent.stompClient.subscribe(`/topic/lobby/${AppComponent.sessionId}/matchDeclined`, data => {
			console.log(`event: matchDeclined, data: ${data.body}`);
			data = JSON.parse(data.body);
			//Version JS
			/*let td = document.getElementById('buttons-' + data.id);
			this.nodeUsers.get(data.opponent).firstElementChild.colSpan = 2;
			td.parentElement.removeChild(td);*/

			//Version TS
			const index = this.foundPlayer(data.opponent);
			this.connectedPlayers[index][2] = false;
		});
		// Adversaire trouvé
		AppComponent.stompClient.subscribe(`/topic/lobby/${AppComponent.sessionId}/matchFound`, data => {
			console.log(`event: matchFound, data: ${data.body}`);
			data = JSON.parse(data.body);
			this.matchFound(data.id, data.opponent);
		});
		
		// Erreur quelconque
		AppComponent.stompClient.subscribe(`/topic/lobby/${AppComponent.sessionId}/error`, data => {
			console.log(`event: error, data: ${data.body}`);
			let message = JSON.parse(data.body).message;
			console.error(message);
		});
  }




  addPlayer(name: string, level: string): void {
		//Version JS
		/*let tr = document.createElement('tr'),
				td = document.createElement('td');
		td.innerHTML = name;
		td.colSpan = 2;
		tr.appendChild(td);
		this.nodeUsers.set(name, tr);
		document.getElementById('players').appendChild(tr);*/
		
		//Version TS
		const index = this.foundPlayer(name);
		if(index !== -1) {
			this.connectedPlayers[index] = [name, level, false];
		} else {
			this.connectedPlayers.push([name, level, false]);
		}
  }

  removePlayer(name: string): void {
		//Version JS
		/*let tr = this.nodeUsers.get(name);
				tr.parentElement.removeChild(tr);
		this.nodeUsers.delete(name);*/
		
		
		//Version TS
		const index = this.foundPlayer(name);
		if(index !==-1) { //Si le joueur était bien dans l'array
			this.connectedPlayers.splice(index, 1);
		}
  }

  matchFound(gameId, opponent: string): void {
		//Version JS
		/*let opponentRow = this.nodeUsers.get(opponent);
		let tdButtons = document.createElement('td'),
	  	  buttonAccept = document.createElement('button'),
				buttonDecline = document.createElement('button');
		tdButtons.style.textAlign = 'right';
		tdButtons.setAttribute('id', 'buttons-' + gameId);

		this.nodeUsers.get(opponent).firstElementChild.colSpan = 1;
	
		buttonAccept.innerHTML = '<span style="color:green">Accept</span>';
		buttonDecline.innerHTML = '<span style="color:red">Decline</span>';
		buttonAccept.className = 'btn btn-default';
		buttonDecline.className = 'btn btn-default';

		buttonAccept.addEventListener('click', () => {
			AppComponent.stompClient.send('/app/lobby/acceptMatch');
			buttonAccept.setAttribute('disabled', 'true');
		});

		buttonDecline.addEventListener('click', () => {
			AppComponent.stompClient.send('/app/lobby/declineMatch');
			buttonDecline.setAttribute('disabled', 'true');
		});

		tdButtons.appendChild(buttonAccept);
		tdButtons.appendChild(buttonDecline);
		opponentRow.appendChild(tdButtons);*/

		//Version TS
		const index = this.foundPlayer(opponent);
		if(index !== -1) {
			this.connectedPlayers[index][2] = true;
		}

  }

  setConnected(connected): void {
    document.getElementById('connected-content').style.setProperty('display', connected ? 'block' : 'none');
    document.getElementById('players').innerHTML = '';
  }

  sendName() {
    //document.getElementById('ask-for-name').style.setProperty('display', 'none');
    //document.getElementById('your-name').style.setProperty('display', 'block');
	console.log("sessionId: "+ AppComponent.sessionId);
	const value = (<HTMLInputElement>document.getElementById('name')).value;
	console.log("Envoi du nom " + value + " de niveau " + this.level);
	AppComponent.stompClient.send('/app/lobby/join', {}, JSON.stringify({name: value.trim(), level: this.level}));
	//AppComponent.stompClient.send('/app/send/message' , {}, message);
	}
	
	foundPlayer(name: string): number {
		let index = -1;
		let i = 0;
		while(i < this.connectedPlayers.length && this.connectedPlayers[i][0] != name) {
			i++
		}
		if(i != this.connectedPlayers.length) { // If the player has not been found
			index = i;
		}
		return index;
	}

	acceptMatch(): void {
		AppComponent.stompClient.send('/app/lobby/acceptMatch');
	}

	declineMatch(): void {
		AppComponent.stompClient.send('/app/lobby/declineMatch');
	}

	setLevelNovice(): void {
		console.log("Set novice");
		this.level = "novice";
		console.log(this.level);
	}

	setLevelRegular(): void {
		console.log("Set regular");
		this.level = "regular";
		console.log(this.level);
	}

	setLevelExpert(): void {
		console.log("Set expert");
		this.level = "expert";
		console.log(this.level);
	}
	
}