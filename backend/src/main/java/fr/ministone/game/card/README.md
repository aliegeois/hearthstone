# Types de card

## CardSpell
### CardSpell(String `id`, String `deck`, IPlayer `owner`, String `name`, int `mana`, Set<SingleTargetEffect> `singleEffect`, Set<MultipleTargetEffect> `multipleEffect`, Set<GlobalEffect> `globalEffects`)

Créer une carte sort ayant le nom `name`, appartenant au joueur `owner`, coutant `mana` points de mana, ayant les effets `singleEffects`, `multipleEffects` et `globalEffects`.

La carte appartient au deck:
- commun si `deck = "shared"`
- des héros guerriers si `deck = "warrior"`
- des héros mages si `deck = "mage"`
- des héros paladins si `deck = "paladin"`

## CardMinion
### CardMinion(String `id`, String `deck`, IPlayer `owner`, String `name`, int `mana`, int `damage`, int `health`, Set<String> `capacities`, Map<String, Integer> `boosts`)

Créer une carte minion ayant le nom `name`, appartenant au joueur `owner`, coutant `mana` points de mana pour invoquer, ayant `health` points de vie, `damage` points d'attaque, les capacités spéciales contenues dans `capacities` et est affectée par les boosts contenus dans `boosts`.

La carte appartient au deck:
- commun si `deck = "shared"`
- des héros guerriers si `deck = "warrior"`
- des héros mages si `deck = "mage"`
- des héros paladins si `deck = "paladin"`