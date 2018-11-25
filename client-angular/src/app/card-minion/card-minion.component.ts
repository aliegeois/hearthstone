import { Component, OnInit } from '@angular/core';
import { PlayerComponent } from '../player/player.component';

@Component({
  selector: 'app-card-minion',
  templateUrl: './card-minion.component.html',
  styleUrls: ['./card-minion.component.scss']
})
export class CardMinionComponent implements OnInit {

	id: number;
	name: String;
	owner: PlayerComponent;
	manaCost: number;

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
  	this.id = id;
  	this.owner = owner;
  	this.name = name;
  	this.manaCost = manaCost;

  	this.damage = damage;
  	this.damageBase = damage;
  	this.damageBoosted = 0;

  	this.health = health;
  	this.healthBase = health;
  	this.healthBoosted = 0;

  	this.effects = effects;
  	this.boosts = boosts;
  	//TODO : this.ready = effects.contains("charge");
  }


  ngOnInit() {
  }

}
