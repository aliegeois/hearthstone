import { Component, OnInit, Injectable } from '@angular/core';
import { Player, CardMinion, Card, CardSpell } from '../app.component';
import { initDomAdapter } from '@angular/platform-browser/src/browser';
import { SingleTargetEffect, MultipleTargetEffect, GlobalEffect } from '../effect.service';

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
  opponentHand: Array<number>; //Un simple number suffirait mais on ne peut faire des ngFor (coté html) que sur des types itérables
  //Attention : par soucis de sécurité, on ne peut pas mettre de card dans cet array
  joueurBoard: Array<Card>;
  opponentBoard: Array<Card>;

  constructor() {
    this.joueurHand = new Array<Card>();
    this.opponentHand = new Array<number>();
    this.joueurBoard = new Array<Card>();
    this.opponentBoard = new Array<Card>();

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
    this.joueur = new Player("Timothée", "mage");
    this.opponent = new Player("Titouan", "paladin");
    let cardTest1: CardMinion = new CardMinion(1,"recrue", 1, 1, 1, new Set<String>(), new Map<String, number>());
    let cardTest2: CardMinion = new CardMinion(2, "murloc", 3, 4, 2, new Set<String>(), new Map<String, number>());
    let cardTest3: CardSpell = new CardSpell(3, "Métamorphose", 4, new Set<SingleTargetEffect>(), new Set<MultipleTargetEffect>(), new Set<GlobalEffect>())

    this.joueur.hand.set(0, cardTest1);
    this.joueur.hand.set(1, cardTest2);
    this.joueur.hand.set(2, cardTest3);
    this.joueurHand.push(cardTest1);
    this.joueurHand.push(cardTest2);
    this.joueurHand.push(cardTest3);

    this.opponentHand.push(0);
    this.opponentHand.push(0);
    this.opponentHand.push(0);

    this.joueurBoard.push(cardTest1);
    this.joueurBoard.push(cardTest2);

    this.opponentBoard.push(cardTest1);
    this.opponentBoard.push(cardTest2);
    this.opponentBoard.push(cardTest1);


  }

  

}
