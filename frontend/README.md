# Ministone - Frontend

Pour la partie frontend de l'application, nous avons utilisé angular (https://angular.io/).

## Notice d'explication

### Composant app-root

Au démarrage de l'application, vous êtes transposé sur la page d'accueil. Cliquez sur le portrait de l'aubergiste pour passer dans le lobby

### Composant lobby

Dans le lobby, il convient de rentrer son nom, son niveau d'expertise, ainsi que la classe que nous désirons jouer (par défaut, il s'agit d'un mage régulier).
Une fois cela fait, cliquez sur send Envoyer afin d'envoyer ces données au serveur. Vous êtes alors connecté et pouvez voir les autres joueurs également connectés.

Si vous désirez vous mettre en file d'attente pour une partie, cliquez sur Rechercher une partie pour vous mettre en liste d'attente. Si une autres personne de votre niveau d'expertise est également en file d'attente, vous aurez le plaisir de votre des options pour Accepter ou Décliner la partie proposé avec ce joueur en face de son nom.
Une fois la partie acceptée, vous passez dans le composant game.

### Composant game

Durant la game, 3 cartes sont piochées par le joueur commençant la partie et 4 par le second à jouer.
Au début de chaque tour, les actions suivantes sont effectuées par le joueur dont le tour commence :
- Le mana maximal du joueur est augmentée de 1
- Le mana actuel est rafraichi pour atteindre le mana maximal
- Une carte est piochée

Si vous cliquez sur un minion dans votre main et que vous avez suffisamment de mana pour le jouer, il est invoqué sur votre plateau. Si vous cliquez sur un minion sur votre plateau, puis sur un minion adverse ou votre adversaire, votre minion attaque la cible désignée.
