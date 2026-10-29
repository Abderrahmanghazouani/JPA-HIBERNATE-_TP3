# TP 3 — Entités Salle, Réservation, Utilisateur et Équipement avec JPA & Hibernate

## Objectif
Créer des entités JPA liées entre elles (OneToMany, ManyToOne, ManyToMany) et tester les cascades et la suppression orpheline.

## Technologies
Java 17, Maven, JPA 2.2, Hibernate 5.6, H2, IntelliJ IDEA.

## Étapes
1. Créer un projet Maven dans IntelliJ.
2. Ajouter les dépendances JPA, Hibernate et H2 dans `pom.xml`.
3. Configurer `persistence.xml`.
4. Créer les entités `Utilisateur`, `Salle`, `Reservation`, `Equipement`.
5. Lancer `App.java` et observer les requêtes SQL.

## Lancer le projet
Dans IntelliJ : ouvrir `App.java` puis cliquer sur ▶.

Ou en ligne de commande :
```bash
mvn clean compile exec:java
```

## Envoyer sur GitHub
```bash
git add .
git commit -m "TP3"
git push
```

## Captures d'écran

### 1. Structure du projet
![screen1](images/screen1.png)
Les fichiers du projet Maven.

### 2. Entité Utilisateur
![screen2](images/screen2.png)
Un utilisateur a plusieurs réservations (cascade + suppression orpheline).

### 3. Entité Salle
![screen3](images/screen3.png)
Relation ManyToMany avec les équipements.

### 4. Entité Reservation
![screen4](images/screen4.png)
Chaque réservation est liée à un utilisateur et à une salle.

### 5. persistence.xml
![screen5](images/screen5.png)
Connexion à la base H2.

### 6. Création des tables
![screen6](images/screen6.png)
Hibernate crée les tables automatiquement.

### 7. Résultat
![screen7](images/screen7.png)
L'utilisateur et ses réservations sont supprimés, les salles et équipements restent.
