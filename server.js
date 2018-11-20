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
    constructor(socket) {
        this.socket = socket;
    }

    setName(name) {
        this.name = name;
    }
}

io.on('connection', socket => {
    socket.on('login', name => {
        socket.name = name;
        if(clients.has(name)) {
            clients.get(name).socket = socket;
        } else {
            clients.set(name, new SClient(socket));
        }
        socket.join('lobby', () => {
            socket.room = 'lobby';
            socket.broadcast.to(socket.room).emit('joinLobby', socket.id, name);
            socket.emit('playersAlreadyConnected', Object.values(io.sockets.sockets).filter(s => s.room == 'lobby' && s.id != socket.id).map(s => {
                return {id: s.id, name: s.name}}));
        });
    });
    
    socket.on('challengePlayer', id => {
        io.sockets.sockets[id].emit('newChallenge', socket.id);
    });
    
    socket.on('acceptChallenge', () => {
        io.sockets.sockets[id].emit('acceptChallenge', socket.id);
    });
    
    socket.on('refuseChallenge', () => {
        io.sockets.sockets[id].emit('refuseChallenge', socket.id);
    });
    
    socket.on('startGame', () => {
        let game = new Game();
        game.addPlayer(socket);
    });
    
    socket.on('disconnect', () => {
        socket.room ? console.log(`disconnect, room: ${socket.room}`) : {};
		if(socket.room == 'lobby') {
            socket.broadcast.to('lobby').emit('leaveLobby', socket.id);
        }
    });
});

http.listen(port, () => console.log(`Dbar on port ${port}`));