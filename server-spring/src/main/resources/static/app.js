var stompClient = null, name, sessionId, nodeUsers = new Map();

function setConnected(connected) {
	document.getElementById('connected-content').style.setProperty('display', connected ? 'block' : 'none');
	document.getElementById('players').innerHTML = '';
}

function connect() {
	let socket = new SockJS('/gs-guide-websocket');
	stompClient = Stomp.over(socket);

	stompClient.connect({}, frame => {
		setConnected(true);
		console.log('Connected: ' + frame);
		sessionId = socket._transport.url.split('/').slice(-2, -1)[0];
		console.log('SessionId : ' + sessionId);

		// Confirmation du nom
		stompClient.subscribe(`/topic/lobby/${sessionId}/confirmName`, data => {
			console.log(`event: confirmName, data: ${data.body}`);
			name = JSON.parse(data.body).name;
			for(let el of document.getElementsByClassName('name'))
				el.innerHTML = name;
		});
		// Récupère les clients déjà connecté
		stompClient.subscribe(`/topic/lobby/${sessionId}/usersBefore`, data => {
			console.log(`event: usersBefore, data: ${data.body}`);
			let users = JSON.parse(data.body);
			for(let user of users)
				addPlayer(user.name);
		});
		// Nouvel utilisateur se connecte
		stompClient.subscribe(`/topic/lobby/${sessionId}/userJoined`, data => {
			console.log(`event: userJoined, data: ${data.body}`);
			let user = JSON.parse(data.body);
			addPlayer(user.name);
		});
		// Un utilisateur se déconnecte
		stompClient.subscribe(`/topic/lobby/${sessionId}/userLeaved`, data => {
			console.log(`event: userLeaved, data: ${data.body}`);
			let user = JSON.parse(data.body);
			removePlayer(user.name);
		});
		// 
		stompClient.subscribe(`/topic/lobby/${sessionId}/matchDeclined`, data => {
			console.log(`event: matchDeclined, data: ${data.body}`);
			data = JSON.parse(data.body);
			let td = document.getElementById('buttons-' + data.id);
			nodeUsers.get(data.opponent).firstElementChild.colSpan = 2;
			td.parentElement.removeChild(td);
		});
		// Adversaire trouvé
		stompClient.subscribe(`/topic/lobby/${sessionId}/matchFound`, data => {
			console.log(`event: matchFound, data: ${data.body}`);
			data = JSON.parse(data.body);
			matchFound(data.id, data.opponent);
		});
		// Partie lancée
		stompClient.subscribe(`/topic/lobby/${sessionId}/startGame`, data => {
			console.log(`event: startGame, data: ${data.body}`);
			data = JSON.parse(data.body);
			window.location.replace('/game.html');
			// TODO lancer la partie, genre redirection vers /game
		});
		// Erreur quelconque
		stompClient.subscribe(`/topic/lobby/${sessionId}/error`, data => {
			console.log(`event: error, data: ${data.body}`);
			let message = JSON.parse(data.body).message;
			console.error(message);
		});
	});
}

function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log('Disconnected');
}

function matchFound(gameId, opponent) {
	let opponentRow = nodeUsers.get(opponent);
	let tdButtons = document.createElement('td'),
	    buttonAccept = document.createElement('button'),
		buttonDecline = document.createElement('button');
	tdButtons.style.textAlign = 'right';
	tdButtons.setAttribute('id', 'buttons-' + gameId);

	nodeUsers.get(opponent).firstElementChild.colSpan = 1;
	
	buttonAccept.innerHTML = '<span style="color:green">Accept</span>';
	buttonDecline.innerHTML = '<span style="color:red">Decline</span>';
	buttonAccept.className = 'btn btn-default';
	buttonDecline.className = 'btn btn-default';

	buttonAccept.addEventListener('click', () => {
		stompClient.send('/app/lobby/acceptMatch');
		buttonAccept.setAttribute('disabled', true);
	});

	buttonDecline.addEventListener('click', () => {
		stompClient.send('/app/lobby/declineMatch');
		buttonAccept.setAttribute('disabled', true);
	});

	tdButtons.appendChild(buttonAccept);
	tdButtons.appendChild(buttonDecline);
	opponentRow.appendChild(tdButtons);
}

function sendName() {
	document.getElementById('ask-for-name').style.setProperty('display', 'none');
	document.getElementById('your-name').style.setProperty('display', 'block');
	stompClient.send('/app/lobby/join', {}, JSON.stringify({name: document.getElementById('name').value.trim()}));
}

function addPlayer(name) {
	let tr = document.createElement('tr'),
		td = document.createElement('td');
	td.innerHTML = name;
	td.colSpan = 2;
	tr.appendChild(td);
	nodeUsers.set(name, tr);
	document.getElementById('players').appendChild(tr);
}

function removePlayer(name) {
	let tr = nodeUsers.get(name);
	tr.parentElement.removeChild(tr);
	nodeUsers.delete(name);
}

onload = () => {
	for(form of document.getElementsByTagName('form')) {
		form.addEventListener('submit', e => {
			e.preventDefault();
		});
	}
	
	connect();
	
	document.getElementById('send-name').addEventListener('click', sendName);
	document.getElementById('search-game').addEventListener('click', () => {
		stompClient.send('/app/lobby/searchGame');
	});
};