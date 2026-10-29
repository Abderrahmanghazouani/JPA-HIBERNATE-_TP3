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
Les entités sont dans `model`, la configuration dans `META-INF/persistence.xml`.

### 2. Utilisateur → Réservations (OneToMany)
![screen2](images/screen2.png)
`cascade = ALL` et `orphanRemoval = true` : les réservations suivent l'utilisateur.

### 3. Salle ↔ Équipement (ManyToMany)
![screen3](images/screen3.png)
La table de jointure `salle_equipement` relie les salles et les équipements.
