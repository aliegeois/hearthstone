import { Component, OnInit } from '@angular/core';
import { SingleTargetEffect, MultipleTargetEffect, GlobalEffect } from './effect.service';
//import { HeroMage, HeroPaladin, HeroWarrior } from './heroes.service';
import { ConstantesService } from './constantes.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})


export class AppComponent implements OnInit{

	title: String;
	currentPage: String;

	constructor() {
		this.title = 'client-angular';
		this.currentPage = 'home';
	}

	ngOnInit() {
		document.getElementById('button').addEventListener('click', this.change);
	}


	change() {
		switch(this.currentPage) {
			case 'home':
				this.currentPage = 'lobby'
				break;
			case 'lobby':
				this.currentPage = 'home'
				break;
		}
	}
}











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
		/*switch(heroType) {
			case "mage":
				this.hero = new HeroMage(this);
				break;
			case "paladin":
				this.hero = new HeroPaladin(this);
				break;
			case "warrior":
				this.hero = new HeroWarrior(this);
				break;
		}*/
	}

	setOpponent(p: Player): void {
		this.opponent = p;
	}

	playMinion(minionId: number) {
		let minion: CardMinion = this.hand.get(minionId) as CardMinion;
		minion.play();
		this.board.set(minionId, minion);
		this.hand.delete(minionId);
	}

	drawCard():void {
		let cardDrawn: Card = this.deck.values[Math.floor(Math.random() * this.deck.size)];
		this.hand.set(cardDrawn.id, cardDrawn);
	}

	attack(minion: CardMinion, cible: Entity):void {
		minion.attack(cible);
	}

	useSpell(cardId: number):void {
		let spell: CardSpell = this.hand.get(cardId) as CardSpell;
		spell.play();
	}

	heroSpecial(target: Entity):void {
		this.hero.special(target);
	}

	getName():String {
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
		return this.manaMax
	}

	getMana(): number {
		return this.mana;
	}

}

export interface Entity {
	takeDamage(quantity: number): void;
	heal(quantity: number):void;
	boostHealth(quantity: number):void;
	getDamage(): number;
	isProvoking(): void;
	isDead(): boolean;
	die(): void;
}














export class Hero implements Entity {
	player: Player;
	health: number;
	healthMax: number;
	armor: number;
	taunt: boolean;

	constructor(player: Player) {
		this.player = player;
		this.health = ConstantesService.HEROMAXHEALTH;
		this.healthMax = ConstantesService.HEROMAXHEALTH;
		this.armor = 0;
		this.taunt = false;
	}

	special(e?: Entity): void {}

	takeDamage(quantity: number): void {
		this.armor = this.armor - quantity;
		if(this.armor < 0) { //Si on a cassé toute l'armure
			this.health = this.health - this.armor;
			this.armor = 0;
		}
	}

	heal(quantity: number):void {
		if(this.health + quantity <= this.healthMax) {
			this.health = this.health + quantity;
		} else {
			this.health = this.healthMax;
		}
	}

	boostHealth(quantity: number): void {
		this.health = this.health + quantity;
	}

	boostArmor(quantity: number): void {
		this.armor = this.armor + quantity;
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

	die(): void {
		if(this.isDead()) {
			//TODO : faire gagner l'adversaire
			console.log(this.player.getOpponent.name + " a gagné");
		}
	}

}











abstract class Card {
	id: number;
	name: String;
	owner: Player;
	manaCost: number;

	constructor(id: number, owner: Player, name: String, manaCost: number) {
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.manaCost = manaCost;
	}

	play(): void {};

	getId(): number {
		return this.id;
	}

	getName(): String {
		return this.name;
	}

	getOwner(): Player {
		return this.owner;
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

	capacities: Set<String>; //Taunt, charge, lifesteal...
	boosts: Map<String, number>;
	ready: boolean;
	provocation: boolean; //We will often nedd these, so we made them variables instead of having to search capacities everytime

	constructor(id: number, owner: Player, name: String, mana: number, damage: number, health: number, capacities: Set<String>, boosts: Map<String, number>) {
		super(id, owner, name, mana);

		this.damageBase = damage;
		this.damage = damage;
		this.damageBoosted = 0;

		this.healthMax = health;
		this.health = health;
		this.healthBoosted = 0;

		this.capacities = capacities;
		this.boosts = boosts;
		this.ready = capacities.has("charge");
		this.provocation = capacities.has("provocation");
	}

	play(): void {
		//Si le minion a été boosté alors qu'il était dans la main, on lui applique les boosts à ce moment
		this.boosts.forEach((value: number, key: String) => {
			this.getOwner().getBoard().forEach(element => {
				if(element.getId() != this.id) {
					switch(key) {
						case "damage":
							this.boostDamage(value);
							break;
						case "health":
							this.boostHealth(value);
							break;
					}
				}
			});
		});
	}

	attack(o: Entity): void {
		o.takeDamage(this.damage);
		this.takeDamage(o.getDamage());

		if(o.isDead()) {
			o.die();
		}
		if(this.health <= 0) {
			this.die();
		}
	}

	takeDamage(quantity: number): void {
		this.health = this.health - quantity;
	}

	heal(quantity: number): void {
		if(this.health + quantity < this.healthMax) {
			this.health = this.health + quantity;
		} else {
			this.health = this.healthMax;
		}
	}

	boostHealth(quantity: number): void {
		this.health = this.health + quantity;
		this.healthMax = this.healthMax + quantity;
		this.healthBoosted = this.healthBoosted + quantity;
	}

	boostDamage(quantity: number): void {
		this.damage = this.damage + quantity;
		this.damageBoosted = this.damageBoosted + quantity;
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
		return this.healthMax
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

	die(): void {
		if(this.isDead()) {
			this.getOwner().getBoard().delete(this.id);
		}
	}


}










export class CardSpell extends Card {
	singleEffects: Set<SingleTargetEffect>;
	multipleEffects: Set<MultipleTargetEffect>;
	globalEffects: Set<GlobalEffect>;

	constructor(id: number, owner: Player, name: String, mana: number, singleEffects: Set<SingleTargetEffect>, multipleEffects: Set<MultipleTargetEffect>, globalEffects: Set<GlobalEffect>) {
		super(id, owner, name, mana);
		this.singleEffects = singleEffects;
		this.multipleEffects = multipleEffects;
		this.globalEffects = globalEffects;
	}

	//TODO : gestion des cibles -> deux fonctions (une play(Entity) appelant les trois arrays et une play() n'appelant pas singleTarget) ?
	play(target?: Entity):void {
		if(target) {
			this.singleEffects.forEach(effect => {
				effect.play();
			})
		}
		this.multipleEffects.forEach(effect => {
			effect.play();
		});
		this.globalEffects.forEach(effect => {
			effect.play();
		})
	}


}