import { Component, OnInit } from '@angular/core';
import { SingleTargetEffect, MultipleTargetEffect, GlobalEffect } from './effect.service';
// import { HeroMage, HeroPaladin, HeroWarrior } from './heroes.service';
import { ConstantesService } from './constantes.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})


export class AppComponent implements OnInit {

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
        switch (this.currentPage) {
            case 'home':
                this.currentPage = 'lobby';
                break;
            case 'lobby':
                this.currentPage = 'home';
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














export class Hero implements Entity {
    health: number;
    healthMax: number;
    armor: number;
    taunt: boolean;

    constructor() {
        this.health = ConstantesService.HEROMAXHEALTH;
        this.healthMax = ConstantesService.HEROMAXHEALTH;
        this.armor = 0;
        this.taunt = false;
    }




    isProvoking(): boolean {
        return this.taunt;
    }

    isDead(): boolean {
        return (this.health <= 0);
    }


}











abstract class Card {
    id: number;
    name: String;
    owner: Player;
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
      super();
    }

}

export class HeroPaladin extends Hero {

    constructor() {
      super();
    }

}

export class HeroWarrior extends Hero {

    constructor() {
      super();
    }

}
