package com.example;

import com.example.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class App {

    private static EntityManagerFactory emf;

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("gestion-reservations-pu");
        try {
            Long userId = creerDonnees();
            afficherDonnees(userId);
            testerOrphanRemoval(userId);
            testerSuppressionUtilisateur(userId);
        } finally {
            emf.close();
        }
    }

    // 1) Creation et association des entites ; un seul persist grace aux cascades
    private static Long creerDonnees() {
        titre("1. CREATION DES ENTITES (cascade PERSIST)");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Utilisateur user = new Utilisateur("Alice Dupont", "alice@example.com");

            Salle salleA = new Salle("Salle A", 30);
            Salle salleB = new Salle("Salle B", 12);

            Equipement projecteur = new Equipement("Projecteur");
            Equipement tableau = new Equipement("Tableau blanc");

            salleA.addEquipement(projecteur);
            salleA.addEquipement(tableau);
            salleB.addEquipement(projecteur); // equipement partage par deux salles

            Reservation r1 = new Reservation(
                    LocalDateTime.of(2026, 11, 2, 9, 0),
                    LocalDateTime.of(2026, 11, 2, 11, 0), "Cours JPA");
            r1.setSalle(salleA);
            user.addReservation(r1);

            Reservation r2 = new Reservation(
                    LocalDateTime.of(2026, 11, 3, 14, 0),
                    LocalDateTime.of(2026, 11, 3, 16, 0), "Reunion projet");
            r2.setSalle(salleB);
            user.addReservation(r2);

            // persist(user) -> reservations (ALL) -> salles (PERSIST) -> equipements (PERSIST)
            em.persist(user);

            em.getTransaction().commit();
            System.out.println(">> Utilisateur persiste avec id = " + user.getId());
            return user.getId();
        } finally {
            em.close();
        }
    }

    // 2) Lecture et navigation dans les relations
    private static void afficherDonnees(Long userId) {
        titre("2. LECTURE ET NAVIGATION");
        EntityManager em = emf.createEntityManager();
        try {
            Utilisateur user = em.find(Utilisateur.class, userId);
            System.out.println(">> " + user);
            for (Reservation r : user.getReservations()) {
                System.out.println("   - " + r + " dans " + r.getSalle().getNom()
                        + " (equipements : " + r.getSalle().getEquipements().size() + ")");
            }
        } finally {
            em.close();
        }
    }

    // 3) orphanRemoval : retirer une reservation de la collection la supprime en base
    private static void testerOrphanRemoval(Long userId) {
        titre("3. SUPPRESSION ORPHELINE (orphanRemoval = true)");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Utilisateur user = em.find(Utilisateur.class, userId);
            Reservation res = user.getReservations().iterator().next();
            System.out.println(">> On retire de la collection : " + res);
            user.removeReservation(res); // aucun em.remove() explicite
            em.getTransaction().commit();

            Long nb = em.createQuery("SELECT COUNT(r) FROM Reservation r", Long.class).getSingleResult();
            System.out.println(">> Reservations restantes en base : " + nb);
        } finally {
            em.close();
        }
    }

    // 4) Suppression d'un utilisateur : cascade REMOVE sur ses reservations,
    //    mais salles et equipements (non cascades) sont conserves
    private static void testerSuppressionUtilisateur(Long userId) {
        titre("4. SUPPRESSION D'UN UTILISATEUR (cascade REMOVE)");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Utilisateur user = em.find(Utilisateur.class, userId);
            for (Reservation r : user.getReservations()) {
                r.getSalle().getReservations().remove(r); // garder le cote Salle coherent
            }
            em.remove(user);
            em.getTransaction().commit();

            System.out.println(">> Utilisateurs : "
                    + em.createQuery("SELECT COUNT(u) FROM Utilisateur u", Long.class).getSingleResult());
            System.out.println(">> Reservations : "
                    + em.createQuery("SELECT COUNT(r) FROM Reservation r", Long.class).getSingleResult());
            System.out.println(">> Salles conservees : "
                    + em.createQuery("SELECT COUNT(s) FROM Salle s", Long.class).getSingleResult());
            System.out.println(">> Equipements conserves : "
                    + em.createQuery("SELECT COUNT(e) FROM Equipement e", Long.class).getSingleResult());
        } finally {
            em.close();
        }
    }

    private static void titre(String t) {
        System.out.println("\n==================== " + t + " ====================");
    }
}
