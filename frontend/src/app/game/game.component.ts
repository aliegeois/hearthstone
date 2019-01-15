import { Component, OnInit, Injectable } from '@angular/core';
import { Player, CardMinion, Card, CardSpell, AppComponent, Entity } from '../app.component';
import { initDomAdapter } from '@angular/platform-browser/src/browser';
import { SingleTargetEffect, MultipleTargetEffect, GlobalEffect } from '../effect.service';
import { stringify } from '@angular/core/src/util';

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
  infoLog: string;

  selectedHand: Card;
  selectedAttacking: CardMinion;
  selectedHeroPower: boolean;


  constructor() {

    this.secretMode = false;
    this.gameId = AppComponent.gameId;
    this.infoLog = "zuefyzeiueoizqgc";
    
    this.selectedHand = null;
    this.selectedAttacking = null;
    this.selectedHeroPower = false;

    this.init();

    this.playing = this.getPlayer(AppComponent.playing);
    AppComponent.addListener(this);
    AppComponent.stompClient.send(`/game/${this.gameId}/confirmGame`, {});

  }

  ngOnInit() {

    let formList = document.getElementsByTagName('form');
	
    for(let form of <any>formList) {
      form.addEventListener('submit', e => {
        e.preventDefault();
      });
    }

    
    document.addEventListener('keyup', (event) => {
      const keyName = event.key;

      // As the user releases the Ctrl key, the key is no longer active,
      // so event.ctrlKey is false.
      if (keyName === 'Escape') {
        console.log('Escape pressed');
        this.selectedAttacking = null;
        this.selectedHand = null;
        this.selectedHeroPower = false;
    }}, false);
    
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
      concernedPlayer.attack(msg.cardId, msg.targetId, msg.hero);
    });

    // On reçoit un cast d'un targetedSpell
    AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/castTargetedSpell`, data => {
      console.log(`event: castTargetedSpell, data: ${data.body}`);
      let msg = JSON.parse(data.body);

      let concernedPlayer : Player = this.getPlayer(msg.playerName);

      // On fait cast le spell par le joueur concerné
      concernedPlayer.castTargetedSpell(msg.cardId, msg.targetId, msg.own, msg.hero);
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
      concernedPlayer.castTargetedSpecial(msg.targetId, msg.own, msg.hero);
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
        this.infoLog = "Votre tour est terminé";
      } else {
        this.infoLog = "Le tour adverse est terminé";
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
    AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/victory`, data => {
      console.log(`event: win, data: ${data.body}`);
      let msg = JSON.parse(data.body);

      let concernedPlayer : Player = this.getPlayer(msg.playerName);

      if(concernedPlayer == this.joueur) {
        this.infoLog = "Vous avez gagné !";
      } else {
        this.infoLog = "Vous avez perdu :/";
      }
    });


    AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/drawCard`, data => {
      console.log(`event: drawCard, data: ${data.body}`);
      let msg = JSON.parse(data.body);

      let card: Card;
      let concernedPlayer : Player = this.getPlayer(msg.playerName);

      if(msg.cardType == "minion") {
        fetch('http://localhost:8080/cards/getMinion?name=' + msg.cardName)
        .then( response => {
          return response.json();
        })
        .then( response => {

          let capacities: Set<String> = new Set<String>();
          if(response.taunt) {
            capacities.add("taunt");
          }
          if(response.lifesteal) {
            capacities.add("lifesteal");
          }
          if(response.charge) {
            capacities.add("charge");
          }

          let boosts: Map<string, number> = new Map<string, number>();
          boosts.set("life", response.boostHealth as number);
          boosts.set("damage", response.boostDamage as number);

          card = new CardMinion(msg.cardId, response.name, response.manaCost, response.damageBase, response.HealthMax, capacities, boosts, concernedPlayer);
        });
      } else if(msg.cardType == "spell") {
        fetch('http://localhost:8080/cards/getSpell?name=' + msg.cardName)
        .then( response => {
          return response.json();
        })
        .then( response => {

          let ste: Set<SingleTargetEffect> = response.ste;
          let mte: Set<MultipleTargetEffect> = response.mte;
          let ge: Set<GlobalEffect> = response.ge;

          card = new CardSpell(msg.cardId, response.name, response.manaCost, ste, mte, ge, concernedPlayer);
        });
      }

    concernedPlayer.drawSpecific(card);
      

    });



  }




  init() {
    console.log('Initialisation');
    this.joueur = new Player(AppComponent.joueurName, AppComponent.joueurHero);
    this.opponent = new Player(AppComponent.opponentName, AppComponent.opponentHero);
    this.joueur.setOpponent(this.opponent);
    this.opponent.setOpponent(this.joueur);

    this.loadDecks();

    let cardTest1: CardMinion = new CardMinion(1,"Recrue de la main d'argent", 1, 1, 1, new Set<String>(), new Map<String, number>(), this.joueur);
    let cardTest2: CardMinion = new CardMinion(2, "Champion Frisselame", 3, 4, 2, new Set<String>().add("charge"), new Map<String, number>(), this.joueur);
    let cardTest3: CardSpell = new CardSpell(3, "Métamorphose", 4, new Set<SingleTargetEffect>(), new Set<MultipleTargetEffect>(), new Set<GlobalEffect>(), this.joueur);
    let cardTest4: CardMinion = new CardMinion(4, "Soldat du Comté de l'or", 1, 1, 2, new Set<String>().add("taunt"), new Map<String, number>(), this.joueur)

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
    this.opponent.board.set(4, cardTest4);
    

  }







  loadDecks() {

    //On charge toutes les cartes neutre :
    fetch('http://localhost:8080/cards/deckMinion?deck=shared')
      .then( response => {
        return response.json();
      })
      .then( response => {
        for(let i = 0 ; i < response.length ; i++) {

          let capacities = new Set();
          if(response.charge) {
            capacities.add("charge");
          }
          if(response.lifesteal) {
            capacities.add("lifesteal");
          }
          if(response.taunt) {
            capacities.add("taunt");
          }

          let boosts = new Map<string, number>();
          boosts.set("health", response.boostHealth);
          boosts.set("damage", response.boostDamage);
    
    
          let cardJoueur: CardMinion = new CardMinion(i, response.name, response.manaCost, response.damageBase, response.healthMax, capacities, boosts, this.joueur)
          this.joueur.deck.add(cardJoueur);
        }
      });



    //On charge tout les minions spécifiques au héros :
    fetch('http://localhost:8080/cards/deckMinion?deck=' + AppComponent.joueurHero)
    .then( response => {
      return response.json();
    })
    .then( response => {
      for(let i = 0 ; i < response.length ; i++) {

        let capacities = new Set();
        if(response.charge) {
          capacities.add("charge");
        }
        if(response.lifesteal) {
          capacities.add("lifesteal");
        }
        if(response.taunt) {
          capacities.add("taunt");
        }

        let boosts = new Map<string, number>();
        boosts.set("health", response.boostHealth);
        boosts.set("damage", response.boostDamage);
  
  
        let cardJoueur: CardMinion = new CardMinion(i, response.name, response.manaCost, response.damageBase, response.healthMax, capacities, boosts, this.joueur)
        this.joueur.deck.add(cardJoueur);
      }
    });

    //On charge tout les spells spécifique au héros :
    fetch('http://localhost:8080/cards/deckSpell?deck=' + AppComponent.joueurHero)
    .then( response => {
      return response.json();
    })
    .then( response => {
      for(let i = 0 ; i < response.length ; i++) {
  
        let cardJoueur: CardSpell = new CardSpell(i, response.name, response.manaCost, response.ste, response.mte, response.ge, this.joueur)
        this.joueur.deck.add(cardJoueur);
      }
    });

  }














  selectCardHand(card: Card): void {
    this.selectedHand = card;

    // S'il ne possède pas de targetable spell, on le lance direct
    if(!this.selectedHand.hasTargetedSpell()) {
      card.playReceived(this.id);
      this.selectedHand = null;
    } else {
      this.joueur.hand.forEach((value: Card, key: number) => {
        value.setTargetable(false);
      });
  
      this.selectedHand.setTargetable(true);
    }

  }


  selectCardPlayerBoard(card: CardMinion): void {
    this.selectedAttacking = card;

    // On opacifie les autres cartes de notre board
    this.joueur.board.forEach((value: Card, key: number) => {
      value.setTargetable(false);
    });

    card.setTargetable(true);

    // On vérifie s'il y a au moins un taunt
    let nbTaunt: number = 0;
    this.opponent.board.forEach((value: CardMinion, key: number) => {
      if(value.isProvoking()) {
        nbTaunt++;
      }
    });

    // On grise toutes les entités adverses, sauf celles avec taunt, s'il y a au moins un taunt
    if(nbTaunt > 0) {
      this.opponent.hero.setTargetable(false);
      this.opponent.board.forEach((value: CardMinion, key: number) => {
        value.setTargetable(value.isProvoking());
      });
    }
  }


  selectCardOpponentBoard(card: CardMinion): void {
    // Si on avait déjà choisi une carte sur le board, on lance une attaque sur card
    if(this.selectedAttacking != null) {
      console.log('Envoi de attack sur ' + card.name);
      AppComponent.stompClient.send(`/game/${this.id}/attack`, {}, JSON.stringify({isHero: false, cardId: this.selectedAttacking.id, targetId: card.id}));
      this.selectedAttacking = null;
    }
    // Sinon, si on avait déjà choisit une carte dans la main, on la joue avec pour cible card
    else if(this.selectedHand != null) {
      this.selectedHand.playReceived(this.id, card);
      this.selectedHand = null;
    }
    // Sinon, si on avait déjà cliquer sur le héro power, on le joue avec pour cible card
    else if(this.selectedHeroPower) {
      this.joueur.specialReceived(this.id, card);
      this.selectedHeroPower = false;
    }
  }

  selectOpponent(): void {
    console.log("Click opponent");
    // Si on avait déjà choisi une carte sur le board, on lance une attaque sur card
    if(this.selectedAttacking != null) {
      console.log('Envoi de attack sur le hero');
      AppComponent.stompClient.send(`/game/${this.id}/attack`, {}, JSON.stringify({isHero: true, cardId: this.selectedAttacking.id, targetId: null}));
      this.selectedAttacking = null;
    }
    // Sinon, si on avait déjà choisit une carte dans la main, on la joue avec pour cible le héros adverse
    else if(this.selectedHand != null) {
      this.selectedHand.playReceived(this.id, this.opponent.hero);
    }
    // Sinon, si on avait déjà cliquer sur le héro power, on le joue avec pour cible card
    else if(this.selectedHeroPower) {
      this.joueur.specialReceived(this.id, this.opponent.hero);
      this.selectedHeroPower = false;
    }
  }

  selectHero(): void {
    console.log("Click hero");
    // Si on avait déjà choisit une carte dans la main, on la joue avec pour cible notre héros
    if(this.selectedHand != null) {
      this.selectedHand.playReceived(this.id, this.joueur.hero);
    }
  }


  special(): void {
    if(AppComponent.joueurHero == "mage") {
      this.selectedHeroPower = true;
    } else {
      this.joueur.specialReceived(this.id);
    }
  }

    endTurn(): void {
      console.log('Envoi de fin du tour');
      AppComponent.stompClient.send(`/game/${this.id}/endTurn`, {});
    }
  



    getImgUrl(card: Card) {
      let name = card.name;
      name = name.replace(/ /g,'') //On vire les espaces
      name = name.replace(/'/g, ''); //On vire les '
      name = name.replace(/-/g, ''); //On vire les -
      name = name.replace(/_/g, ''); //On vire les _
      let url = '../../assets/images/cards/' + name.toLocaleLowerCase() + '.png';
      console.log('URLIMG : ' + url);
      return url;
    }











    isTargetable(card: Card) {
      console.log('Check is targetable');
      return card.isTargetable();
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


  test() {
    console.log("Envoi draw");
    AppComponent.stompClient.send(`/game/${this.gameId}/drawCard`, {}, JSON.stringify({playerName: this.joueur.name}));
  }
}
