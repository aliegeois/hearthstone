import { Component, OnInit } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

import { SingleTargetEffect, MultipleTargetEffect, GlobalEffect, Transform } from './effect.service';
// import { HeroMage, HeroPaladin, HeroWarrior } from './heroes.service';
import { ConstantesService } from './constantes.service';
import { TouchSequence } from 'selenium-webdriver';



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
              
            AppComponent.joueurName = data.joueurName;
            AppComponent.joueurHero = data.joueurHero;
            AppComponent.opponentName = data.opponentName;
            AppComponent.opponentHero = data.opponentHero;
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

    this.testNePasToucher();
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

  

  testNePasToucher() {
    let minion: CardMinion = new CardMinion(54, "Test", 4, 2, 3, new Set<String>(), new Map<String, number>());;
    let transformEffect = new Transform(minion);
    
  }

}











//------------------------------------------ TS Logic

export class Player {
  name: String;
  deck: Set<Card>;
  hand: Map<number, Card>;
  board: Map<number, CardMinion>;

  manaMax: number;
  mana: number;

  hero: Hero;
  opponent: Player;


  constructor(name: String, heroType: String) {
      this.name = name;
      switch (heroType) {
          case 'mage':
              this.hero = new HeroMage();
              break;
          case 'paladin':
              this.hero = new HeroPaladin();
              break;
          case 'warrior':
              this.hero = new HeroWarrior();
              break;
      }
      this.deck = new Set<Card>();
      this.hand = new Map<number, Card>();
      this.board = new Map<number, CardMinion>();

      this.manaMax = 0;
      this.mana = this.manaMax;
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

  getHand(): Map<number, Card> {
      return this.hand;
  }

  getBoard(): Map<number, CardMinion> {
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

}

export interface Entity {
  isProvoking(): void;
  isDead(): boolean;
}














export abstract class Hero implements Entity {
  health: number;
  healthMax: number;
  armor: number;
  taunt: boolean;

  name: String;
  portrait: String;


  constructor(name: String, portrait: String) {
      this.health = ConstantesService.HEROMAXHEALTH;
      console.log("VIEDUHERO : " + this.health);
      this.healthMax = ConstantesService.HEROMAXHEALTH;
      this.armor = 0;
      this.taunt = false;

      this.name = name;
      this.portrait = portrait;
  }




  isProvoking(): boolean {
      return this.taunt;
  }

  isDead(): boolean {
      return (this.health <= 0);
  }

  normalMode() {}

  alternativMode() {}
}











export abstract class Card {
  id: number;
  name: String;
  manaCost: number;

  constructor(id: number, name: String, manaCost: number) {
      this.id = id;
      this.name = name;
      this.manaCost = manaCost;
  }

  play(): void {}

  getId(): number {
      return this.id;
  }

  getName(): String {
      return this.name;
  }


  getManaCost(): number {
      return this.manaCost;
  }
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

  constructor(id: number,
              name: String,
              mana: number,
              damage: number,
              health: number,
              capacities: Set<String>,
              boosts: Map<String, number>) {

      super(id, name, mana);

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
}










export class CardSpell extends Card {
  singleEffects: Set<SingleTargetEffect>;
  multipleEffects: Set<MultipleTargetEffect>;
  globalEffects: Set<GlobalEffect>;

  constructor(id: number,
              name: String,
              mana: number,
              singleEffects: Set<SingleTargetEffect>,
              multipleEffects: Set<MultipleTargetEffect>,
              globalEffects: Set<GlobalEffect>) {
      super(id, name, mana);
      this.singleEffects = singleEffects;
      this.multipleEffects = multipleEffects;
      this.globalEffects = globalEffects;
  }

}








// ---------------------HEROES--------------------- //

export class HeroMage extends Hero {

  constructor() {
    super("Jaina", "../../assets/images/portrait/Jaina_portrait.png");
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

  constructor() {
    super("Uther", "../../assets/images/portrait/Uther_portrait.png");
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

  constructor() {
    super("Garrosh", "../../assets/images/portrait/Garrosh_portrait.png");
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

