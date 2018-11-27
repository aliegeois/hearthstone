import { Component, OnInit } from '@angular/core';

import { HeroComponent } from '../hero/hero.component';
import { HeroWarriorComponent } from '../hero/hero.component';
import { HeroMageComponent } from '../hero/hero.component';
import { HeroPaladinComponent } from '../hero/hero.component';

import { CardComponent } from '../card/card.component';
import { CardMinionComponent } from '../card/card.component';

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {

	name: String;
	
  deck: CardComponent[];
  hand: Map<number, CardComponent>;
  board: Map<number, CardMinionComponent>;

  cardId: number;

	hero: HeroComponent; //Compile with warning, but it's fine

  opponent: PlayerComponent;

  constructor(name: String, heroType: String) {
    this.name = name;
  	switch(heroType) {
      case "warrior":
        this.hero = new HeroWarriorComponent(this);
        break;
      case "mage":
        this.hero = new HeroMageComponent(this);
        break;
      default:
        this.hero = new HeroPaladinComponent(this);
    }
      
  	//this.hero.special(); TODO : voir pour l'héritage
  }

  setOpponent(p: PlayerComponent) {
    this.opponent = p;
  }

  drawCard() {
    let cards: Array<CardComponent> = Array.from(this.hand.values());
    let card = cards[Math.floor(Math.random()*this.deck.length)];
    this.hand[this.cardId++] = card;
  }

  playMinion(cardId: number) {
    //minion: CardMinionComponent = this.hand.get(cardId);
    //TODO
  }

  attackMinion(minionId1: number, minionId2: number) {
    this.board.get(minionId1).attackMinion(this.board.get(minionId2));
  }

  useSpell(cardId: number, target: CardMinionComponent) {
    //TODO
  }

  heroSpecial(target: CardMinionComponent) {
    //this.hero.special(target);
    //TODO : voir pour le système d'héritage
  }


  getName() {
    return this.name;
  }
  getDeck() {
    return this.deck;
  }
  getHand() {
    return this.hand;
  }
  getBoard() {
    return this.board;
  }
  getHero() {
    return this.hero;
  }
  getOpponent() {
    return this.opponent
  }
  ngOnInit() {
  }

}
