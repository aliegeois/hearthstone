# Effets possibles d'une carte sort

## Effets qui s'appliquent globalement

### DrawRandom(int `numberCardsDrawn`)
Fait piocher `numberCardsDrawn` cartes

### SummonSpecific(String `minionName`)
Invoque la carte ayant le nom `minionName`

### Transform(String `minionName`)
Transforme la carte cible en la carte ayant pour nom `minionName`

## Effets ayant besoin d'une cible

### SingleTargetDamage(int `damage`)
Inflige `damage` points de dégâts à la cible

### SingleTargetHeal(int `amount`)
Redonne jusqu'à `amount` points de vie à la cible (dans la limite de sa vie maximale)

### SingleTargetDamageBuff(int `attack`)
Donne un bonus de `attack` points d'attaque au serviteur ciblé

### SingleTargetLifeBuff(int `life`)
Donne un bonus de `life` points de vie au serviteur ciblé

## Effets n'ayant pas besoin d'une cible

### MultipleTargetDamage(boolean `ownBoard`, boolean `opponentBoard`, boolean `ownHero`, boolean `opponentHero`, int `damage`)
Inflige `damage` points de dégâts à certaines cibles:
- toutes ses cartes si `ownBoard = true`
- les cartes adverses si `opponentBoard = true`
- son héro si `ownHero = true`
- le héro adverse si `ownHero = true`

### MultipleTargetHeal(boolean `ownBoard`, boolean `opponentBoard`, boolean `ownHero`, boolean `opponentHero`, int `amount`)
Redonne jusqu'à `amount` points de vie à certaines cibles (dans la limite de leur vie maximale):
- toutes ses cartes si `ownBoard = true`
- les cartes adverses si `opponentBoard = true`
- son héro si `ownHero = true`
- le héro adverse si `ownHero = true`

### MultipleTargetBuff(boolean `ownBoard`, boolean `opponentBoard`, boolean `ownHero`, boolean `opponentHero`, int `life`, int `attack`, int `armor`)
Donne un bonus de `life` points de vie ET donne un bonus de `attack` points d'attaque ET donne un bonus de `armor` points d'armure à certaines cibles:
- toutes ses cartes si `ownBoard = true`
- les cartes adverses si `opponentBoard = true`
- son héro si `ownHero = true`
- le héro adverse si `ownHero = true`