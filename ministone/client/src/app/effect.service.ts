import { Injectable } from '@angular/core';
import { CardSpell, CardMinion, Entity } from './app.component';

@Injectable({
  providedIn: 'root'
})


export abstract class EffectService {

    constructor() { }


}


export abstract class GlobalEffect extends EffectService {

    constructor() {
        super();
     }

}

export abstract class MultipleTargetEffect extends EffectService {
    ownBoard: boolean;
    opponentBoard: boolean;
    ownHero: boolean;
    opponentHero: boolean;

    constructor(ownBoard: boolean, opponentBoard: boolean, ownHero: boolean, opponentHero: boolean) {
        super();
        this.ownBoard = ownBoard;
        this.opponentBoard = opponentBoard;
        this.ownHero = ownHero;
        this.opponentHero = opponentHero;
    }

    getOwnBoard(): boolean {
        return this.ownBoard;
    }

    getOpponentBoard(): boolean {
        return this.opponentBoard;
    }

    getOwnHero(): boolean {
        return this.ownHero;
    }

    getOpponentHero(): boolean {
        return this.opponentHero;
    }
}

export abstract class SingleTargetEffect extends EffectService {

    constructor() {
        super();
    }

}

export class MultiTargetBuff extends MultipleTargetEffect {
    life: number;
    attack: number;

    constructor(ownBoard: boolean, opponentBoard: boolean, ownHero: boolean, opponentHero: boolean, life: number, attack: number) {
        super(ownBoard, opponentBoard, ownHero, opponentHero);
        this.life = life;
        this.attack = attack;
    }

}


export class MultiTargetDamage extends MultipleTargetEffect {
    quantity: number;

    constructor(ownBoard: boolean, opponentBoard: boolean, ownHero: boolean, opponentHero: boolean, quantity: number) {
        super(ownBoard, opponentBoard, ownHero, opponentHero);
        this.quantity = quantity;
    }

}

export class MultiTargetHeal extends MultipleTargetEffect {
    amount: number;

    constructor(ownBoard: boolean, opponentBoard: boolean, ownHero: boolean, opponentHero: boolean, amount: number) {
        super(ownBoard, opponentBoard, ownHero, opponentHero);
        this.amount = amount;
    }

}


export class SingleTargetDamageBuff extends SingleTargetEffect {
    attack: number;

    constructor(attack: number) {
        super();
        this.attack = attack;
    }

}

export class SingleTargetLifeBuff extends SingleTargetEffect {
    life: number;

    constructor(life: number) {
        super();
        this.life = life;
    }

}


export class SingleTargetDamage extends SingleTargetEffect {
    damage: number;

    constructor(damage: number) {
        super();
        this.damage = damage;
    }

}

export class SingleTargetHeal extends SingleTargetEffect {
    amount: number;

    constructor(amount: number) {
        super();
        this.amount = amount;
    }

}


export class Transform extends SingleTargetEffect {
    into: CardMinion;

    constructor(into: CardMinion) {
        super();
        this.into = into;
    }

}

export class DrawRandom extends EffectService {
    cardNumber: number;

    constructor(cardNumber: number) {
        super();
        this.cardNumber = cardNumber;
    }

}
