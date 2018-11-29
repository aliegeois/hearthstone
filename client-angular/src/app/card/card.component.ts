import { Component, OnInit } from '@angular/core';
import { PlayerComponent } from '../player/player.component';
import { EffectsComponent } from '../effects/effects.component';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss']
})


export class CardComponent implements OnInit {

	id: number;
	name: String;
	owner: PlayerComponent;
	manaCost: number;

  constructor(id: number, name: String, owner: PlayerComponent, manaCost: number) {
  	this.id = id;
  	this.name = name;
  	this.owner = owner;
  	this.manaCost = manaCost;
  }

  ngOnInit() {
  }


}



export class CardMinionComponent extends CardComponent implements OnInit {

	damage: number;
	damageBase: number;
	damageBoosted: number;

	health: number;
	healthBase: number;
	healthBoosted: number;

	effects: Set<String>;
	boosts: Map<String, number>;

	ready: boolean;

  constructor(id: number, name: String, owner: PlayerComponent, manaCost: number, damage: number, health: number, effects: Set<String>, boosts: Map<String, number>) {
  	super(id, name, owner, manaCost);

  	this.damage = damage;
  	this.damageBase = damage;
  	this.damageBoosted = 0;

  	this.health = health;
  	this.healthBase = health;
  	this.healthBoosted = 0;

  	this.effects = effects;
  	this.boosts = boosts;
  	this.ready = effects.has("charge");
  }


  ngOnInit() {
  }

  takeDamage(quantity: number) {
    this.health = this.health - quantity;
  }
  attackMinion(opponent: CardMinionComponent) {
    opponent.takeDamage(this.damage);
    this.takeDamage(opponent.damage);
  }
}


export class CardSpellComponent extends CardComponent implements OnInit {


  effects: Set<EffectsComponent>;

  constructor(id: number, name: String, owner: PlayerComponent, manaCost: number, effects: Set<EffectsComponent>) {
    super(id, name, owner, manaCost);
    this.effects = effects;
  }


  ngOnInit() {
  }

  getOwner() {
    return this.owner;
  }
}
