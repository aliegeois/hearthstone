# Ministone
Mini projet Hearthstone

Projet d'Analyse Conception et Evolution logicielle et Tests Logiciels de Master 1 ALMA. Ce projet a pour but de la création d'une version simplifiée du jeu Hearthstone dévellopé par Blizzard (https://playhearthstone.com/en-us/.

Le projet possède une interface web réalisée grâce à Angular (https://angular.io/) communiquant via websockets avec un serveur spring gérant également la persistence des données statiques (cartes, effets...)

### Auteurs

- E152689R : Arthur Liégeois (aliegeois)
- E134932Q : Ronan Gueguen (Karides)
- E158479K : Antoine Godet (Zardas)
- E16B498D : Charles Bequet-Ermoy (BuildTools)

>Nous savons que des commits ont été effectués après la date de rendu (15/01 à 23h59) mais nous préférons que vous évaluiez la version finale, quitte à perdre des points pour le retard.

## Comment lancer le(s) serveur(s)

### Compiler le serveur :
mvn clean install
### Lancer le serveur :
mvn spring-boot:run (ou java -jar target/ministone-0.0.1-SNAPSHOT.jar)

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


## Tests du code JAVA

Les tests sont situés dans backend/src/test/java/fr/ministone

### Petit notice sur les communications client-serveur

Après avoir envisagé de faire uniquement des calculs coté client et de renvoyer tout l'état du jeu au client afin qu'il se mette à jour après chacune de ses actions, nous avons préféré partir sur un système fonctionnant ainsi :
 1 - Le client envoie un input au client sur une url auquel le server est subscribe
 2 - Le serveur recoit la demande client et détermine sa validité, calcul l'effet sur les données qu'il possède au niveau du serveur et renvoit le même input que l'utilisateur sur une url à laquelle le client est subscribe
 3 - Le client reçoit la requête sur son url, et fait les calculs coté client et affiche à l'utilisateur le nouveau statut du jeu.
 
 Cette approche nous a semblée plus pertinente car elle permettait d'avoir une logique client et serveur très similaire, puisque elles réalisent pour une bonne partie les mêmes calculs. Ce, sans perdre en sécurité puisque, si l'utilisateur s'amuse à modifier le contenu de ses variables en local, le serveur ne lui répondra rien, déterminant que sa demande n'est pas valide.
