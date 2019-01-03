import { Injectable } from '@angular/core';
import { CardSpell, CardMinion, Entity } from './app.component';

@Injectable({
  providedIn: 'root'
})

export abstract class EffectService {

    card: CardSpell;

    constructor(card: CardSpell) {
        this.card = card;
    }

    play(e?: Entity):void { //The parameters e is optionnal

    }
}


export abstract class GlobalEffect extends EffectService {

    constructor(card: CardSpell) {
        super(card);
    }

    play():void {

    }
}

export abstract class MultipleTargetEffect extends EffectService {
    ownBoard: boolean;
    opponentBoard: boolean;
    ownHero: boolean;
    opponentHero: boolean;

    constructor(card: CardSpell, ownBoard: boolean, opponentBoard: boolean, ownHero: boolean, opponentHero: boolean) {
        super(card);
        this.ownBoard = ownBoard;
        this.opponentBoard = opponentBoard;
        this.ownHero = ownHero;
        this.opponentHero = opponentHero;
    }

    play():void {

    }

    getOwnBoard(): boolean {
        return this.ownBoard;
    }

    getOpponentBoard(): boolean {
        return this.opponentBoard;
    }

    getOwnHero():boolean {
        return this.ownHero;
    }

    getOpponentHero(): boolean {
        return this.opponentHero;
    }
}

export abstract class SingleTargetEffect extends EffectService {

    constructor(card: CardSpell) {
        super(card);
    }

    play(e?: Entity): void {
        
    }
}

export class MultiTargetBuff extends MultipleTargetEffect {
    life: number;
    attack: number;

    constructor(card: CardSpell, ownBoard: boolean, opponentBoard: boolean, ownHero: boolean, opponentHero: boolean, life: number, attack: number) {
        super(card, ownBoard, opponentBoard, ownHero, opponentHero);
        this.life = life;
        this.attack = attack;
    }

    play(e?: Entity): void {
        if (e != null) { //TODO : éviter cette horreur (ts n'accepte pas les duplications de fonctions)
            throw new Error("Unsupported Operation Exception");
        }
       

        if(this.getOwnBoard()) {
            this.card.getOwner().getBoard().forEach(ownMinions => {
                ownMinions.boostHealth(this.life);
                ownMinions.boostDamage(this.attack);
            });
        }

        if(this.getOpponentBoard()) {
            this.card.getOwner().getOpponent().getBoard().forEach(opponentMinions => {
                opponentMinions.boostHealth(this.life);
                opponentMinions.boostDamage(this.attack);
            });
        }
    }
}


export class MultiTargetDamage extends MultipleTargetEffect {
    quantity: number;

    constructor(card: CardSpell, ownBoard: boolean, opponentBoard: boolean, ownHero: boolean, opponentHero: boolean, quantity: number) {
        super(card, ownBoard, opponentBoard, ownHero, opponentHero);
        this.quantity = quantity;
    }

    play(e?: Entity): void {
        if (e != null) { //TODO : éviter cette horreur (ts n'accepte pas les duplications de fonctions)
            throw new Error("Unsupported Operation Exception");
        }

        if (this.getOwnBoard()) {
            this.card.getOwner().getBoard().forEach(ownMinions => {
                ownMinions.takeDamage(this.quantity);
            });
        }
        if (this.getOpponentBoard) {
            this.card.getOwner().getOpponent().getBoard().forEach(opponentMinions => {
                opponentMinions.takeDamage(this.quantity);
            });
        }
        if (this.getOwnHero()) {
            this.card.getOwner().getHero().takeDamage(this.quantity);
        }
        if (this.getOpponentHero()) {
            this.card.getOwner().getOpponent().getHero().takeDamage(this.quantity);
        }
    }
}

export class MultiTargetHeal extends MultipleTargetEffect {
    amount: number;

    constructor(card: CardSpell, ownBoard: boolean, opponentBoard: boolean, ownHero: boolean, opponentHero: boolean, amount: number) {
        super(card, ownBoard, opponentBoard, ownHero, opponentHero);
        this.amount = amount;
    }

    play(e?: Entity): void {
        if (e != null) { //TODO : éviter cette horreur (ts n'accepte pas les duplications de fonctions)
            throw new Error("Unsupported Operation Exception");
        }

        if (this.getOwnBoard()) {
            this.card.getOwner().getBoard().forEach(ownMinions => {
                ownMinions.heal(this.amount);
            });
        }
        if (this.getOpponentBoard) {
            this.card.getOwner().getOpponent().getBoard().forEach(opponentMinions => {
                opponentMinions.heal(this.amount);
            });
        }
        if (this.getOwnHero()) {
            this.card.getOwner().getHero().heal(this.amount);
        }
        if (this.getOpponentHero()) {
            this.card.getOwner().getOpponent().getHero().heal(this.amount);
        }
    }
}


export class SingleTargetBuff extends SingleTargetEffect {
    life: number;
    attack: number;

    constructor(card: CardSpell, life: number, attack: number) {
        super(card);
        this.life = life;
        this.attack = attack;
    }

    play(e?: Entity): void {
        if (e == null) { //TODO : éviter cette horreur (ts n'accepte pas les duplications de fonctions)
            throw new Error("Unsupported Operation Exception");
        }
        e.boostHealth(this.life);
        //TODO : gérer ça
        e.boostDamage(this.attack);
    }
}

export class SingleTargetDamage extends SingleTargetEffect {
    damage: number;

    constructor(card: CardSpell, damage: number) {
        super(card);
        this.damage = damage;
    }

    play(e?: Entity): void {
        if (e == null) { //TODO : éviter cette horreur (ts n'accepte pas les duplications de fonctions)
            throw new Error("Unsupported Operation Exception");
        }
        e.takeDamage(this.damage);
    }
}

export class SingleTargetHeal extends SingleTargetEffect {
    amount: number;

    constructor(card: CardSpell, amount: number) {
        super(card);
        this.amount = amount;
    }

    play(e?: Entity): void {
        if (e == null) { //TODO : éviter cette horreur (ts n'accepte pas les duplications de fonctions)
            throw new Error("Unsupported Operation Exception");
        }
        e.heal(this.amount);
    }
}


export class Transform extends SingleTargetEffect {
    into: CardMinion;

    constructor(card: CardSpell, into: CardMinion) {
        super(card);
        this.into = into;
    }

    play(e: CardMinion):void {
        if (e == null) {
            throw new Error("Unsupported Operation Exception");
        }
        e = this.into;
    }
}

export class DrawRandom extends EffectService {
    cardNumber: number;
    
    constructor(card: CardSpell, cardNumber: number) {
        super(card);
        this.cardNumber = cardNumber;
    }

    play(e?: Entity): void {
        if (e != null) { //TODO : éviter cette horreur (ts n'accepte pas les duplications de fonctions)
            throw new Error("Unsupported Operation Exception");
        }
        for (let i:number = 0; i < this.cardNumber; i++) {
            this.card.getOwner().drawCard();
        }    
    }

    /*play(): void {
       for (let i:number = 0; i < this.cardNumber; i++) {
            this.card.getOwner().drawCard();
        } 
    }*/
}
