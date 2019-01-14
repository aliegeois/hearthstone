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
    /*document.getElementById('button').addEventListener('click', () => {
      this.sendMessage('testMessage');
    });*/

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
        this.board.set(cardId, card);
    }
    
    attack(cardId: string, targetId: string) {
        let card: CardMinion = this.hand.get(cardId) as CardMinion;
        let target: Entity;

        if(targetId == "hero") {
            target = this.opponent.hero;
        } else {
            target = this.opponent.hand.get(cardId) as CardMinion;
        }
        card.attack(target);
    }

    castTargetedSpell(cardId: string, targetId: string, own: string) {
        let card: CardSpell = this.hand.get(cardId) as CardSpell;
        this.mana = this.mana - card.manaCost;
        let target: Entity = this.foundTarget(targetId, own);

        card.play(target);
    }

    castUntargetedSpell(cardId: string) {
        let card: CardSpell = this.hand.get(cardId) as CardSpell;
        this.mana = this.mana - card.manaCost;
        card.play();
    }

    castTargetedSpecial(targetId: string, own: string) {
        this.mana = this.mana - ConstantesService.HEROPOWERMANACOST;
        let target: Entity = this.foundTarget(targetId, own);
        this.hero.special(target);
    }

    castUntargetedSpecial() {
        this.mana = this.mana - ConstantesService.HEROPOWERMANACOST;
        this.hero.special();
    }

    beginTurn() {
        this.manaMax = Math.min(this.manaMax++, ConstantesService.MANAMAX); // Incrémentation de manaMax de 1
        this.mana = this.manaMax;
        //this.drawCard();
    }

    drawCard(): UUID {
        let cardDrawn: Card = Array.from(this.deck)[Math.random() * this.deck.size];
        let identif: string = ConstantesService.generateUUID();

        let card = cardDrawn.clone();
		card.id = identif;
		this.hand.set(identif, card);

		return identif;
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



    foundTarget(targetId: string, own: String): Entity {
        let target: Entity;

        if(own == "true") { //Own indique si l'entité visée est du coté du joueur
            if(targetId == "hero") {
                target = this.hero;
            } else {
                target = this.hand.get(targetId) as CardMinion;
            }
        } else if(own == "false") {
            if(targetId == "hero") {
                target = this.opponent.hero;
            } else {
                target = this.opponent.hand.get(targetId) as CardMinion;
            }
        } else {
            console.log("Own not defined");
        }

        return target;
    }
}

export interface Entity {
    boostHealth(quantity: number);
    heal(quantity: number);
    takeDamage(quantity: number);
    getDamage(): number;
    isProvoking(): void;
    isDead(): boolean;
    getOwner(): Player;
}














export abstract class Hero implements Entity {
    health: number;
    healthMax: number;
    armor: number;
    taunt: boolean;

    name: String;
    portrait: String;
    
    owner: Player;


    constructor(name: String, portrait: String, owner: Player) {
        this.health = ConstantesService.HEROMAXHEALTH;
        console.log("VIEDUHERO : " + this.health);
        this.healthMax = ConstantesService.HEROMAXHEALTH;
        this.armor = 0;
        this.taunt = false;

        this.name = name;
        this.portrait = portrait;

        this.owner = owner;
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

    special(e?: Entity) {}

    normalMode() {}

    alternativMode() {}
}











export abstract class Card {
    id: UUID;
    name: String;
    manaCost: number;
    owner: Player;

  constructor(id: UUID, name: String, manaCost: number, owner: Player) {
      this.id = id;
      this.name = name;
      this.manaCost = manaCost;
      this.owner = owner;
  }

  play(e?: Entity): void {}

  getId(): UUID {
      return this.id;
  }

  getName(): String {
      return this.name;
  }


  getManaCost(): number {
      return this.manaCost;
  }

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
  ready: boolean;
  provocation: boolean; // We will often nedd these, so we made them variables instead of having to search capacities everytime

  constructor(id: UUID,
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
      this.ready = capacities.has('charge');
      this.provocation = capacities.has('provocation');
  }

    attack(target: Entity): void {
        this.takeDamage(target.getDamage());
        target.takeDamage(this.getDamage());
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

    isReady(): boolean {
        return this.ready;
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


    clone(): CardMinion {
        let card: CardMinion = new CardMinion(this.id, this.name, this.manaCost, this.damage, this.health, this.capacities, this.boosts, this.owner);
        return card;
    }
}










export class CardSpell extends Card {
  singleEffects: Set<SingleTargetEffect>;
  multipleEffects: Set<MultipleTargetEffect>;
  globalEffects: Set<GlobalEffect>;

  constructor(id: UUID,
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

  clone(): CardSpell {
    let card: CardSpell = new CardSpell(this.id, this.name, this.manaCost, this.singleEffects, this.multipleEffects, this.globalEffects, this.owner);
    return card;
}
}








// ---------------------HEROES--------------------- //

export class HeroMage extends Hero {

    constructor(owner: Player) {
        super("Jaina", "../../assets/images/portrait/Jaina_portrait.png", owner);
    }

    special(e: Entity) {
        e.takeDamage(1);
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

    special() {
        let uuid = ConstantesService.generateUUID();
        let minion: CardMinion = new CardMinion(uuid, "Silverhand Recruit", 1, 1, 1, new Set<String>(), new Map<String, number>(), this.owner);
        this.owner.board.set(uuid, minion);
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

    special() {
        this.boostArmor(2);
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

