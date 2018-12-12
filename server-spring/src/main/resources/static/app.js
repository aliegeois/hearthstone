var stompClient = null, name, sessionId, nodeUsers = new Map();

function setConnected(connected) {
	//document.getElementById('connect').setAttribute('disabled', connected);
	//$('#connect').prop('disabled', connected);
	//document.getElementById('test').setAttribute('disabled', !connected);
	//$('#test').prop('disabled', !connected);
	
	//$('#disconnect').prop('disabled', !connected);
	if (connected) {
		//$('#connected-content').show();
		document.getElementById('connected-content').style.setProperty('display', 'block');
		document.getElementById('connect').setAttribute('disabled', 'true');
		document.getElementById('disconnect').removeAttribute('disabled');
	} else {
		//$('#connected-content').hide();
		document.getElementById('connected-content').style.setProperty('display', 'none');
		document.getElementById('connect').removeAttribute('disabled');
		document.getElementById('disconnect').setAttribute('disabled', 'true');
	}
	//$('#players').html('');
	document.getElementById('players').innerHTML = '';
}

function connect() {
	let socket = new SockJS('/gs-guide-websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, frame => {
		setConnected(true);
		console.log('Connected: ' + frame);
		sessionId = socket._transport.url.split('/').slice(-2, -1)[0];

		// Confirmation du nom
		stompClient.subscribe(`/topic/lobby/${sessionId}/confirmName`, data => {
			console.log(`event: confirmName, data: ${data.body}`);
			name = JSON.parse(data.body).name;
			for(el of document.getElementsByClassName('name'))
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
		// Un utilisateur nous défie
		stompClient.subscribe(`/topic/lobby/${sessionId}/askCreateGame`, data => {
			console.log(`event: askCreateGame, data: ${data.body}`);
			let askedName = JSON.parse(data.body).asked;
		});
		// Confirmation de la demande de création d'une partie
		stompClient.subscribe(`/topic/lobby/${sessionId}/confirmCreateGame`, data => {
			console.log(`event: askCreateGame, data: ${data.body}`);
			let askingName = JSON.parse(data.body).asking;
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

function sendName() {
	document.getElementById('ask-for-name').style.setProperty('display', 'none');
	document.getElementById('your-name').style.setProperty('display', 'block');
	stompClient.send('/app/lobby/join', {}, JSON.stringify({name: document.getElementById('name').value.trim()}));
}

function addPlayer(name) {
	let tr = document.createElement('tr'),
		td = document.createElement('td');
	td.innerHTML = name;
	tr.appendChild(td);
	nodeUsers.set(name, tr);
	//$('#players').append(tr);
	document.getElementById('players').appendChild(tr);
}

function removePlayer(name) {
	let tr = nodeUsers.get(name);
	tr.parentElement.removeChild(tr);
	nodeUsers.delete(name);
}

//$(() => {
onload = () => {
	/*$('form').on('submit', e => {
		e.preventDefault();
	});*/
	for(form of document.getElementsByTagName('form')) {
		form.addEventListener('submit', e => {
			e.preventDefault();
		});
	}
	//$( '#connect' ).click( connect );
	document.getElementById('connect').addEventListener('click', connect);
	//$( '#disconnect' ).click( disconnect );
	document.getElementById('disconnect').addEventListener('click', disconnect);
	//$( '#send' ).click( sendName );
	document.getElementById('send').addEventListener('click', sendName);
	//$( '#search-game' ).click( () => { stompClient.send('/app/lobby/searchGame') } );
	document.getElementById('search-game').addEventListener('click', () => {
		stompClient.send('/app/lobby/searchGame');
	});
//});
};