class Game {
	constructor() {
		this.players = {};
	}
	
	addPlayer(socket) {
		this.players[socket.id] = new Player(socket);
		let nbPlayers = Object.keys(this.players).length;
		if(nbPlayers == 2)
			this.start();
		else if(nbPlayers > 2) {
			// Erreur
		}
	}
	
	disconnect(playerId) {
		// Déconnection d'un joueur
	}
	
	setHero(playerId, heroId) {
		this.players[playerId].setHero(heroId);
	}
}

class Player {
	constructor(s) {
		this.socket = s;
	}
	
	setHero(heroName) {
		
	}
}