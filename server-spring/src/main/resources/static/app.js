var stompClient = null, name, nodeUsers = new Map();

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
	name = $('#name').val();

	// Récupère les clients déjà connecté
	stompClient.subscribe(`/topic/lobby/${name}/usersBefore`, data => {
		console.log(`event: usersBefore, data: ${data.body}`);
		let users = JSON.parse(data.body);
		for(let user of users)
			showGreeting(user.name);
	});
	// Nouvel utilisateur se connecte
	stompClient.subscribe(`/topic/lobby/${name}/userJoined`, data => {
		console.log(`event: userJoined, data: ${data.body}`);
		let user = JSON.parse(data.body);
		addPlayer(user.name);
	});
	// Un utilisateur se déconnecte
	stompClient.subscribe(`/topic/lobby/${name}/userLeaved`, data => {
		console.log(`event: userLeaved, data: ${data.body}`);
		let user = JSON.parse(data.body);
		removePlayer(user.name);
	});
	// Un utilisateur nous défie
	stompClient.subscribe(`/topic/lobby/${name}/askCreateGame`, data => {
		console.log(`event: askCreateGame, data: ${data.body}`);
		let askedName = JSON.parse(data.body).asked;
	});
	// Confirmation de la demande de création d'une partie
	stompClient.subscribe(`/topic/lobby/${name}/confirmCreateGame`, data => {
		console.log(`event: askCreateGame, data: ${data.body}`);
		let askingName = JSON.parse(data.body).asking;
	});
	
	stompClient.send('/app/lobby/join', {}, JSON.stringify({name: name}));
}

function addPlayer(name) {
	let tr = document.createElement('tr'),
		td = document.createElement('td');
	td.innerHTML = name;
	tr.appendChild(td);
	nodeUsers.set(name, tr);
	$('#players').appendChild(tr);
	$('#players').append(`<tr><td>${message}</td></tr>`);
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