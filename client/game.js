/**
 * @class
 */
class Game {
	constructor() {
		this.players = new Map();
	}
	
	/**
	 * 
	 * @param {SClient} sc 
	 * @param {string} heroName 
	 */
	addPlayer(sc, heroName) {
		this.players.set(sc.socket.id, new Player(sc, heroName));
		let nbPlayers = this.players.size;
		if(nbPlayers == 2) {
			for(let p1 in this.players.values()) {
				for(let p2 in this.players.values()) {
					if(p1 != p2) {
						p1.setOpponent(p2);
						p2.setOpponent(p1);
					}
				}
			}
			this.start();
		} else if(nbPlayers > 2) {
            // Erreur
		}
	}
	
	/**
	 * 
	 * @param {number} playerId 
	 */
	disconnect(playerId) {
		// Déconnection d'un joueur
	}
	
	/*setHero(playerId, heroId) {
		this.players[playerId].setHero(heroId);
	}*/
	
	start() {
		
	}
}

/**
 * @class
 */
class Player {
	/**
	 * 
	 * @param {SClient} s 
	 * @param {string} heroName 
	 */
	constructor(s, heroName) {
		this.sclient = s;
		
		switch(heroName) {
			case 'mage':
				this.hero = new HeroMage(this);
				break;
			case 'paladin':
				this.hero = new HeroPaladin(this);
				break;
			case 'warrior':
				this.hero = new HeroWarrior(this);
				break;
			default:
				// Erreur
				throw new Error('Héro inconnu');
		}
		
		this.CARD_ID = 0;
		
		this.deck = new Set();
        for(let i = 0; i < data.cards.length; i++) {
			let card = data.cards[i];
            if(card.deck == 'shared' || card.deck == heroName) {
				if(card.type == 'minion')
					this.deck.add(new CardMinion(this, ++this.CARD_ID, card.name, card.mana, card.damage, card.health, card.effects, card.boosts));
				else if(card.type == 'spell')
					this.deck.add(new CardSpell(this, ++this.CARD_ID, card.name, card.mana, card.use));
			}
		}
		this.hand = new Set();
		this.board = new Set();
	}
	
	/**
	 * 
	 * @param {Player} op 
	 */
	setOpponent(op) {
		this.opponent = op;
	}
	
	drawCard() {
        let card = this.deck[Math.floor(Math.random() * this.deck.length)];
        if(card.type == 'minion')
			this.hand.add(new CardMinion(this, ++this.CARD_ID, card.name, card.mana, card.damage, card.health, card.effects, card.boosts));
		else if(card.type == 'spell')
			this.hand.add(new CardSpell(this, ++this.CARD_ID, card.name, card.mana, card.use));
	}
	
	/**
	 * 
	 * @param {CardMinion} card
	 * @param {? implements takesDamage} target
	 */
	playCard(card, target) {
		if(!this.hand.has(card))
			throw new Error('La carte n\'est pas dans la main');
		
		this.hand.delete(card);
		this.board.add(card);
		card.invoke(target);
	}
}

Player.timeToPlay = 120000; // 2 minutes

/**
 * @class
 */
class Hero {
	/**
	 * 
	 * @param {Player} player 
	 */
    constructor(player) {
        this.player = player;
        this.health = Hero.healthMax;
        this.healthMax = Hero.healthMax;
	}
	
	/**
	 * 
	 * @param {number} quantity 
	 */
	takeDamage(quantity) {
		this.health -= quantity;
		if(this.health <= 0) {

		}
	}
    
    static toString() {
        throw new Error('cannot invoke toString on abstract class Hero');
    }
}

Hero.healthMax = 30;

/**
 * @class
 */
class HeroMage extends Hero {
	special(target) {
		target.takeDamage(1);
	}
	
	static toString() {
        return 'mage';
	}
}

/**
 * @class
 */
class HeroPaladin extends Hero {
	special() {
		this.player.board.add(new CardMinion(this.player, ++this.player.CARD_ID, "Recrue de la Main d'argent", 0, 1, 1));
	}

    static toString() {
        return 'paladin';
    }
}

/**
 * @class
 */
class HeroWarrior extends Hero {
    constructor(player) {
		super(player);
		this.armor = 0;
	}

	special() {
		this.armor += 2;
	}
	
	static toString() {
        return 'warrior';
    }
}

/**
 * @class
 */
class Card {
	/**
	 * 
	 * @param {Player} player 
	 * @param {number} id 
	 * @param {string} name 
	 * @param {number} mana 
	 */
    constructor(player, id, name, mana) {
        this.owner = player;
		this.id = id;
        this.name = name;
        this.mana = mana;
    }

    invoke() {}
}

/**
 * @class
 */
class CardMinion extends Card {
	/**
	 * 
	 * @param {Player} player 
	 * @param {number} id 
	 * @param {string} name 
	 * @param {number} mana 
	 * @param {number} damage 
	 * @param {number} health 
	 * @param {array} effects 
	 * @param {array} boosts 
	 */
    constructor(player, id, name, mana, damage, health, effects, boosts) {
        /*  valeurs possibles de effect: taunt, charge, lifesteal
            exemple: effects: [
                'taunt',
                'charge'
            ] pour invoquer un serviteur avec provocation et charge
            
            boost: donne un bonus de statistiques au cartes alliées tant que ce serviteur est en vie
            exemple: boost: {
                damage: 1
            } pour donner 1 dégât supplémentaire aux serviteurs alliés
        */
        super(player, id, name, mana);
		this.damageBase = damage; // Quantité de dégâts de la carte de base
        this.damage = damage; // Quantité de dégâts actuels (boosts compris)
		this.damageBoosted = 0; // Quantité de dégats en plus/moins
		this.healthBase = health;
        this.health = health;
		this.healthBoosted = 0;
        //this.healthMax = health;
        this.effects = (effects == undefined ? [] : effects);
        this.boosts = (boosts == undefined ? {} : boosts);
		this.ready = effects.contains('charge');
    }
	
	invoke() {
		for(let [boost, value] in Object.entries(this.boosts)) {
			for(let card of player.board) {
				if(card != this) {
					switch(boost) {
						case 'damage':
							card.damageBoosted += value;
							card.damage += value;
							break;
						case 'health':
							card.healthBoosted += value;
							card.health += value;
							break;
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param {number} quantity 
	 */
    takeDamage(quantity) {
        this.health -= quantity;
        if(this.health <= 0) {
            
        }
    }
	
	/**
	 * 
	 * @param {CardMinion} minion 
	 */
	attackMinion(minion) {
		minion.takeDamage(this.damage);
		this.takeDamage(minion.damage);
	}
	
	/**
	 * 
	 * @param {Hero} hero 
	 */
	attackHero(hero) {
		hero.takeDamage(this.damage);
	}
	
	get validOpponents() {
		let all = new Set(),
			taunts = new Set();
		for(let card of this.player.opponent.board) {
			all.add(card);
			if(card.effects.contains('taunt'))
				taunts.add(card);
		}
		
		return taunts.size ? taunts : all;
	}
    
    /*static attack(from, to) {
        // from instanceof CardMinion
        // to instanceof CardMinion
        from.takeDamage(to.damage);
        to.takeDamage(from.damage);
    }*/
}

/**
 * @class
 */
class CardSpell extends Card {
	/**
	 * 
	 * @param {Player} player 
	 * @param {number} id 
	 * @param {string} name 
	 * @param {number} mana 
	 * @param {object} use 
	 */
    constructor(player, id, name, mana, use) {
        super(player, id, name, mana);
        this.use = use;
    }
    
    invoke(target) {
        if(this.use.summonMinions) {
            /*  Invoque des cartes sur le terrain
            
                exemple: this.use: {
                summonMinions: {
                    name: "Avocat commis d'office",
                    quantity: 1
                }
            } pour invoquer 1 Avocat commis d'office
            */

        }
        if(this.use.damageCards) {
            /*  Inflige des dégâts à des serviteurs
            
                Options complémentaires:
                .value: quantité de dégâts ingligé par le sort (prend en comtpe le héro ?)
                exemple: this.use: {
                    damageCards: {
                        all: 1
                    }
                } pour infliger 1 dégât à tous les serviteurs
            */
            if(this.use.damageCards.all) {
				// Inflige des dégâts à toutes les cartes
				for(let minion of this.player.opponent.board)
					minion.takeDamage(this.use.damageCards.all);
            } else if (this.use.damageCards.enemy) {
				// Inflige des dégâts aux cartes ennemies
				for(let minion of this.player.opponent.board)
					minion.takeDamage(this.use.damageCards.all);
				for(let minion of this.player.board)
					minion.takeDamage(this.use.damageCards.all);
            }
        }
        if(this.use.alterCard) {
            /*  Change les statistiques d'une carte
            
                Options complémentaires:
                .[mana/damage/health].[set/alter]: set pour remplacer la stat et alter pour augmenter/réduire la valeur de la stat
                exemple: this.use: {
                    alterCard: {
                        damage: {
                            alter: 3
                        }
                    }
                } pour augmenter de 3 l'attaque d'une carte
            */
			for(let [stat, value] of Object.entries(this.use.alterCard)) {
				switch(stat) {
					case 'damage':
						if(value.set) {
							target.damage = value.set;
							target.damageBoosted = 0;
						}
						if(value.alter) {
							target.damage += value.alter;
							target.damageBoosted += value.alter;
						}
						break;
					case 'health':
						if(value.set) {
							target.health = value.set;
							target.healthBoosted = 0;
						}
						if(value.alter) {
							target.health += value.alter;
							target.healthBoosted += value.alter;
						}
						break;
				}
			}
		}
        if(this.use.alterHero) {
            /*  Change les statistiques du héro
                
                Options complémentaires:
                options.[health/armor].[set/alter]: set pour remplacer la stat et alter pour augmenter/réduire la valeur de la stat
                exemple: this.use: {
                    alterHero: {
                        armor: {
                            alter: 5
                        }
                    }
                } pour ajouter 5 d'armure au héro
            */
			for(let [stat, value] of Object.entries(this.use.alterHero)) {
				switch(stat) {
					case 'health':
						if(value.set)
							this.player.hero.health = value.set;
						if(value.alter)
							this.player.hero.health = value.alter;
						break;
					case 'armor':
						if(value.set)
							this.player.hero.armor = value.set;
						if(value.alter)
							this.player.hero.armor = value.alter;
						break;
			   }
		   }
        }
        if(this.use.drawCards) {
            /*  Pioche des cartes
            
                exemple: this.use: {
                    drawCards: 2
                } pour piocher 2 cartes au hasard
            */
			for(let i = 0; i < this.use.drawCards; i++)
				this.player.drawCard();
        }
    }
}