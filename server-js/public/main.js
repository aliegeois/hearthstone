var socket = io();

socket.on('connect', () => {
	console.log(`Connected, id: ${socket.id}`);
});

socket.on('log', data => console.log(data));

onload = function() {
	document.getElementById('valider').addEventListener('click', _ => {
		let pseudo = document.getElementById('pseudo').value.trim();
		
		if(pseudo.length >= 3) {
			localStorage.setItem('name', pseudo);
			//socket.emit('login', pseudo);
			window.location.replace('/lobby');
			//window.location.href = '/lobby';
		} else {
			// Pseudo incorrect
			console.warn('Pseudo incorrect (doit être >= 3 caractères)');
		}
	});
}