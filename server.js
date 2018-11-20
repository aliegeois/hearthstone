const express = require('express'),
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

io.on('connection', socket => {
	//let sess = socket.handshake.session;
	
    socket.on('setName', name => {
        socket.name = name;
		//sess.name = name;
		//sess.save();
        if(socket.rooms.hasOwnProperty('lobby')) {
            socket.broadcast.to(socket.room).emit('rename', socket.id, name);
        } else if(socket.rooms.hasOwnProperty('void')) {
            socket.leave('void', () => {
                socket.join('lobby', () => {
                    socket.room = 'lobby';
					//sess.room = 'lobby';
					//sess.save();
                    socket.broadcast.to(socket.room).emit('joinLobby', socket.id, name);
					socket.emit('playersAlreadyConnected', Object.values(io.sockets.sockets).filter(s => s.room == 'lobby' && s.id != socket.id).map(s => {
						return {id: s.id, name: s.name}}));
                });
            });
        }
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
        console.log(`disconnect, room: ${socket.room}`);
		if(socket.room == 'lobby') {
			io.to('lobby').emit('leaveLobby', socket.id);
		}
    });
	
	//console.log(`connexion, sess.room = ${sess.room}`);
	
	/*if(typeof sess.room == 'undefined') {
		console.log('new client');
		socket.join('void', () => {
			socket.room = 'void';
			sess.room = 'void';
			sess.save();
			console.log(`client in room: ${sess.room}`);
		});
	} else {
		console.log(`old client, room: ${sess.room}, name: ${sess.name}`);
		socket.join('lobby');
		socket.name = ess.name;
		socket.emit('name', sess.name);
    }*/
});

http.listen(port, () => console.log(`Dbar on port ${port}`));