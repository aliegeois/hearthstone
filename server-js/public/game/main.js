var socket = io();

socket.on('connect', () => {
	console.log(`Connected, id: ${socket.id}`);
	socket.emit('login', localStorage.getItem('name'));
});

onload = () => {
    
}