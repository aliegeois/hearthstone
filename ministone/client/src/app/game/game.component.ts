import { Component, OnInit } from '@angular/core';
//import { Player } from '../app.component';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {

  //players: Map<String, Player>;
  playing: String; // Actual player
  nbTurns: number;


  constructor(player1Name?: String, player1Type?: String, player2Name?: String, player2Type?: String) {
    /*const player1: Player = new Player(player1Name, player1Type); // Quand on a pas ces paramètres, ça met juste à null
    const player2: Player = new Player(player2Name, player2Type); // Quand on a pas ces paramètres, ça met juste à null

    if (player1Name != null) {
      this.players.set(player1Name, player1);
    }
    if (player2Name != null) {
      this.players.set(player2Name, player2);
    }*/

    this.nbTurns = 0;
  }

  ngOnInit() {
  }


  init_test(): GameComponent {
    return new GameComponent('Claudius', 'paladin', 'Nero', 'warrior');
  }

}
