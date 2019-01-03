import { Injectable } from '@angular/core';
import { Hero, Player, Entity, CardMinion } from './app.component';

@Injectable({
  providedIn: 'root'
})

export class HeroMage extends Hero {

  constructor(player: Player) {
    super(player);
  }

  special(e: Entity) {
    e.takeDamage(2);
  }
}

export class HeroPaladin extends Hero {

  constructor(player: Player) {
    super(player);
  }

  special() {
    let idCard: number = 0 //Temp valeur, TODO : déterminer l'id suivante à affecter
    let cap: Set<String> = new Set<String>();
    let boosts: Map<String, number> = new Map<String, number>();
    let card: CardMinion = new CardMinion(idCard, this.player, "SilverHand recruit", 1, 1, 1, cap, boosts);
    this.player.getHand().set(idCard, card); //Put the card in player hand...
    this.player.playMinion(idCard); //... and play it immediatly
  }
}

export class HeroWarrior extends Hero {

  constructor(player: Player) {
    super(player);
  }

  special() {
    this.boostArmor(2);
  }
}
