import { Component, OnInit } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

import { SingleTargetEffect, MultipleTargetEffect, GlobalEffect, Transform } from './effect.service';
// import { HeroMage, HeroPaladin, HeroWarrior } from './heroes.service';
import { ConstantesService } from './constantes.service';
import { TouchSequence } from 'selenium-webdriver';
import { UUID } from 'angular2-uuid';



@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor() {
    AppComponent.title = 'client-angular';
    this.currentPage = 'home';

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

  /* Variables utilisées dans app-game et récupérées ici, on les met donc en static pour y accéder depuis app-game */
  static joueurName: string;
  static joueurHero: string;
  static opponentName: string;
  static opponentHero: string;
  static playing: string;
  static gameId: string;

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
              
            AppComponent.joueurName = data.playerName;
            AppComponent.joueurHero = data.playerHero;
            AppComponent.opponentName = data.opponentName;
            AppComponent.opponentHero = data.opponentHero;
            AppComponent.playing = data.playing;
            AppComponent.gameId = data.gameId;

            this.currentPage = 'game';
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

  


}











//------------------------------------------ TS Logic

export class Player {
    name: String;
    deck: Set<Card>;
    hand: Map<string, Card>;
    board: Map<string, CardMinion>;

    manaMax: number;
    mana: number;

    hero: Hero;
    opponent: Player;


    constructor(name: String, heroType: String) {
        this.name = name;
        switch (heroType) {
            case 'mage':
                this.hero = new HeroMage(this);
                break;
            case 'paladin':
                this.hero = new HeroPaladin(this);
                break;
            case 'warrior':
                this.hero = new HeroWarrior(this);
                break;
        }
        this.deck = new Set<Card>();
        this.hand = new Map<string, Card>();
        this.board = new Map<string, CardMinion>();

        this.manaMax = 0;
        this.mana = this.manaMax;

    }




    summon(cardId: string) {
        let card: CardMinion = this.hand.get(cardId) as CardMinion;
        this.mana = this.mana - card.manaCost;
        this.hand.delete(cardId);
        
        // On applique les boosts

        this.board.set(cardId, card);
    }
    
    attack(cardId: string, targetId: string, isHero: boolean) {
        let card: CardMinion = this.hand.get(cardId) as CardMinion;
        let target: Entity;

        if(isHero) {
            target = this.opponent.hero;
        } else {
            target = this.opponent.hand.get(targetId) as CardMinion;
        }
        card.attack(target);
    }

    castTargetedSpell(cardId: string, targetId: string, own: string, hero: boolean) {
        let card: CardSpell = this.hand.get(cardId) as CardSpell;
        this.mana = this.mana - card.manaCost;
		let target: Entity = this.foundTarget(targetId, own, hero);
		// Supprimer la carte de la main
        this.hand.delete(cardId);
        card.play(target);
    }

    castUntargetedSpell(cardId: string) {
        let card: CardSpell = this.hand.get(cardId) as CardSpell;
        this.mana = this.mana - card.manaCost;
        this.hand.delete(cardId);
		card.play();
		// Supprimer la carte de la main
    }

    castTargetedSpecial(targetId: string, own: string, hero: boolean) {
        this.mana = this.mana - ConstantesService.HEROPOWERMANACOST;
        let target: Entity = this.foundTarget(targetId, own, hero);
        this.hero.special(target);
    }

    castUntargetedSpecial() {
        this.mana = this.mana - ConstantesService.HEROPOWERMANACOST;
        this.hero.special();
    }

    beginTurn(cardName: string, cardId: string, cardType: string) {
        this.manaMax = Math.min(this.manaMax + 1, ConstantesService.MANAMAX); // Incrémentation de manaMax de 1
        this.mana = this.manaMax;

        // On remet tout ça à true
        this.hero.specialUsable = true;
        this.board.forEach((value: CardMinion, key: string) => {
            value.canAttack = true;
        });


            let card: Card;
      
            if(cardType == "minion") {
              fetch('http://localhost:8080/cards/getMinion?name=' + cardName)
              
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
      
                card = new CardMinion(cardId, response.name, response.manaCost, response.damageBase, response.HealthMax, capacities, boosts, this);
                this.drawSpecific(card);
              });
            } else if(cardType == "spell") {
      
              fetch('http://localhost:8080/cards/getSpell?name=' + cardName)
              .then( response => {
                return response.json();
              })
              .then( response => {
                let ste: Set<SingleTargetEffect> = response.ste;
                let mte: Set<MultipleTargetEffect> = response.mte;
                let ge: Set<GlobalEffect> = response.ge;
      
                card = new CardSpell(cardId, response.name, response.manaCost, ste, mte, ge, this);
                this.drawSpecific(card);
              });
            }
    }



    drawSpecific(card: Card) {
        this.hand.set(card.id, card);
    }

    setOpponent(p: Player): void {
        this.opponent = p;
    }



    getName(): String {
        return this.name;
    }

    getDeck(): Set<Card> {
        return this.deck;
    }

    getHand(): Map<string, Card> {
        return this.hand;
    }

    getBoard(): Map<string, CardMinion> {
        return this.board;
    }

    getHero(): Hero {
        return this.hero;
    }

    getOpponent(): Player {
        return this.opponent;
    }

    getManaMax(): number {
        return this.manaMax;
    }

    getMana(): number {
        return this.mana;
    }

    foundTarget(targetId: string, own: String, hero: boolean): Entity {
        let target: Entity;

        if(own == "true") { //Own indique si l'entité visée est du coté du joueur
            if(hero) {
                target = this.hero;
            } else {
                target = this.hand.get(targetId) as CardMinion;
            }
        } else if(own == "false") {
            if(hero) {
                target = this.opponent.hero;
            } else {
                target = this.opponent.hand.get(targetId) as CardMinion;
            }
        } else {
            console.log("Own not defined");
        }

        return target;
    }

    specialReceived(gameId: string, e?: Entity) {
        this.hero.specialReceived(gameId, e);
    }

}

export interface Entity {
    boostHealth(quantity: number);
    boostArmor(quantity: number);
    heal(quantity: number);
    takeDamage(quantity: number);
    getDamage(): number;
    isProvoking(): void;
    isDead(): boolean;
    getOwner(): Player;
    isTargetable(): boolean; // Usefull for the html, do not delete
}














export abstract class Hero implements Entity {
    health: number;
    healthMax: number;
    armor: number;
    taunt: boolean;

    name: String;
    portrait: String;
    
    owner: Player;

    targetable: boolean; // For html
    specialUsable: boolean; // The special is specific to the hero, not the player

    constructor(name: String, portrait: String, owner: Player) {
        this.health = ConstantesService.HEROMAXHEALTH;
        console.log("VIEDUHERO : " + this.health);
        this.healthMax = ConstantesService.HEROMAXHEALTH;
        this.armor = 0;
        this.taunt = false;

        this.name = name;
        this.portrait = portrait;

        this.owner = owner;

        this.targetable = true;
        this.specialUsable = true;
    }


    takeDamage(quantity: number): void {
        this.armor = this.armor - quantity;
        if(this.armor < 0) { //Si on a cassé toute l'armure
            this.health += this.armor;
            this.armor = 0;
        }
    }

    boostHealth(quantity: number) {
        this.health = this.health + quantity;
        this.healthMax = this.health + quantity;
    }

    boostArmor(quantity: number) {
        this.armor = this.armor + quantity;
    }

    heal(quantity: number) {
        if(this.health + quantity > this.healthMax) {
            this.health = this.healthMax;
        } else {
            this.health = this.health + quantity;
        }
    }

    getDamage(): number {
        return 0;
    }

    isProvoking(): boolean {
        return this.taunt;
    }

    isDead(): boolean {
        return (this.health <= 0);
    }

    getOwner(): Player {
        return this.owner;
    }

    isTargetable() {
        return this.targetable;
    }

    special(e?: Entity) {}

    setTargetable(bool: boolean): void {
        this.targetable = bool;
    }

    isSpecialUsable(): boolean {
        return this.specialUsable;
    }
    
    abstract specialReceived(gameId: string, e?: Entity);
    abstract normalMode();
    abstract alternativMode();
}











export abstract class Card {
    id: string;
    name: String;
    manaCost: number;
    owner: Player;

  constructor(id: string, name: String, manaCost: number, owner: Player) {
      this.id = id;
      this.name = name;
      this.manaCost = manaCost;
      this.owner = owner;
  }

  play(e?: Entity): void {}

  getId(): string {
      return this.id;
  }

  getName(): String {
      return this.name;
  }


  getManaCost(): number {
      return this.manaCost;
  }

  /* The three following method are for html display */
  abstract isTargetable(): boolean;
  abstract setTargetable(bool: boolean): void;
  abstract hasTargetedSpell(): boolean;
  abstract playReceived(gameId: string, target?: Entity): void;

  abstract clone();
}










export class CardMinion extends Card implements Entity {
  damageBase: number;
  damage: number;
  damageBoosted: number;

  healthMax: number;
  health: number;
  healthBoosted: number;

  capacities: Set<String>; // Taunt, charge, lifesteal...
  
  boosts: Map<String, number>;
  canAttack: boolean;
  provocation: boolean; // We will often nedd these, so we made them variables instead of having to search capacities everytime

  targetable: boolean; // For html

  constructor(id: string,
              name: String,
              mana: number,
              damage: number,
              health: number,
              capacities: Set<String>,
              boosts: Map<String, number>,
              owner: Player) {

      super(id, name, mana, owner);

      this.damageBase = damage;
      this.damage = damage;
      this.damageBoosted = 0;

      this.healthMax = health;
      this.health = health;
      this.healthBoosted = 0;

      this.capacities = capacities;
      this.boosts = boosts;
      this.canAttack = capacities.has('charge');
      this.provocation = capacities.has('provocation');

      this.targetable = true;
  }

    attack(target: Entity): void {
        this.takeDamage(target.getDamage());
        target.takeDamage(this.getDamage());
        this.canAttack = false;
    }

    takeDamage(quantity: number) {
        this.health = this.health - quantity;
    }

    boostDamage(quantity: number) {
        this.damage = this.damage + quantity;
        this.damageBoosted = this.damageBoosted + quantity;
    }

    boostHealth(quantity: number) {
        this.health = this.health + quantity;
        this.healthMax = this.health + quantity;
        this.healthBoosted = this.healthBoosted + quantity;
    }

    boostArmor(quantity: number): TypeError {
        throw new TypeError("BoostArmor on minion");
    }

    heal(quantity: number) {
        if(this.health + quantity > this.healthMax) {
            this.health = this.healthMax;
        } else {
            this.health = this.health + quantity;
        }
    }


    getDamage(): number {
        return this.damage;
    }

    getDamageBase(): number {
        return this.damageBase;
    }

    getDamageBoosted(): number {
        return this.damageBoosted;
    }

    getHealth(): number {
        return this.health;
    }

    getHealthMax(): number {
        return this.healthMax;
    }

    getHealthBoosted(): number {
        return this.healthBoosted;
    }

    getCapacities(): Set<String> {
        return this.capacities;
    }

    getCanAttack(): boolean { // canAttack already taken
        return this.canAttack;
    }

    isProvoking(): boolean {
        return this.provocation;
    }

    isDead(): boolean {
        return (this.health <= 0);
    }
    
    getOwner(): Player {
        return this.owner;
    }

    isTargetable(): boolean {
        return this.targetable;
    }

    setTargetable(bool: boolean): void {
        this.targetable = bool;
    }
    
    hasTargetedSpell(): boolean {
        return false;
    }

    clone(): CardMinion {
        let card: CardMinion = new CardMinion(this.id, this.name, this.manaCost, this.damage, this.health, this.capacities, this.boosts, this.owner);
        return card;
    }

    playReceived(gameId: string): void {
        console.log("Envoi de summonMinion du minion " + this.attack.name);
        AppComponent.stompClient.send(`/app/game/${gameId}/summonMinion`, {}, JSON.stringify({cardId: this.id}));
    }
}










export class CardSpell extends Card {
  singleEffects: Set<SingleTargetEffect>;
  multipleEffects: Set<MultipleTargetEffect>;
  globalEffects: Set<GlobalEffect>;

  targetable: boolean; // For html

  constructor(id: string,
              name: String,
              mana: number,
              singleEffects: Set<SingleTargetEffect>,
              multipleEffects: Set<MultipleTargetEffect>,
              globalEffects: Set<GlobalEffect>,
              owner: Player) {
      super(id, name, mana, owner);
      this.singleEffects = singleEffects;
      this.multipleEffects = multipleEffects;
      this.globalEffects = globalEffects;

      this.targetable = true;
  }

  play(e?: Entity) {
        let player = this.owner;
        let opponent = this.owner.opponent;

        if(e != null) {
            this.singleEffects.forEach( effect => {
                effect.cast(e);
            });
        }
        this.multipleEffects.forEach( effect => {
            effect.cast(player.hero, player.board, opponent.hero, opponent.board);

        });
        this.globalEffects.forEach( effect => {
            effect.cast(player.hero, player.deck, player.hand, player.board, opponent.hero, opponent.deck, opponent.hand, opponent.board);
        });
  }

    isTargetable(): boolean {
        return this.targetable;
    }

    setTargetable(bool: boolean): void {
        this.targetable = bool;
    }

    hasTargetedSpell(): boolean {
        return this.singleEffects.size > 0;
    }

    clone(): CardSpell {
        let card: CardSpell = new CardSpell(this.id, this.name, this.manaCost, this.singleEffects, this.multipleEffects, this.globalEffects, this.owner);
        return card;
    }


    playReceived(gameId: string, e?: Entity): void {
        // Si on a reçu l'envoi avec une target
        if(e) {

            // On vérifie d'abord si l'on a trouvé un héro
            // Il faut envoyer si l'entité est de notre coté du plateau
            if(e == this.owner.hero) {
                console.log("Envoi de castTargetedSpell sur notre hero");
                AppComponent.stompClient.send(`/app/game/${gameId}/castTargetedSpell`, {}, JSON.stringify({own: "true", isHero: "true", cardId: this.id, targetId: null}));
            } else if(e == this.owner.opponent.hero) {
                console.log("Envoi de castTargetedSpell sur le hero adverse");
                AppComponent.stompClient.send(`/app/game/${gameId}/castTargetedSpell`, {}, JSON.stringify({own: "false", isHero: "true", cardId: this.id, targetId: null}));
            } else {

                // Sinon, on cherche la cible sur le plateau
                try {
                    let targetId: string = (e as CardMinion).id; // Work because we only get their if e is not a hero
                    if(this.owner.board.get(targetId)) {
                        console.log("Envoi de castTargetedSpell de " + this.name + " sur notre board");
                        AppComponent.stompClient.send(`/app/game/${gameId}/castTargetedSpell`, {}, JSON.stringify({own: "true", isHero: "false", cardId: this.id, targetId: targetId}));
                    } else if(this.owner.opponent.board.get(targetId)) {
                        console.log("Envoi de castTargetedSpell de " + this.name + " sur le board adverse");
                        AppComponent.stompClient.send(`/app/game/${gameId}/castTargetedSpell`, {}, JSON.stringify({own: "false", isHero: "false", cardId: this.id, targetId: targetId}));
                    } else {
                        console.log("Carte cible non trouvée");
                    }
                } catch {
                    console.log("Erreur findCard target");
                }

                
            }
            
        } else {
            // Si on a reçu un envoi sans target
            console.log("Envoi de castUntargetedSpell de " + this.name);
            AppComponent.stompClient.send(`/app/game/${gameId}/castUntargetedSpell`, {}, JSON.stringify({cardId: this.id}));
        }
    }
}








// ---------------------HEROES--------------------- //

export class HeroMage extends Hero {

    constructor(owner: Player) {
        super("Jaina", "../../assets/images/portrait/Jaina_portrait.png", owner);
    }

    // On reçoit depuis le serveur
    special(e: Entity) {
        e.takeDamage(1);
        this.specialUsable = false;
    }

    // On reçoit depuis le client
    specialReceived(gameId: string, e: Entity) {
        // On vérifie d'abord si l'on a trouvé un héro
        // Il faut envoyer si l'entité est de notre coté du plateau
        if(e == this.owner.hero) {
            console.log("Envoi de castTargetedSpecial sur notre hero");
            AppComponent.stompClient.send(`/app/game/${gameId}/heroTargetedSpecial`, {}, JSON.stringify({own: "true", isHero: "true", targetId: null}));
        } else if(e == this.owner.opponent.hero) {
            console.log("Envoi de castTargetedSpecial sur le hero adverse");
            AppComponent.stompClient.send(`/app/game/${gameId}/heroTargetedSpecial`, {}, JSON.stringify({own: "false", isHero: "true", targetId: null}));
        } else {

            // Sinon, on cherche la cible sur le plateau
            try {
                let targetId = (e as CardMinion).id; // Work because we only get their if e is not a hero
                if(this.owner.board.get(targetId)) {
                    console.log("Envoi de castTargetedSpecial de mage sur notre board");
                    AppComponent.stompClient.send(`/app/game/${gameId}/heroTargetedSpecial`, {}, JSON.stringify({own: "true", isHero: "false", targetId: targetId}));
                } else if(this.owner.opponent.board.get(targetId)) {
                    console.log("Envoi de castTargetedSpecial de mage sur le board adverse");
                    AppComponent.stompClient.send(`/app/game/${gameId}/heroTargetedSpecial`, {}, JSON.stringify({own: "false", isHero: "false", targetId: targetId}));
                } else {
                    console.log("Carte cible non trouvée");
                }
            } catch {
                console.log("Erreur findCard target");
            }

        }
    }


    normalMode(): void {
        console.log('normal mage');
        this.name = "Jaina";
        this.portrait = "../../assets/images/portrait/Jaina_portrait.png";
    }
    alternativMode(): void {
        console.log('pasmage')
        this.name = "Pasmage";
        this.portrait = "../../assets/images/portrait/Jaina_portrait_alternativ.png";
    }
}


export class HeroPaladin extends Hero {

    constructor(owner: Player) {
        super("Uther", "../../assets/images/portrait/Uther_portrait.png", owner);
    }

    // On reçoit depuis le serveur
    special() {
        this.specialUsable = false;
        // Rien à faire, on va recevoir un summonMinion de Silverhand recruit sur summonMinion
    }

    // On reçoit depuis le client
    specialReceived(gameId: string) {
        console.log("Envoi de castTargetedSpecial de paladin");
        AppComponent.stompClient.send(`/app/game/${gameId}/heroUntargetedSpecial`, {});
    }


    normalMode(): void {
        console.log("uther normal");
        this.name = "Uther";
        this.portrait = "../../assets/images/portrait/Uther_portrait.png";
    }
    alternativMode(): void {
        console.log("uther slidesbringer");
        this.name = "Uther Slidesbringer";
        this.portrait = "../../assets/images/portrait/Uther_portrait_alternativ.png";
    }

}

export class HeroWarrior extends Hero {

    constructor(owner: Player) {
        super("Garrosh", "../../assets/images/portrait/Garrosh_portrait.png", owner);
    }

    // On reçoit depuis le serveur
    special() {
        this.specialUsable = false;
        this.boostArmor(2);
    }

    // On reçoit depuis le client
    specialReceived(gameId: string) {
        console.log('Envoi de castUntargetedSpecial pour le warrior');
        AppComponent.stompClient.send(`/app/game/${gameId}/heroUntargetedSpecial`, {});
    }

    normalMode(): void {
        console.log("garrosh normal");
        this.name = "Garrosh";
        this.portrait = "../../assets/images/portrait/Garrosh_portrait.png";
    }
    alternativMode(): void {
        console.log("chantosh");
        this.name = "Chantosh";
        this.portrait = "../../assets/images/portrait/Garrosh_portrait_alternativ.png";
    }

}

