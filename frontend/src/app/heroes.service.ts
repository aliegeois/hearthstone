import { Injectable } from '@angular/core';
import { Hero, Player, Entity, CardMinion } from './app.component';

@Injectable({
  providedIn: 'root'
})

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
