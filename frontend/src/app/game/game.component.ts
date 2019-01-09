import { Component, OnInit } from '@angular/core';
import { Player } from '../app.component';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {

  players: Map<String, Player>;
  playing: String; //Actual player
  constructor() { }

  ngOnInit() {
  }


  init_test(): GameComponent {
    let player1: Player = new Player('Claudius', 'paladin');
    let player2: Player = new Player('Claudius', 'warrior');

    let game: Game = new GameComponent
  }

}
