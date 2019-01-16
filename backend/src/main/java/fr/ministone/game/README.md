# Choix d'implémentation

## Implémentation de Game:

- La classe Game utilise une map de Player pour stocker les deux joueurs de la partie, le joueur commençant à jouer est ensuite choisit aléatoirement parmis les joueurs de la map.

- La methode checkBoard() de la classe appèle la méthode checkDead() de chaque joueur de la map, si la méthode retourne vrai si un hero est mort.

## Implémentation de Player:

### Player(String `name`, String `sessionsId`, String `gameId`, String `heroType`, AbstractMessageSendingTemplate<String> `template`, CardMinionRepository `cardMinionRepository`, CardSpellRepository cardSpellRepository)

- Constuit un joueur appelé `name`, ayant l'identifiant `gameId` dans la session `sessionId`, jouant le hero `heroType`.

- Deux methodes jouant le special du héros ont été implémentées, la première heroSpecial(boolean `own`, boolean `isHero`, String `targetId`) est utilisée si le special du héros e beson d'une cible, dans le cas contraire la méthode heroSpecial() est appelée.

- Une implémentation similaire à celle des méthodes heroSpecial est utilisée pour l'utilisation des sorts.