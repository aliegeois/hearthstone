var stompClient = null, name;

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#test").prop("disabled", !connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	}
	else {
		$("#conversation").hide();
	}
	$("#greetings").html("");
}

function connect() {
	var socket = new SockJS('/gs-guide-websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function (frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		/*stompClient.subscribe('/topic/greetings', function (greeting) {
			console.log(greeting);
			showGreeting(JSON.parse(greeting.body).content);
		});
		stompClient.subscribe('/topic/attack', function (response) {
			console.log(response);
			showGreeting(JSON.parse(response.body).content);
		});*/
		/*stompClient.subscribe('/topic/', function (greeting) {
			console.log(greeting);
			showGreeting(JSON.parse(greeting.body).value);
		});*/
		/*stompClient.subscribe('/topic/game/meskouilles/test', function (greeting) {
			console.log(greeting);
			showGreeting(JSON.parse(greeting.body).value);
		});*/
		//Connexion to lobby
		
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

	stompClient.subscribe(`/topic/lobby/${name}/users`, users => {
		console.log(users);
		users = JSON.parse(users.body);
		for(let user of users)
			showGreeting(user);
		//showGreeting(JSON.parse(greeting.body).value);
	});
	stompClient.subscribe(`/topic/lobby/${name}/newUser`, userName => {
		console.log(userName);
		showGreeting(userName.body);
	});

	stompClient.send("/app/lobby/join", {}, JSON.stringify({'name': name}));
}

let sendTest = () => {
	stompClient.send("/app/game/meskouilles/test", {}, JSON.stringify({value: "meskouilles"}));
};

function showGreeting(message) {
	$("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
	$("form").on('submit', function (e) {
		e.preventDefault();
	});
	$( "#connect" ).click(function() { connect(); });
	$( "#disconnect" ).click(function() { disconnect(); });
	$( "#send" ).click(function() { sendName(); });
	$( "#test" ).click(function() { sendTest() });
});