import { Component, OnInit } from '@angular/core';
import { PlayerComponent } from '../player/player.component';
import { ConstantesComponent } from '../constantes/constantes.component';
import { AppComponent } from '../app.component';

@Component({
  selector: 'app-hero',
  templateUrl: './hero.component.html',
  styleUrls: ['./hero.component.scss']
})
export class HeroComponent implements OnInit {

	constantes: ConstantesComponent;

	player: PlayerComponent;

	health: number;
	healthMax: number;
	armor: number;

  constructor(player: PlayerComponent) {
  	this.player = player;
  	this.constantes = new ConstantesComponent();
  	this.health = this.constantes.heroMaxHealth;
  	this.health = this.constantes.heroMaxHealth;
  	this.armor = 0;
  }

  ngOnInit() {
  }

  takeDamage(quantity: number) {
  	this.health = this.health - quantity;
  }

  heal(quantity: number) {
  	this.health = this.health + quantity;
  	if(this.health > this.healthMax) {
  		this.health = this.healthMax;
  	}
  }

  boostheal(quantity: number) {
  	this.health = this.health + quantity;
  	this.healthMax = this.healthMax + quantity;
  }

}


export class HeroWarriorComponent extends HeroComponent implements OnInit {

	constructor(player: PlayerComponent) {
		super(player);
	}

	special() {
		this.armor = this.armor + 2;
	}

  ngOnInit() {
  }
}
export class HeroMageComponent extends HeroComponent implements OnInit {

  constructor(player: PlayerComponent) {
    super(player);
  }

  special() {
    //TODO
  }

  ngOnInit() {
  }
}
export class HeroPaladinComponent extends HeroComponent implements OnInit {

  constructor(player: PlayerComponent) {
    super(player);
  }

  special() {
    //TODO
  }

  ngOnInit() {
  }
}