import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class ConstantesService {

  static HEROMAXHEALTH = 30;
  static NBPOSSIBLEHEROES = 3;

  constructor() { }

  getAleatoireHero(): String {
    const min = 1;
    const max = ConstantesService.NBPOSSIBLEHEROES;

    const nbChoosed = Math.floor((Math.random() * max) + min);
    let typeChoosed: String;

    switch (nbChoosed) {
      case 1:
        typeChoosed = 'paladin';
        break;
      case 2:
        typeChoosed = 'warrior';
        break;
      case 3:
        typeChoosed = 'mage';
        break;
      case 4:
        typeChoosed = 'casParDefaut';
        break;
    }
    return typeChoosed;
  }
}
