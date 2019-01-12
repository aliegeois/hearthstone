import { Component, OnInit, Injectable } from '@angular/core';
//import { Player } from '../app.component';
import { AppComponent } from '../app.component';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {

 
	
	
  constructor() {

  }

  ngOnInit() {

    let formList = document.getElementsByTagName('form');
	
    for(let form of <any>formList) {
      form.addEventListener('submit', e => {
        e.preventDefault();
      });
    }
    

  }

  onConnect() {
    
  }




  

}
