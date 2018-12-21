var stompClient = null, name, sessionId;

function connect() {
	let socket = new SockJS('/gs-guide-websocket');
	stompClient = Stomp.over(socket);

	stompClient.connect({}, frame => {
		console.log('Connected: ' + frame);
		sessionId = socket._transport.url.split('/').slice(-2, -1)[0];
		console.log("SessionId : " + sessionId); //TODO : apparemment, le sessionId n'est pas le même
	});
} // TODO méthode dans gmaecontroller pour récupérer id


//Va recherche les données de noms et de gameId à partir de l'id de la session
function retrieveDataFromId() {
	stompClient.send('/app/game/retrieve');
}

onload = () => {
	connect();
	retrieveDataFromId();
}