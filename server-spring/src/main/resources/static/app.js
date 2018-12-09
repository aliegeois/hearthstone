var stompClient = null, name;

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#test").prop("disabled", !connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	} else {
		$("#conversation").hide();
	}
	$("#greetings").html("");
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
	console.log("Disconnected");
}

function sendName() {
	//stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
	name = $("#name").val();

	// Récupère les clients déjà connecté
	stompClient.subscribe(`/topic/lobby/${name}/users`, data => {
		console.log(data);
		let users = JSON.parse(data.body);
		for(let [sessionId, user] of Object.entries(users))
			showGreeting(user.name);
	});
	// Nouvel utilisateur se connecte
	stompClient.subscribe(`/topic/lobby/${name}/newUser`, data => {
		console.log(data);
		showGreeting(data.body);
	});
	// Un utilisateur nous défie
	stompClient.subscribe(`/topic/lobby/${name}/askCreateGame`, data => {
		console.log(`askCreateGame: ${data.body}`);
		let askedName = JSON.parse(data.body).asked;
	});
	// Confirmation de la demande de création d'une partie
	stompClient.subscribe(`/topic/lobby/${name}/confirmCreateGame`, data => {
		console.log(`confirmCreateGame: ${data.body}`);
		let askingName = JSON.parse(data.body).asking;
	});
	
	stompClient.send("/app/lobby/join", {}, JSON.stringify({name: name}));
}

function showGreeting(message) {
	$("#players").append("<tr><td>" + message + "</td></tr>");
}

$(() => {
	$("form").on('submit', e => {
		e.preventDefault();
	});
	$( "#connect" ).click( connect );
	$( "#disconnect" ).click( disconnect );
	$( "#send" ).click( sendName );
	$( "#toBattle" ).click( () => { stompClient.send('/app/lobby/createGame', {}, JSON.stringify({opponent: $('#battle').val()})) } );
});