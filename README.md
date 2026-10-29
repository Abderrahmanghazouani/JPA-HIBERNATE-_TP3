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

## Explication du code
- **Utilisateur** : `@OneToMany(cascade = ALL, orphanRemoval = true)` → enregistrer ou supprimer un utilisateur fait de même pour ses réservations ; une réservation retirée de sa liste est supprimée de la base.
- **Reservation** : deux `@ManyToOne` (vers Utilisateur et Salle) ; c'est elle qui contient les clés étrangères `utilisateur_id` et `salle_id`.
- **Salle** : `@ManyToMany` + `@JoinTable("salle_equipement")` vers Equipement, sans cascade REMOVE pour ne pas supprimer un équipement partagé.
- **Equipement** : côté inverse du ManyToMany (`mappedBy = "equipements"`).
- **App** : un seul `em.persist(user)` enregistre tout grâce aux cascades, puis on teste la suppression orpheline et la suppression d'un utilisateur.

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

## Résumé
Ce TP montre comment relier des entités JPA (OneToMany, ManyToOne, ManyToMany) et comment les cascades et `orphanRemoval` automatisent l'enregistrement et la suppression, tout en protégeant les données partagées comme les équipements.
