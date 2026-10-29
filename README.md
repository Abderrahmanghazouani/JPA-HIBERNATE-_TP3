# TP 3 — Gestion avancée des entités Salle, Réservation, Utilisateur et Équipement avec JPA & Hibernate

## Objectif du TP

Modéliser le fil rouge « Réservation de salles » avec des entités JPA reliées entre elles, et maîtriser :

- les relations **OneToMany / ManyToOne** (Utilisateur ↔ Réservation, Salle ↔ Réservation) ;
- la relation **ManyToMany** avec table de jointure (Salle ↔ Équipement) ;
- les **cascades** (`CascadeType.ALL`, `PERSIST`, `MERGE`) pour automatiser la persistance ;
- la **suppression orpheline** (`orphanRemoval = true`) ;
- la manipulation des entités avec l'`EntityManager` et l'observation des requêtes SQL générées.

## Technologies utilisées

| Technologie | Version | Rôle |
|---|---|---|
| Java (JDK) | 17+ | Langage |
| Maven | 3.x | Build et dépendances |
| JPA (`javax.persistence-api`) | 2.2 | API standard de persistance |
| Hibernate ORM | 5.6.15.Final | Implémentation JPA |
| H2 Database | 1.4.200 | Base de données en mémoire |
| IntelliJ IDEA | — | IDE |

> L'énoncé propose Hibernate 5.4.30 ; la 5.6.15 est utilisée car elle reste en JPA 2.2 (`javax.persistence`) tout en fonctionnant avec les JDK 17 et plus.

## Modèle de données

```
Utilisateur 1 ────── * Reservation * ────── 1 Salle * ────── * Equipement
             (cascade ALL,                         (table de jointure
              orphanRemoval)                        salle_equipement)
```

| Entité | Relation | Annotation | Côté propriétaire |
|---|---|---|---|
| Utilisateur → Reservation | 1..* | `@OneToMany(mappedBy="utilisateur", cascade=ALL, orphanRemoval=true)` | Reservation |
| Reservation → Utilisateur | *..1 | `@ManyToOne` + `@JoinColumn(name="utilisateur_id")` | Reservation |
| Reservation → Salle | *..1 | `@ManyToOne(cascade={PERSIST, MERGE})` + `@JoinColumn(name="salle_id")` | Reservation |
| Salle ↔ Equipement | *..* | `@ManyToMany` + `@JoinTable(name="salle_equipement")` | Salle |

Pas de cascade `REMOVE` sur le ManyToMany : un équipement partagé entre plusieurs salles ne doit pas être supprimé avec une salle.

## Structure du projet

```
gestion-reservations/
├── pom.xml
├── src/main/java/com/example/
│   ├── App.java                    # Classe principale (scénarios de test)
│   └── model/
│       ├── Utilisateur.java
│       ├── Salle.java
│       ├── Reservation.java
│       └── Equipement.java
└── src/main/resources/META-INF/
    └── persistence.xml             # Unité de persistance "gestion-reservations-pu"
```

## Étapes de réalisation

1. **Création du projet Maven** (archetype quickstart, `groupId=com.example`, `artifactId=gestion-reservations`).
2. **Dépendances** : JPA 2.2, Hibernate, H2 dans le `pom.xml`, plus `exec-maven-plugin` pour lancer `App`.
3. **`persistence.xml`** : unité `gestion-reservations-pu` en `RESOURCE_LOCAL`, H2 en mémoire, `hbm2ddl.auto=update`, `show_sql` et `format_sql` activés.
4. **Entités** `Utilisateur`, `Salle`, `Reservation` avec leurs attributs et relations OneToMany / ManyToOne.
5. **Entité `Equipement`** et relation ManyToMany avec `Salle` via la table `salle_equipement`.
6. **Cascades et orphanRemoval** sur `Utilisateur.reservations`.
7. **Méthodes utilitaires** (`addReservation`, `removeReservation`, `addEquipement`, `setSalle`) qui mettent à jour les deux côtés de chaque relation.
8. **Classe `App`** qui déroule quatre scénarios :
   1. création des entités avec un seul `em.persist(user)` (la cascade fait le reste) ;
   2. lecture et navigation dans les relations ;
   3. retrait d'une réservation de la collection → suppression automatique (orphanRemoval) ;
   4. suppression de l'utilisateur → ses réservations sont supprimées, les salles et équipements sont conservés.

## Lancer le projet

### Avec IntelliJ IDEA

1. `File > Open…` puis sélectionner le dossier du projet (IntelliJ détecte le `pom.xml`).
2. Attendre la fin de l'import Maven (barre de progression en bas).
3. Ouvrir `src/main/java/com/example/App.java` et cliquer sur la flèche verte ▶ à côté de `main`.

### En ligne de commande

```bash
git clone https://github.com/Abderrahmanghazouani/JPA-HIBERNATE-_TP3.git
cd JPA-HIBERNATE-_TP3
mvn clean compile exec:java
```

## Création du projet dans IntelliJ IDEA

1. `File > New > Project…` → **Maven Archetype** (ou *Maven*), JDK 17+.
2. Archetype : `maven-archetype-quickstart`, GroupId `com.example`, ArtifactId `gestion-reservations`.
3. Compléter le `pom.xml`, puis cliquer sur l'icône **Load Maven Changes** (🔄) pour télécharger les dépendances.
4. Créer le package `com.example.model` et les entités, puis `src/main/resources/META-INF/persistence.xml`.
5. Lancer `App.java` avec la flèche verte ▶ et lire les requêtes SQL dans la console **Run**.

## Publication du code sur GitHub (Git)

Depuis le terminal d'IntelliJ (`View > Tool Windows > Terminal`), à la racine du projet :

```bash
git init
git add .
git commit -m "TP3 : entites Salle, Reservation, Utilisateur, Equipement avec JPA/Hibernate"
git branch -M main
git remote add origin https://github.com/Abderrahmanghazouani/JPA-HIBERNATE-_TP3.git
git pull origin main --allow-unrelated-histories
git push -u origin main
```

Pour ajouter les captures d'écran ensuite :

```bash
git add images/
git commit -m "Ajout des captures d'ecran"
git push
```

> Équivalent sans terminal dans IntelliJ : `Git > Commit…` (Ctrl+K) pour committer, puis `Git > Push…` (Ctrl+Shift+K).

### Résultat attendu (extrait)

```
==================== 1. CREATION DES ENTITES (cascade PERSIST) ====================
>> Utilisateur persiste avec id = 1
==================== 3. SUPPRESSION ORPHELINE (orphanRemoval = true) ====================
>> Reservations restantes en base : 1
==================== 4. SUPPRESSION D'UN UTILISATEUR (cascade REMOVE) ====================
>> Utilisateurs : 0
>> Reservations : 0
>> Salles conservees : 2
>> Equipements conserves : 2
```

## Captures d'écran

### 1. Structure du projet dans IntelliJ
![Structure du projet](images/screen1.png)

Arborescence Maven : le `pom.xml`, les quatre entités dans le package `com.example.model`, la classe `App` et le fichier `META-INF/persistence.xml`.

### 2. Création des tables par Hibernate
![Création des tables](images/screen2.png)

Au démarrage, Hibernate lit les annotations et génère les tables `Utilisateur`, `Salle`, `Reservation`, `Equipement` et la table de jointure `salle_equipement`, avec les clés étrangères `utilisateur_id` et `salle_id`.

### 3. Insertions en cascade
![Insertions en cascade](images/screen3.png)

Un seul `em.persist(user)` déclenche les `insert` de l'utilisateur, des salles, des équipements, des réservations puis des lignes de `salle_equipement` : c'est l'effet des cascades `ALL` et `PERSIST`.

### 4. Suppression orpheline
![Suppression orpheline](images/screen4.png)

La réservation retirée de la collection de l'utilisateur provoque un `delete from Reservation` sans appel à `em.remove()` : c'est `orphanRemoval = true`. Il reste 1 réservation en base.

### 5. Suppression de l'utilisateur
![Suppression de l'utilisateur](images/screen5.png)

`em.remove(user)` supprime aussi ses réservations (cascade REMOVE), mais les 2 salles et les 2 équipements sont conservés, car ces relations ne sont pas en cascade REMOVE.

## Auteur

Abderrahman Ghazouani — TP réalisé dans le cadre du cours « Hibernate & JPA » (MLIAEdu).
