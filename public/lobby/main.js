var socket = io();

var player_list,
	players = new Map();

//var wantToChallenge = null;

socket.on('connect', () => {
	console.log(`Connected, id: ${socket.id}`);
});

socket.on('joinLobby', (id, name) => {
	console.log(`new player in lobby: ${name}`);
	addPlayer(id, name);
});

socket.on('leaveLobby', id => {
	let tr = players.get(id);
	tr.parentElement.removeChild(tr);
});

socket.on('playersAlreadyConnected', data => {
	console.log('players already connected: ', ids);
	for(let i = 0; i < data.length; i++)
		addPlayer(data[i].id, data[i].name);
});

socket.on('newChallenge', id => {
	
});

socket.on('name', name => {
	console.log(`new name: ${name}`);
	document.title = name;
});

onload = function() {
	player_list = document.getElementById('connectes');
}

let addPlayer = (id, name) => {
	let tr = document.createElement('tr');
	let td = document.createElement('td');
	td.innerHTML = name;
	tr.appendChild(td);
	player_list.appendChild(tr);
	players.set(id, tr);
};