import { Injectable } from '@angular/core';
import { CardMinion, Entity, Card, Hero, CardSpell } from './app.component';

@Injectable({
	providedIn: 'root'
})


/* On ne peut pas avoir de référence vers Player à cause des dépendences circulaires non supportées, l'on doit donc mettre énormement de paramètres pour chaque cast() */
export abstract class EffectService {
	card: CardSpell;

	setCard(spell: CardSpell) {
		this.card = spell;
	}
	
	getCard(): CardSpell {
		return this.card;
	}
}


export abstract class GlobalEffect extends EffectService {
	
	constructor() {
		super();
	}
	
	abstract cast(player: Hero, playerHand: Map<string, Card>, playerBoard: Map<string, CardMinion>, opponent: Hero, opponentHand: Map<string, Card>, opponentBoard: Map<string, Card>): void;
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
	
	abstract cast(player: Hero, playerBoard: Map<string, CardMinion>, opponent: Hero, opponentBoard: Map<string, CardMinion>): void;
}

export abstract class SingleTargetEffect extends EffectService {
	
	constructor() {
		super();
	}
	
	abstract cast(e: Entity): void;
}

export class MultiTargetBuff extends MultipleTargetEffect {
	life: number;
	attack: number;
	armor: number;
	
	constructor(ownBoard: boolean, opponentBoard: boolean, ownHero: boolean, opponentHero: boolean, life: number, attack: number, armor: number) {
		super(ownBoard, opponentBoard, ownHero, opponentHero);
		this.life = life;
		this.attack = attack;
		this.armor = armor;
	}
	
	cast(player: Hero, playerBoard: Map<string, CardMinion>, opponent: Hero, opponentBoard: Map<string, CardMinion>) {
		if(this.ownHero) {
			player.boostHealth(this.life);
			player.boostArmor(this.armor);
		}

		if(this.opponentHero) {
			opponent.boostHealth(this.life);
			player.boostArmor(this.armor);
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
	
	cast(player: Hero, playerBoard: Map<string, CardMinion>, opponent: Hero, opponentBoard: Map<string, CardMinion>) {
		console.log('Flag a');
		if(this.ownHero) {
			console.log('Flag b');
			player.takeDamage(this.quantity);
		}
		if(this.opponentHero) {
			console.log('Flag c');
			opponent.takeDamage(this.quantity);
		}
		console.log('Flag d');
		if(this.ownBoard) {
			console.log('Flag e');
			playerBoard.forEach((value: CardMinion, key: string) => {
				value.takeDamage(this.quantity);
			})
		}
		
		if(this.opponentBoard) {
			opponentBoard.forEach((value: CardMinion, key: string) => {
				value.takeDamage(this.quantity);
			})
		}
		
	}  
}

export class MultiTargetHeal extends MultipleTargetEffect {
	amount: number;
	
	constructor(ownBoard: boolean, opponentBoard: boolean, ownHero: boolean, opponentHero: boolean, amount: number) {
		super(ownBoard, opponentBoard, ownHero, opponentHero);
		this.amount = amount;
	}
	
	cast(player: Hero, playerBoard: Map<string, CardMinion>, opponent: Hero, opponentBoard: Map<string, CardMinion>) {
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
		//e = this.into;
		e = new CardMinion((e as any as CardMinion).getId(), "Métamorphose_token", 0, 1, 1, new Set<String>(), new Map<string, number>(), e.getOwner());
	}
	
}

export class DrawRandom extends GlobalEffect {
	cardNumber: number;
	
	constructor(cardNumber: number) {
		super();
		this.cardNumber = cardNumber;
	}
	
	cast(player: Hero, playerHand: Map<string, Card>, playerBoard: Map<string, CardMinion>, opponent: Hero, opponentHand: Map<string, Card>, opponentBoard: Map<string, Card>) {
		// Nothing to do, the server sends a message to /draw
	}
	
}

export class SummonSpecific extends GlobalEffect {
	minionName: string;
	quantity: number;
	
	constructor(minionName: string, quantity: number) {
		super();
		this.minionName = minionName;
		this.quantity = quantity;
	}
	
	cast(player: Hero, playerHand: Map<string, Card>, playerBoard: Map<string, CardMinion>, opponent: Hero, opponentHand: Map<string, Card>, opponentBoard: Map<string, Card>) {
		// Nothing to do, the server sends a message to /summon
	}
	
}
