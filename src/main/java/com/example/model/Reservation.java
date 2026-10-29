package com.example.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateDebut;

    @Column(nullable = false)
    private LocalDateTime dateFin;

    private String motif;

    // Cote proprietaire : la colonne utilisateur_id est dans la table Reservation
    @ManyToOne(optional = false)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    // PERSIST/MERGE : persister une reservation persiste aussi sa salle si elle est nouvelle
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    public Reservation() {
    }

    public Reservation(LocalDateTime dateDebut, LocalDateTime dateFin, String motif) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
    }

    public Long getId() { return id; }
    public LocalDateTime getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDateTime dateDebut) { this.dateDebut = dateDebut; }
    public LocalDateTime getDateFin() { return dateFin; }
    public void setDateFin(LocalDateTime dateFin) { this.dateFin = dateFin; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    public Salle getSalle() { return salle; }

    // Synchronise aussi le cote Salle
    public void setSalle(Salle salle) {
        this.salle = salle;
        if (salle != null) {
            salle.getReservations().add(this);
        }
    }

    @Override
    public String toString() {
        return "Reservation{id=" + id + ", motif='" + motif + "', du " + dateDebut + " au " + dateFin + "}";
    }
}
