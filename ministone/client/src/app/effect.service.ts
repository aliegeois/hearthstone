import { Injectable } from '@angular/core';
import { CardSpell, CardMinion, Entity, Card, Hero } from './app.component';

@Injectable({
  providedIn: 'root'
})


/* On ne peut pas avoir de référence vers Player à cause des dépendences circulaires non supportées, l'on doit donc mettre énormement de paramètres pour chaque cast() */
export abstract class EffectService {

    constructor() { }


}


export abstract class GlobalEffect extends EffectService {

    constructor() {
        super();
     }

     cast(player: Hero, playerDeck: Set<Card>, playerHand: Map<number, Card>, playerBoard: Map<number, CardMinion>, opponent: Hero, opponentDeck: Set<Card>, opponentHand: Map<number, Card>, opponentBoard: Map<number, Card>) {}
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

    cast(player: Hero, playerBoard: Map<number, CardMinion>, opponent: Hero, opponentBoard: Map<number, CardMinion>) {}
}

export abstract class SingleTargetEffect extends EffectService {

    constructor() {
        super();
    }

    cast(e: Entity) {}
}

export class MultiTargetBuff extends MultipleTargetEffect {
    life: number;
    attack: number;

    constructor(ownBoard: boolean, opponentBoard: boolean, ownHero: boolean, opponentHero: boolean, life: number, attack: number) {
        super(ownBoard, opponentBoard, ownHero, opponentHero);
        this.life = life;
        this.attack = attack;
    }

    cast(player: Hero, playerBoard: Map<number, CardMinion>, opponent: Hero, opponentBoard: Map<number, CardMinion>) {
        if(this.ownHero) {
            player.boostHealth(this.life);
        }
        if(this.opponentHero) {
            opponent.boostHealth(this.life);
        }

        if(this.ownBoard) {
            playerBoard.forEach( card => {
                card.boostHealth(this.life);
                card.boostDamage(this.attack);
            });
        }

        if(this.opponentBoard) {
            opponentBoard.forEach( card => {
                card.boostHealth(this.life);
                card.boostDamage(this.attack);
            });
        }
        
    }
}


export class MultiTargetDamage extends MultipleTargetEffect {
    quantity: number;

    constructor(ownBoard: boolean, opponentBoard: boolean, ownHero: boolean, opponentHero: boolean, quantity: number) {
        super(ownBoard, opponentBoard, ownHero, opponentHero);
        this.quantity = quantity;
    }

    cast(player: Hero, playerBoard: Map<number, CardMinion>, opponent: Hero, opponentBoard: Map<number, CardMinion>) {
        if(this.ownHero) {
            player.takeDamage(this.quantity);
        }
        if(this.opponentHero) {
            opponent.takeDamage(this.quantity);
        }

        if(this.ownBoard) {
            playerBoard.forEach( card => {
                card.takeDamage(this.quantity);
            });
        }

        if(this.opponentBoard) {
            opponentBoard.forEach( card => {
                card.takeDamage(this.quantity);
            });
        }
        
    }  
}

export class MultiTargetHeal extends MultipleTargetEffect {
    amount: number;

    constructor(ownBoard: boolean, opponentBoard: boolean, ownHero: boolean, opponentHero: boolean, amount: number) {
        super(ownBoard, opponentBoard, ownHero, opponentHero);
        this.amount = amount;
    }

    cast(player: Hero, playerBoard: Map<number, CardMinion>, opponent: Hero, opponentBoard: Map<number, CardMinion>) {
        if(this.ownHero) {
            player.heal(this.amount);
        }
        if(this.opponentHero) {
            opponent.heal(this.amount);
        }

        if(this.ownBoard) {
            playerBoard.forEach( card => {
                card.heal(this.amount);
            });
        }

        if(this.opponentBoard) {
            opponentBoard.forEach( card => {
                card.heal(this.amount);
            });
        }
        
    }  
}


export class SingleTargetDamageBuff extends SingleTargetEffect {
    attack: number;

    constructor(attack: number) {
        super();
        this.attack = attack;
    }

    cast(e: CardMinion) {
        e.boostDamage(this.attack);
    }
}

export class SingleTargetLifeBuff extends SingleTargetEffect {
    life: number;

    constructor(life: number) {
        super();
        this.life = life;
    }

    cast(e: Entity) {
        e.boostHealth(this.life);
    }
}


export class SingleTargetDamage extends SingleTargetEffect {
    damage: number;

    constructor(damage: number) {
        super();
        this.damage = damage;
    }

    cast(e: Entity) {
        e.takeDamage(this.damage);
    }
}

export class SingleTargetHeal extends SingleTargetEffect {
    amount: number;

    constructor(amount: number) {
        super();
        this.amount = amount;
    }

    cast(e: Entity) {
        e.heal(this.amount);
    }
}


export class Transform extends SingleTargetEffect {
    into: CardMinion;

    constructor(into: CardMinion) {
        super();
        this.into = into;
    }

    cast(e: Entity) {
        e = this.into;
    }

}

export class DrawRandom extends GlobalEffect {
    cardNumber: number;

    constructor(cardNumber: number) {
        super();
        this.cardNumber = cardNumber;
    }

    cast(player: Hero, playerDeck: Set<Card>, playerHand: Map<number, Card>, playerBoard: Map<number, CardMinion>, opponent: Hero, opponentDeck: Set<Card>, opponentHand: Map<number, Card>, opponentBoard: Map<number, Card>) {
        for(let i = 0 ; i <= this.cardNumber ; i++) {
            // TODO : appeler la méthode AppComponent.stomp.client.send pour piocher une carte
        }
    }

}
