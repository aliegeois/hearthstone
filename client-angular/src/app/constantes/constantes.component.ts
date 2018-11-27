import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-constantes',
  templateUrl: './constantes.component.html',
  styleUrls: ['./constantes.component.scss']
})
export class ConstantesComponent implements OnInit {

	heroMaxHealth: number = 30;
  constructor() { }

  ngOnInit() {
  }

}
