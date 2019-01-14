import { Component, OnInit, Injectable } from '@angular/core';
import { Player, CardMinion, Card, CardSpell, AppComponent } from '../app.component';
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
  gameId: string;
  errorLog: string;

  constructor() {
    this.secretMode = false;
    this.gameId = AppComponent.gameId;
    this.errorLog = "";

    this.init();

    AppComponent.addListener(this);
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
    console.log("[GameComponent.onConnect] SessionId : " + AppComponent.sessionId);


    // On reçoit un summonMinion
		AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/summonMinion`, data => {
      console.log(`event: summonMinion, data: ${data.body}`);
      let msg = JSON.parse(data.body);

      let concernedPlayer : Player = this.getPlayer(msg.playerName);

      // On retire le minion de la main et on l'ajoute au niveau du board
      concernedPlayer.summon(msg.cardId);
    });


    // On reçoit une attack
    AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/attack`, data => {
      console.log(`event: attack, data: ${data.body}`);
      let msg = JSON.parse(data.body);

      let concernedPlayer : Player = this.getPlayer(msg.playerName);

      // On fait s'attaquer les deux minions
      concernedPlayer.attack(msg.cardId, msg.targetId);
    });

    // On reçoit un cast d'un targetedSpell
    AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/castTargetedSpell`, data => {
      console.log(`event: castTargetedSpell, data: ${data.body}`);
      let msg = JSON.parse(data.body);

      let concernedPlayer : Player = this.getPlayer(msg.playerName);

      // On fait cast le spell par le joueur concerné
      concernedPlayer.castTargetedSpell(msg.cardId, msg.targetId, msg.own);
    });

    // On reçoit un cast d'un untargetedSpell
    AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/castUntargetedSpell`, data => {
      console.log(`event: castUntargetedSpell, data: ${data.body}`);
      let msg = JSON.parse(data.body);

      let concernedPlayer : Player = this.getPlayer(msg.playerName);

      // On fait cast le spell par le joueur concerné
      concernedPlayer.castUntargetedSpell(msg.cardId);
    });

    // On recoit un specialHero targeted
    AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/targetedSpecial`, data => {
      console.log(`event: targetedSpecial, data: ${data.body}`);
      let msg = JSON.parse(data.body);

      let concernedPlayer : Player = this.getPlayer(msg.playerName);

      // On fait cast le special par le joueur concerné
      concernedPlayer.castTargetedSpecial(msg.targetId, msg.own);
    });

    // On recoit un specialHero untargeted
    AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/untargetedSpecial`, data => {
      console.log(`event: untargetedSpecial, data: ${data.body}`);
      let msg = JSON.parse(data.body);

      let concernedPlayer : Player = this.getPlayer(msg.playerName);

      // On fait cast le special par le joueur concerné
      concernedPlayer.castUntargetedSpecial();
    });

    // On reçoit un timeout
    AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/timeout`, data => {
      console.log(`event: timeout, data: ${data.body}`);
      let msg = JSON.parse(data.body);

      let concernedPlayer : Player = this.getPlayer(msg.playerName);

      if(concernedPlayer == this.joueur) {
        this.errorLog = "Votre tour est terminé";
      } else {
        this.errorLog = "Le tour adverse est terminé";
      }
      
      AppComponent.stompClient.send(`/game/${this.gameId}/endTurn`, {}, JSON.stringify({playerName: concernedPlayer.name}));
    });

    
    // On recoit un nextTurn
    AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/nextTurn`, data => {
      console.log(`event: nextTurn, data: ${data.body}`);
      let msg = JSON.parse(data.body);

      //concernedPlayer est le nom du joueur dont le tour vient de commencer
      let concernedPlayer : Player = this.getPlayer(msg.playerName);

      // On démarre le tour du joueur concerné
      concernedPlayer.beginTurn();
      AppComponent.stompClient.send(`/game/${this.gameId}/drawCard`, {}, JSON.stringify({playerName: concernedPlayer.name}));
      this.playing = concernedPlayer;
    });

    // On reçoit la victoire d'un joueur
    AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/win`, data => {
      console.log(`event: win, data: ${data.body}`);
      let msg = JSON.parse(data.body);

      let concernedPlayer : Player = this.getPlayer(msg.playerName);

      if(concernedPlayer == this.joueur) {
        this.errorLog = "Vous avez gagné !";
      } else {
        this.errorLog = "Vous avez perdu :/";
      }
      
      AppComponent.stompClient.send(`/game/${this.gameId}/endTurn`, {}, JSON.stringify({playerName: concernedPlayer.name}));
    });

  }




  init() {
    console.log('Initialisation');
    this.joueur = new Player(AppComponent.joueurName, AppComponent.joueurHero);
    this.opponent = new Player(AppComponent.opponentName, AppComponent.opponentHero);
    /*let cardTest1: CardMinion = new CardMinion(1,"recrue", 1, 1, 1, new Set<String>(), new Map<String, number>(), this.joueur);
    let cardTest2: CardMinion = new CardMinion(2, "murloc", 3, 4, 2, new Set<String>(), new Map<String, number>());
    let cardTest3: CardSpell = new CardSpell(3, "Métamorphose", 4, new Set<SingleTargetEffect>(), new Set<MultipleTargetEffect>(), new Set<GlobalEffect>())

    this.joueur.hand.set("0", cardTest1);
    this.joueur.hand.set("1", cardTest2);
    this.joueur.hand.set("2", cardTest3);

    this.joueur.board.set("0", cardTest1);
    this.joueur.board.set("1", cardTest2);

    this.opponent.hand.set("0", cardTest2);
    this.opponent.hand.set("1", cardTest2);
  
    this.opponent.board.set("0", cardTest2);
    this.opponent.board.set("1", cardTest1);
    this.opponent.board.set("2", cardTest2);
    */

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

  getPlayer(name: string): Player { //Return the adequate player
    if(this.joueur.name == name) {
      return this.joueur;
    } else if(this.opponent.name == name) {
      return this.opponent;
    } else {
      console.log("Player not found");
      throw new ReferenceError("Player not found");
    }
  }

}
