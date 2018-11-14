var socket = io();

//var list;
//var wantToChallenge = null;

socket.on('connect', () => {
	console.log(`Connected, id: ${socket.id}`);
});

socket.on('log', data => console.log(data));

/*socket.on('joinLobby', (id, name) => {
	Lobby.addPlayer(id, name);
});

socket.on('leaveLobby', id => {
	Lobby.removePlayer(id);
});

socket.on('playersAlreadyConnected', ids => {
	console.log('ids: ', ids);
	for(let i = 0; i < ids.length; i++)
		Lobby.addPlayer(ids[i].id, ids[i].name);
});

socket.on('newChallenge', id => {
	
});*/

onload = function() {
	/*document.getElementById('choisir_pseudo').addEventListener('submit', e => {
		e.preventDefault();
		var pseudo = document.getElementById('pseudo').value;
		socket.emit('setname', document.getElementById('pseudo').value);
		document.getElementById('index').style.setProperty('display', 'none');
		document.getElementById('lobby').style.removeProperty('display');
	});
	
	list = document.getElementById('list_players');*/
	
	//Content.loadContent('index', document.body);
	
	document.getElementById('valider').addEventListener('click', _ => {
		let pseudo = document.getElementById('pseudo').value;
		
		if(pseudo.length >= 3) {
			socket.emit('setName', pseudo);
			window.location.replace('/lobby');
			//window.location.href = '/lobby';
		} else {
			// Pseudo incorrect
			console.warn('Pseudo incorrect (doit être >= 3 caractères)');
		}
	});
}


/*class Content {
	constructor(name, load) {
		Content.contents[name] = this;
		this.load = top => {
			while(top.firstChild)
				top.removeChild(top.firstChild);
			Content.selectedContent = name;
			load(top);
			
			return this;
		}
	}
	
	static loadContent(name, top) {
		Content.contents[name].load(top);
	}
}
Content.contents = [];
Content.selectedContent = null;

var Index = new Content('index', top => {
	let choix_pseudo = document.createElement('input');
	choix_pseudo.type = 'text';
	let valider = document.createElement('button');
	valider.innerHTML = 'Valider';
	valider.addEventListener('click', _ => {
		if(choix_pseudo.value.length >= 3) {
			socket.emit('setName', choix_pseudo.value);
			Content.loadContent('lobby', top);
		} else {
			// Pseudo incorrect
			console.warn('Pseudo incorrect (doit être >= 3 caractères)')
		}
	});
	top.innerHTML = 'Choisissez un pseudo: ';
	top.appendChild(choix_pseudo);
	top.appendChild(document.createElement('br'));
	top.appendChild(valider);
});

var Lobby = new Content('lobby', top => {
	let elems = {};
	let list = document.createElement('div');
	
	Lobby.addPlayer = (id, name) => {
		var span = document.createElement('span');
		span.innerHTML = name;
		var button = document.createElement('button');
		button.innerHTML = 'Défier';
		button.addEventListener('click', _ => {
			
		});
		list.appendChild(span);
		elems[id] = span;
	};
	
	Lobby.removePlayer = id => {
		list.removeChild(elems[id]);
	};
	
	top.appendChild(list);
});*/