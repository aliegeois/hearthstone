# Effets possibles d'une carte sort

## Effets qui s'appliquent globalement

### DrawRandom(int `numberCardsDrawn`)
Fait piocher `numberCardsDrawn` cartes

### SummonSpecific(String `minionName`)
Invoque la carte ayant le nom `minionName`

## Effets ayant besoin d'une cible

### SingleTargetDamage(int `damage`)
Inflige `damage` points de dégâts à la cible

### SingleTargetDamageBuff(int `attack`)
Donne un bonus de `attack` points d'attaque au serviteur ciblé

### SingleTargetHeal(int `amount`)
Redonne jusqu'à `amount` points à la cible (dans la limite de sa vie maximale)

### SingleTargetLifeBuff(int `life`)
Donne un bonus de `life` points de vie au serviteur ciblé

