# Mini projet HearthStone 🍆

## Conventions:
* Quand un client est dans le lobby (et donc possède un nom), il est référé en tant que "user"
* Si le client est dans une partie, on le nomme "player"
* Un client connecté mais qui n'a pas encore définit son nom d'utilisateur est considéré comme inexistant (il n'apparait pas dans la liste des clients connectés)

## doc socket events (client -> server):
* /lobby/join - quand un utilisateur rejoint le lobby
* /lobby/createGame - un utilisateur veut en affronter un autre

## doc socket events (server -> client):