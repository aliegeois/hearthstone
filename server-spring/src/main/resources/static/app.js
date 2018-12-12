var stompClient = null, name, sessionId, nodeUsers = new Map();

function setConnected(connected) {
	$('#connect').prop('disabled', connected);
	$('#test').prop('disabled', !connected);
	$('#disconnect').prop('disabled', !connected);
	if (connected) {
		$('#conversation').show();
	} else {
		$('#conversation').hide();
	}
	$('#greetings').html('');
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
	stompClient.send('/app/lobby/join', {}, JSON.stringify({name: $('#name').val().trim()}));
}

function addPlayer(name) {
	let tr = document.createElement('tr'),
		td = document.createElement('td');
	td.innerHTML = name;
	tr.appendChild(td);
	nodeUsers.set(name, tr);
	$('#players').append(tr);
}

function removePlayer(name) {
	let tr = nodeUsers.delete(name);
	tr.parentElement.removeChild(tr);
}

$(() => {
	$('form').on('submit', e => {
		e.preventDefault();
	});
	$( '#connect' ).click( connect );
	$( '#disconnect' ).click( disconnect );
	$( '#send' ).click( sendName );
	$( '#toBattle' ).click( () => { stompClient.send('/app/lobby/createGame', {}, JSON.stringify({opponent: $('#battle').val()})) } );
});