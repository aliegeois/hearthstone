import { Component, OnInit, Injectable } from '@angular/core';
import { Player, CardMinion, Card, CardSpell, AppComponent, Entity } from '../app.component';
import { initDomAdapter } from '@angular/platform-browser/src/browser';
import { SingleTargetEffect, MultipleTargetEffect, GlobalEffect, Transform, SingleTargetDamage } from '../effect.service';

@Component({
	selector: 'app-game',
	templateUrl: './game.component.html',
	styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {
	
	joueur: Player;
	opponent: Player;
	
	playing: Player;
	turn: number;
	
	secretMode: boolean; // False : normal heroes ; True : alternativ heroes (Pascal, Sunye, Chantal)
	gameId: string;
	infoLog: string;
	
	selectedHand: Card;
	selectedAttacking: CardMinion;
	selectedHeroPower: boolean;
	
	
	constructor() {
		
		this.secretMode = false;
		this.gameId = AppComponent.gameId;
		this.infoLog = "";
		
		this.selectedHand = null;
		this.selectedAttacking = null;
		this.selectedHeroPower = false;
		
		this.init();
		
		this.playing = this.getPlayer(AppComponent.playing);
		AppComponent.addListener(this);
		console.log("Envoi de la confirmation de game");
		AppComponent.stompClient.send(`/app/game/${this.gameId}/confirmStart`, {});
		
		this.playing.manaMax = this.playing.manaMax + 1;
		this.playing.mana = this.playing.manaMax;
		
		
	}
	
	ngOnInit() {
		
		let formList = document.getElementsByTagName('form');
		
		for(let form of <any>formList) {
			form.addEventListener('submit', e => {
				e.preventDefault();
			});
		}
		
		
		document.addEventListener('keyup', (event) => {
			const keyName = event.key;
			console.log('Escape pressed');
			// As the user releases the Ctrl key, the key is no longer active,
			// so event.ctrlKey is false.
			if (keyName === 'Escape') {
				console.log('Escape pressed');
				this.resetSelected();  
			}
		}, false);
		
	}
	
	onConnect() {
		console.log("[GameComponent.onConnect] SessionId : " + AppComponent.sessionId);
		
		
		// On reçoit un summonMinion
		AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/summonMinionFromHand`, data => {
			console.log(`event: summonMinion, data: ${data.body}`);
			let msg = JSON.parse(data.body);
			
			let concernedPlayer : Player = this.getPlayer(msg.playerName);
			
			// On retire le minion de la main et on l'ajoute au niveau du board
			concernedPlayer.summon(msg.cardId);
		});
		
		// On reçoit un summonMinion
		AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/summonMinionGlobal`, data => {
			console.log(`event: summonMinion, data: ${data.body}`);
			let msg = JSON.parse(data.body);
			
			let concernedPlayer : Player = this.getPlayer(msg.playerName);

			concernedPlayer.draw(msg.cardName, "minion", msg.cardId, "board");
			/*
			// On ajoute le minion au board
			fetch('http://localhost:8080/cards/getMinion?name=' + msg.cardName)
			
			.then( response => {          
				return response.json();
			})
			.then( response => {
				let capacities: Set<String> = new Set<String>();
				if(response.taunt) {
					capacities.add("taunt");
				}
				if(response.lifesteal) {
					capacities.add("lifesteal");
				}
				if(response.charge) {
					capacities.add("charge");
				}
				
				let boosts: Map<string, number> = new Map<string, number>();
				boosts.set("life", response.boostHealth as number);
				boosts.set("damage", response.boostDamage as number);
				
				let card: CardMinion = new CardMinion(msg.cardId, response.name, response.manaCost, response.damageBase, response.healthMax, capacities, boosts, concernedPlayer);
				concernedPlayer.board.set(msg.cardId, card);
			});*/
		});
		
		
		
		// On reçoit une attack
		AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/attack`, data => {
			console.log(`event: attack, data: ${data.body}`);
			let msg = JSON.parse(data.body);
			
			let concernedPlayer : Player = this.getPlayer(msg.playerName);
			
			// On fait s'attaquer les deux minions
			concernedPlayer.attack(msg.cardId, msg.targetId, msg.hero);
		});
		
		// On reçoit un cast d'un targetedSpell
		AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/castTargetedSpell`, data => {
			console.log(`event: castTargetedSpell, data: ${data.body}`);
			let msg = JSON.parse(data.body);
			
			let concernedPlayer : Player = this.getPlayer(msg.playerName);
			
			// On fait cast le spell par le joueur concerné
			concernedPlayer.castTargetedSpell(msg.cardId, msg.targetId, msg.own, msg.hero);
		});
		
		// On reçoit un cast d'un untargetedSpell
		AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/castUntargetedSpell`, data => {
			console.log(`event: castUntargetedSpell, data: ${data.body}`);
			let msg = JSON.parse(data.body);
			
			let concernedPlayer : Player = this.getPlayer(msg.playerName);
			
			// On fait cast le spell par le joueur concerné
			concernedPlayer.castUntargetedSpell(msg.cardId);
		});
		
		// On recoit un specialHero targeted
		AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/targetedSpecial`, data => {
			console.log(`event: targetedSpecial, data: ${data.body}`);
			let msg = JSON.parse(data.body);
			
			let concernedPlayer : Player = this.getPlayer(msg.playerName);
			
			// On fait cast le special par le joueur concerné
			concernedPlayer.castTargetedSpecial(msg.targetId, msg.own, msg.hero);
		});
		
		// On recoit un specialHero untargeted
		AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/untargetedSpecial`, data => {
			console.log(`event: untargetedSpecial, data: ${data.body}`);
			let msg = JSON.parse(data.body);
			
			let concernedPlayer : Player = this.getPlayer(msg.playerName);
			
			// On fait cast le special par le joueur concerné
			concernedPlayer.castUntargetedSpecial();
		});
		
		// On reçoit un timeout
		AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/timeout`, data => {
			console.log(`event: timeout, data: ${data.body}`);
			let msg = JSON.parse(data.body);
			
			let concernedPlayer : Player = this.getPlayer(msg.playerName);
			
			if(concernedPlayer == this.joueur) {
				this.infoLog = "Votre tour est terminé";
			} else {
				this.infoLog = "Le tour adverse est terminé";
			}
			
			AppComponent.stompClient.send(`/app/game/${this.gameId}/endTurn`, {}, JSON.stringify({playerName: concernedPlayer.name}));
		});
		
		
		// On recoit un nextTurn
		AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/nextTurn`, data => {
			console.log(`event: nextTurn, data: ${data.body}`);
			let msg = JSON.parse(data.body);
			
			//concernedPlayer est le nom du joueur dont le tour vient de commencer
			let concernedPlayer : Player = this.getPlayer(msg.playerName).opponent;
			
			
			// On démarre le tour du joueur concerné
			concernedPlayer.beginTurn(msg.cardName, msg.cardId, msg.cardType);
			this.playing = concernedPlayer;
			this.resetSelected();
		});
		
		// On reçoit la victoire d'un joueur
		AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/victory`, data => {
			console.log(`event: win, data: ${data.body}`);
			let msg = JSON.parse(data.body);
			
			let concernedPlayer : Player = this.getPlayer(msg.playerName);
			
			if(concernedPlayer == this.joueur) {
				this.infoLog = "Vous avez gagné !";
			} else {
				this.infoLog = "Vous avez perdu :/";
			}
		});
		
		
		AppComponent.stompClient.subscribe(`/topic/game/${this.gameId}/drawCard`, data => {
			console.log(`event: drawCard, data: ${data.body}`);
			let msg = JSON.parse(data.body);
			
			let concernedPlayer : Player = this.getPlayer(msg.playerName);
			concernedPlayer.draw(msg.cardName, msg.cardType, msg.cardId, "hand");
		});
	}
	
	
	
	
	init() {
		console.log('Initialisation');
		this.joueur = new Player(AppComponent.joueurName, AppComponent.joueurHero);
		this.opponent = new Player(AppComponent.opponentName, AppComponent.opponentHero);
		this.joueur.setOpponent(this.opponent);
		this.opponent.setOpponent(this.joueur);
		
		this.loadDecks();
		
		/*let cardTest1: CardMinion = new CardMinion(1,"Recrue de la main d'argent", 1, 1, 1, new Set<String>(), new Map<String, number>(), this.joueur);
		let cardTest2: CardMinion = new CardMinion(2, "Champion Frisselame", 3, 4, 2, new Set<String>().add("charge"), new Map<String, number>(), this.joueur);
		let cardTest3: CardSpell = new CardSpell(3, "Métamorphose", 4, new Set<SingleTargetEffect>().add(new Transform(cardTest1)), new Set<MultipleTargetEffect>(), new Set<GlobalEffect>(), this.joueur);
		let cardTest4: CardMinion = new CardMinion(4, "Chevaucheur de loup", 1, 1, 2, new Set<String>().add("taunt"), new Map<String, number>(), this.joueur)
		let cardTest5: CardMinion = new CardMinion(5,"Image Miroir_token", 1, 1, 1, new Set<String>(), new Map<String, number>(), this.joueur);
		let cardTest6: CardMinion = new CardMinion(6,"Sanglier Brocheroc", 1, 1, 1, new Set<String>(), new Map<String, number>(), this.joueur);
		let cardTest7: CardMinion = new CardMinion(7,"Chef de raid", 1, 1, 1, new Set<String>(), new Map<String, number>(), this.joueur);
		let cardTest8: CardMinion = new CardMinion(8,"Yeti noroit", 1, 1, 1, new Set<String>(), new Map<String, number>(), this.joueur);
		let cardTest9: CardSpell = new CardSpell(9, "Image Miroir", 4, new Set<SingleTargetEffect>(), new Set<MultipleTargetEffect>(), new Set<GlobalEffect>(), this.joueur);
		let cardTest10: CardSpell = new CardSpell(10, "Fireball", 4, new Set<SingleTargetEffect>().add(new SingleTargetDamage(6)), new Set<MultipleTargetEffect>(), new Set<GlobalEffect>(), this.joueur);
		
		this.joueur.hand.set(0, cardTest1);
		this.joueur.hand.set(1, cardTest2);
		this.joueur.hand.set(2, cardTest3);
		this.joueur.hand.set(9, cardTest9);
		this.joueur.hand.set(10, cardTest10);
		
		this.joueur.board.set(0, cardTest4);
		this.joueur.board.set(1, cardTest6);
		
		this.opponent.board.set(0, cardTest5);
		this.opponent.board.set(1, cardTest7);
		
		this.opponent.hand.set(0, cardTest8);*/
		
		
	}
	
	
	
	
	
	
	
	loadDecks() {
		
		//On charge toutes les cartes neutre :
		fetch('http://localhost:8080/cards/deckMinion?deck=shared')
		.then( response => {
			return response.json();
		})
		.then( response => {
			for(let i = 0 ; i < response.length ; i++) {
				
				let capacities = new Set();
				if(response.charge) {
					capacities.add("charge");
				}
				if(response.lifesteal) {
					capacities.add("lifesteal");
				}
				if(response.taunt) {
					capacities.add("taunt");
				}
				
				let boosts = new Map<string, number>();
				boosts.set("health", response.boostHealth);
				boosts.set("damage", response.boostDamage);
				
				
				let cardJoueur: CardMinion = new CardMinion(i as unknown as string, response.name, response.manaCost, response.damageBase, response.healthMax, capacities, boosts, this.joueur)
				this.joueur.deck.add(cardJoueur);
			}
		});
		
		
		
		//On charge tout les minions spécifiques au héros :
		fetch('http://localhost:8080/cards/deckMinion?deck=' + AppComponent.joueurHero)
		.then( response => {
			return response.json();
		})
		.then( response => {
			for(let i = 0 ; i < response.length ; i++) {
				
				let capacities = new Set();
				if(response.charge) {
					capacities.add("charge");
				}
				if(response.lifesteal) {
					capacities.add("lifesteal");
				}
				if(response.taunt) {
					capacities.add("taunt");
				}
				
				let boosts = new Map<string, number>();
				boosts.set("health", response.boostHealth);
				boosts.set("damage", response.boostDamage);
				
				
				let cardJoueur: CardMinion = new CardMinion(i as unknown as string, response.name, response.manaCost, response.damageBase, response.healthMax, capacities, boosts, this.joueur)
				this.joueur.deck.add(cardJoueur);
			}
		});
		
		//On charge tout les spells spécifique au héros :
		fetch('http://localhost:8080/cards/deckSpell?deck=' + AppComponent.joueurHero)
		.then( response => {
			return response.json();
		})
		.then( response => {
			for(let i = 0 ; i < response.length ; i++) {
				
				let cardJoueur: CardSpell = new CardSpell(i as unknown as string, response.name, response.manaCost, response.ste, response.mte, response.ge, this.joueur)
				this.joueur.deck.add(cardJoueur);
			}
		});
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	selectCardHand(card: Card): void {
		
		this.resetSelected();
		
		this.selectedHand = card;
		
		console.log("Select cardHand " + card.name);
		
		// S'il ne possède pas de targetable spell, on le lance direct
		if(!this.selectedHand.hasTargetedSpell()) {
			card.playReceived(this.gameId);
			
			// On remet tout à zéro
			this.selectedHand = null;
		} else {
			
		}
		
	}
	
	
	selectCardPlayerBoard(card: CardMinion): void {
		
		this.resetSelected();
		
		this.selectedAttacking = card;
		
		console.log("Select cardBoard " + card.name);
		
	}
	
	
	selectCardOpponentBoard(card: CardMinion): void {
		console.log("Select cardOpponentBoard " + card.name);
		
		// Si on avait déjà choisi une carte sur le board, on lance une attaque sur card
		if(this.selectedAttacking != null && this.selectedAttacking.canAttack && (!this.tauntInTheWay() || card.isProvoking())) {
			console.log('Envoi de attack sur ' + card.name);
			AppComponent.stompClient.send(`/app/game/${this.gameId}/attack`, {}, JSON.stringify({hero: "false", cardId: this.selectedAttacking.id, targetId: card.id}));
			this.selectedAttacking = null;
		}
		// Sinon, si on avait déjà choisit une carte dans la main, on la joue avec pour cible card
		else if(this.selectedHand != null) {
			this.selectedHand.playReceived(this.gameId, card);
			this.selectedHand = null;
		}
		// Sinon, si on avait déjà cliquer sur le héro power, on le joue avec pour cible card
		else if(this.selectedHeroPower) {
			this.joueur.specialReceived(this.gameId, card);
			this.selectedHeroPower = false;
		}
	}
	
	
	selectOpponent(): void {
		console.log("Click opponent");
		// Si on avait déjà choisi une carte sur le board, on lance une attaque sur card
		if(this.selectedAttacking != null && this.selectedAttacking.canAttack) {
			console.log('Envoi de attack sur le hero');
			AppComponent.stompClient.send(`/app/game/${this.gameId}/attack`, {}, JSON.stringify({hero: "true", cardId: this.selectedAttacking.id, targetId: null}));
			this.selectedAttacking = null;
		}
		// Sinon, si on avait déjà choisit une carte dans la main, on la joue avec pour cible le héros adverse
		else if(this.selectedHand != null) {
			this.selectedHand.playReceived(this.gameId, this.opponent.hero);
		}
		// Sinon, si on avait déjà cliquer sur le héro power, on le joue avec pour cible card
		else if(this.selectedHeroPower) {
			this.joueur.specialReceived(this.gameId, this.opponent.hero);
			this.selectedHeroPower = false;
		}
	}
	
	selectHero(): void {
		console.log("Click hero");
		// Si on avait déjà choisit une carte dans la main, on la joue avec pour cible notre héros
		if(this.selectedHand != null) {
			this.selectedHand.playReceived(this.gameId, this.joueur.hero);
		}
	}
	
	
	special(): void {
		if(this.joueur.hero.isSpecialUsable() && this.joueur == this.playing) {
			if(AppComponent.joueurHero == "mage") {
				this.selectedHeroPower = true;
			} else {
				this.joueur.specialReceived(this.gameId);
			}
		}
	}
	
	
	
	
	
	tauntInTheWay(): boolean {
		let tauntInTheWay = false;
		
		this.opponent.board.forEach((value: CardMinion, key: string) => {
			tauntInTheWay = tauntInTheWay || value.isProvoking();
		});
		
		return tauntInTheWay;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	endTurn(): void {
		console.log('Envoi de fin du tour');
		AppComponent.stompClient.send(`/app/game/${this.gameId}/endTurn`, {});
	}
	
	
	
	
	getImgUrl(card: Card) {
		let name = card.name;
		name = name.replace(/ /g,'') //On vire les espaces
		name = name.replace(/'/g, ''); //On vire les '
		name = name.replace(/-/g, ''); //On vire les -
		let url = '../../assets/images/cards/' + name.toLocaleLowerCase() + '.png';
		return url;
	}
	
	
	
	
	
	
	
	
	
	
	
	enter_secretMode() {
		switch(this.secretMode) {
			case true:
			this.joueur.hero.normalMode();
			this.opponent.hero.normalMode();
			this.secretMode = false;
			break;
			case false:
			this.joueur.hero.alternativMode();
			this.opponent.hero.alternativMode();
			this.secretMode = true;
		}
	}
	
	getPlayer(name: string): Player { //Return the adequate player
		if(this.joueur.name == name) {
			return this.joueur;
		} else if(this.opponent.name == name) {
			return this.opponent;
		} else {
			console.log("Player not found");
			throw new ReferenceError("Player not found");
		}
	}
	
	resetSelected() {
		this.selectedAttacking = null;
		this.selectedHand = null;
		this.selectedHeroPower = false;
	}
}
