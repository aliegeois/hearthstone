# Ministone
Mini projet Hearthstone

Projet d'Analyse Conception et Evolution logicielle et Tests Logicielles de Master 1 ALMA. Ce projet a pour but de la création d'une version simplifiée du jeu Hearthstone dévellopé par Blizzard (https://playhearthstone.com/en-us/.

Le projet possède une interface web réalisée grâce à Angular (https://angular.io/) communiquant via websockets avec un serveur spring gérant également la persistence des données statiques (cartes, effets...)

### Auteurs

- E152689R : Arthur Liégeois (aliegeois)
- E134932Q : Ronan Gueguen (Karides)
- E158479K : Antoine Godet (Zardas)
- E16B498D : Charles Bequet-Ermoy (BuildTools)

>Nous savons que des commits ont été effectués après la date de rendu (15/01 à 00h00) mais nous préférons que vous évaluiez la version finale, quitte à perdre des points pour le retard.
>Bisou

## Comment lancer le(s) serveur(s)

### Compiler le serveur :
mvn clean install
### Lancer le serveur :
mvn spring-boot:run

### Installer les packages nodejs la première fois (dans /client) :
npm i
### Lancer Angular :
ng serve

## Utilisation standard:
- Lancer le serveur Spring
- Lancer le serveur Angular
- Accéder au serveur sur [localhost:4200](http://localhost:4200)

### Utilisation du jeu
- Se référer au README du dossier frontend
