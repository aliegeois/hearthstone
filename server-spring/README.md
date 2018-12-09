# Mini projet HearthStone 🍆

## Conventions:
* Quand un client est dans le lobby (et donc possède un nom), il est référé en tant que "user"
* Si le client est dans une partie, on le nomme "player"
* Un client connecté mais qui n'a pas encore définit son nom d'utilisateur est considéré comme inexistant (il n'apparait pas dans la liste des clients connectés)

## doc socket events (client -> server):
* /lobby/join - quand un utilisateur rejoint le lobby
* /lobby/createGame - un utilisateur veut en affronter un autre

## doc socket events (server -> client):
* /lobby/{userName}/users: liste des utilisateurs déjà connectés
	- array<string>: liste du nom des utilisateurs
* /lobby/{userName}/userJoined: un nouvel utilisateur s'est connecté
* /lobby/{userName}/userLeaved: un utilisateur s'est déconnecté
* /lobby/{userName}/confirmCreateGame: confirme à l'utilisateur que se demande de création de partie a été enregistrée
* /lobby/{userName}/askCreateGame: informe un utilisateur qu'un autre veut le défier

## Propositions:
* Une partie (objet "Game") est définit par un identifiant unique (passé dans l'URL du websocket) via l'objet UUID (java.util.UUID)
* Lobby et création de game: faire comme le lobby de LoL et les swaps de champions (je me comprends)