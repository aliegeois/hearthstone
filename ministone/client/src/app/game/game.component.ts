import { Component, OnInit, Injectable } from '@angular/core';
import { Player, CardMinion, Card } from '../app.component';
import { initDomAdapter } from '@angular/platform-browser/src/browser';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {

  joueur: Player;
  opponent: Player;

	playing: Player;
	turn: number;
	id: number;
	
  /* Variables nécessaires pour l'affichage */
  joueurHand: Array<Card>;

  constructor() {
    this.joueurHand = new Array<Card>();

    this.init();
  }

  ngOnInit() {

    let formList = document.getElementsByTagName('form');
	
    for(let form of <any>formList) {
      form.addEventListener('submit', e => {
        e.preventDefault();
      });
    }
    

  }

  onConnect() {
    
  }


  init() {
    console.log('Initialisation');
    this.joueur = new Player("james", "paladin");
    let cardTest: CardMinion = new CardMinion(1,"recrue", 1, 1, 1, new Set<String>(), new Map<String, number>())
    this.joueur.hand.set(0, cardTest);
    this.joueurHand.push(cardTest);
  }

  

}
