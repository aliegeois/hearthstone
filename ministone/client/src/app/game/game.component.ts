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

  secretMode: boolean; // False : normal heroes ; True : alternativ heroes (Pascal, Sunye, Chantal)

  constructor() {
    this.secretMode = false;
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

    this.joueur.board.set(0, cardTest1);
    this.joueur.board.set(1, cardTest2);

    this.opponent.hand.set(0, cardTest2);
    this.opponent.hand.set(1, cardTest2);
  
    this.opponent.board.set(0, cardTest2);
    this.opponent.board.set(1, cardTest1);
    this.opponent.board.set(2, cardTest2);


  }

  enter_secretMode() {
    switch(this.secretMode) {
      case true:
        this.joueur.hero.normalMode();
        this.opponent.hero.normalMode();
        this.secretMode = false;
        break;
      case false:
        this.joueur.hero.alternativMode();
        this.opponent.hero.alternativMode();
        this.secretMode = true;
    }
  }

}
