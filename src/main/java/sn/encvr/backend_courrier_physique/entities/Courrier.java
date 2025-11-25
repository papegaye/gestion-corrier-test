package sn.encvr.backend_courrier_physique.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Courrier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private LocalDate dateReception;
    private String objet;

    @ManyToOne
    @JoinColumn(name = "expediteur_id")
    private Expediteur expediteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private Destinataire destinataire;

    @ManyToOne
    @JoinColumn(name = "statut_id")
    private Statut statut;


}
