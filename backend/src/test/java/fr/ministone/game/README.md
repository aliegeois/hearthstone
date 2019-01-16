# Tests et Doublures

## Tests

### GameTest

La classe de test GameTest vérifie le fonctionnement des méthodes de la classe Game (hors getters et setters) en passant par une doublure MGame.

### PlayerTest

La classe de test PlayerTest vérifie le fonctionnement des méthodes de la classe Player (hors getters et setter) en passant par des doublure PlayerMock.

### HeroTest

La classe de test HeroTest vérifie le fonctionnement des méthodes (hors getters et setters) des classes héritant de la classe Hero :
- HeroMage
- HeroPaladin
- HeroWarrior

La classe utilise des PlayerMock dont la mana n'est pas précisée.

### EffectTest

La classe de test EffectTest vérifie le fonctionnements des méthodes (hors getters et setters) des classes héritants de Effect:
- SingleTargetDamage
- SingleTargetDamageBuff
- SingleTargetLifeBuff
- SingleTargetHeal
- MultipleTagerDamage
- MultipleTargetHeal
- MultipleTargerBuff
- DrawRandom
- Transform
- SummonSpecific

La classe utilise des PlayerMock

### CardMinionTest

La classe de test CardMinionTest vérifie le fonctionnement des méthodes (hors getters et setters) de la classe CardMinion.
La classe utilise des PlayerMock dont la mana n'est pas précisée.

### CardSpellTest

La classe de test CardSpellTest vérifie le fonctionnement des méthodes (hors getters et setters) de la classe CardSpell.
La classe utilise des PlayerMock dont la mana n'est pas précisée.

## Doublures

### MGame

La doublure MGame simule le fonctionnement d'une partie mais guarantisant l'orde dans lequel les joueurs vont jouer.

### PlayerMock

La doublure PlayerMock simule le fonctionnement d'un joueur dont le nombre de point de mana est donné en paramètre, si le nombre de point de mana n'est pas précisé, la mana est fixée à 10.

