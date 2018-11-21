const express = require('express'),
	  //MemoryStore = express.session.MemoryStore,
	  /*session = require('express-session')({
		  secret: 'haha benis',
		  resave: true,
		  saveUninitialized: true
	  }),
	  sharedsession = require('express-socket.io-session'),
	  util = require('util'),*/
	  app = express(),
	  http = require('http').Server(app),
	  io = require('socket.io')(http),
	  port = 8080;

app.use(express.static('public'));
/*app.use(express.cookieParser());
app.use(express.session({
	secret: "haha, benis",
	store: new MemoryStore({
		reapInterval: 600000
	})
}));*/
//app.use(session);

app.get('/', (_, res) => res.sendFile(`${__dirname}/index.html`));
app.get('/lobby', (_, res) => res.sendFile(`${__dirname}/lobby/index.html`));
app.get('/game/:id', (_, res) => {
	//let gameId = req.params.id;
	res.sendFile(`${__dirname}/game/index.html`);
});
app.get('/bite', (_, res) => {
	res.json({haha: 'benis'});
});
/*app.get('/session', (req, res) => {
	res.json(req.session);
});*/

//io.use(sharedsession(session));

let clients = new Map();
class SClient {
	constructor(socket, name) {
		this.socket = socket;
		this.name = name;
		this.challenging = null;
		this.inGame = false;
	}
}

let globalGameId = 0;
let games = new Map();

io.on('connection', socket => {
	socket.on('login', name => {
		socket.name = name;
		if(clients.has(name)) {
			clients.get(name).socket = socket;
		} else {
			clients.set(name, new SClient(socket, name));
		}
		socket.join('lobby', () => {
			socket.room = 'lobby';
			socket.broadcast.to(socket.room).emit('new', name);
			socket.emit('playersAlreadyConnected', Object.values(io.sockets.sockets).filter(s => s.room == 'lobby' && s.id != socket.id).map(s => s.name));
		});
	});
	
	socket.on('challengePlayer', name => {
		let me = clients.get(socket.name); // Ce client
		let sclient = clients.get(name); // L'adversaire (potentiel)
		if(sclient.inGame) { // Si adversaire déjà en game
			socket.emit('alreadyInGame');
		} else {
			me.challenging = name;
			me.socket.emit('challenging', name); // Confirme la demande de challenge
			sclient.socket.emit('newChallenge', socket.name); // Envoie la demande de challenge
		}
	});
	
	socket.on('acceptChallenge', name => {
		
		io.sockets.sockets[id].emit('acceptChallenge', socket.id);
	});
	
	socket.on('refuseChallenge', () => {
		io.sockets.sockets[id].emit('refuseChallenge', socket.id);
	});
	
	socket.on('startGame', (otherName) => {
		let game = new Game();
		let p1 = clients.get(socket.name),
			p2 = clients.get(otherName);
		p1.inGame = true;
		p2.inGame = true;
		game.addPlayer(clients.get(socket.name));
		game.addPlayer(clients.get(otherName));
		games.set(globalGameId++, game);
		game.start();
	});
	
	socket.on('disconnect', () => {
		//socket.room ? console.log(`disconnect, room: ${socket.room}`) : {};
		//if(socket.room == 'lobby') {
		//    socket.broadcast.to('lobby').emit('leaveLobby', socket.id);
		//}
	});
});

http.listen(port, () => console.log(`Dbar on port ${port}`));