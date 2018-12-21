import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{

	title: String;
	currentPage: String;

	constructor() {
		this.title = 'client-angular';
		this.currentPage = 'home';
	}

	ngOnInit() {
		//document.getElementById('button').addEventListener('click', this.change);
	}


	change() {
		switch(this.currentPage) {
			case 'home':
				this.currentPage = 'lobby'
				break;
			case 'lobby':
				this.currentPage = 'home'
				break;
		}
	}
}
