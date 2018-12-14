var stompClient = null, name, sessionId;

function connect() {
	let socket = new SockJS('/gs-guide-websocket');
	stompClient = Stomp.over(socket);

	stompClient.connect({}, frame => {
		setConnected(true);
		console.log('Connected: ' + frame);
		sessionId = socket._transport.url.split('/').slice(-2, -1)[0];
	});
} // TODO méthode dans gmaecontroller pour récupérer id

onload = () => {
	connect();
}