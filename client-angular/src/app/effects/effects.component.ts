import { Component, OnInit } from '@angular/core';
import { CardSpellComponent } from '../card/card.component';

@Component({
  selector: 'app-effects',
  templateUrl: './effects.component.html',
  styleUrls: ['./effects.component.scss']
})
export class EffectsComponent implements OnInit {

	card: CardSpellComponent; //Compile with warning (circular depedency), but it's fine

  constructor(card: CardSpellComponent) {
  	this.card = card;
  }

  ngOnInit() {
  }

}

export class DrawRandomComponent extends EffectsComponent implements OnInit {

	cardNumber: number;

	constructor(card: CardSpellComponent, cardNumber: number) {
		super(card);
		this.cardNumber = cardNumber;
	}

	play() {
		for(let i = 0 ; i < this.cardNumber ; i++) {
			this.card.getOwner().drawCard();
		}
	}


}

