//l = s => document.createElement(s);

const data = require('./data.json');

/*class GUI {
    constructor(element) {
        this.top = element;
        this.game = l('div');
    }
    
    addCard(player, card) {
        return new Promise((resolve, reject) => {
            
        });
    }
}*/

class Game {
    constructor() {
        this.players = [];
    }
    
    addPlayer() {
        // Ouvre une interface pour demander le nom du joueur et sa classe
    }
    
    init() {
        // Initialise les variables relatives à la partie
    }
}

class Player {
    constructor(heroClass) {
        this.hero = new heroClass();
        this.deck = [];
        for(let i = 0; i < data.cards.length; i++)
            if(data.cards[i].deck == 'shared' || data.cards[i].deck == heroClass.toString())
                this.deck.push(data.cards[i]);
		this.cards = [];
    }
    
    drawCard() {
        let card = this.deck[Math.floor(Math.random() * this.deck.length)];
        if(card.type == 'minion')
            return new CardMinion(this, card.name, card.mana, card.damage, card.health, card.effects, card.boosts);
        else if(card.type == 'spell') {
            return new CardSpell(this, card.name, card.mana, card.call);
        }
    }
    
    play() {
        // Fait jouer le joueur, c'est-à-dire piocher une carte puis on le laisse faire ce qu'il veut
    }
}

Player.timeToPlay = 120000; // 2 minutes

class Hero {
    constructor(player) {
        this.player = player;
        this.health = Hero.healthMax;
        this.healthMax = Hero.healthMax;
    }
    
    static toString() {
        throw new Error('cannot invoke toString on abstract class Hero');
    }
}

Hero.healthMax = 30;

class HeroMage extends Hero {
    static toString() {
        return 'mage';
    }
}

class HeroPaladin extends Hero {
    static toString() {
        return 'paladin';
    }
}

class HeroWarrior extends Hero {
    constrcutor(player) {
		super(player);
		this.armor = 0;
	}
	
	static toString() {
        return 'warrior';
    }
}

class Card {
    constructor(player, name, mana) {
        this.owner = player;
        this.name = name;
        this.mana = mana;
    }
}

class CardSpell extends Card {
    constructor(player, name, mana, use) {
        super(player, name, mana);
        this.use = use;
    }
    
    use() {
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
                        all: {
                            value: 1
                        }
                    }
                } pour infliger 1 dégât à tous les serviteurs
            */
            if(this.use.damageCards == 'all') {
                // Inflige des dégâts à toutes les cartes
            } else if (this.use.damageCards == 'enemy') {
                // Inflige des dégâts aux cartes ennemies
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
        }
        if(this.use.drawCards) {
            /*  Pioche des cartes
            
                exemple: this.use: {
                    drawCards: 2
                } pour piocher 2 cartes au hasard
            */
        }
    }
}

class CardMinion extends Card {
    constructor(player, name, mana, damage, health, effects, boosts) {
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
        super(player, name, mana);
        this.damage = damage;
        this.health = health;
        this.healthMax = health;
        this.effects = (effects == undefined ? [] : effects);
        this.boosts = (boosts == undefined ? {} : boosts);
    }
    
    takeDamage(quantity) {
        this.health -= quantity;
        if(this.health <= 0) {
            
        }
    }
    
    static attack(from, to) {
        /*  from instanceof CardMinion
            to instanceof CardMinion
        */
        from.takeDamage(to.damage);
        to.takeDamage(from.damage);
        
    }
}

var p = new Player(HeroMage, 'bite');

nw.Window.get().showDevTools();