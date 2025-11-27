package sn.encvr.backend_courrier_physique.dtos;


import lombok.Data;
import sn.encvr.backend_courrier_physique.entities.Destinataire;
import sn.encvr.backend_courrier_physique.entities.Expediteur;
import sn.encvr.backend_courrier_physique.entities.Statut;

import java.time.LocalDate;

@Data
public class CourrierDTO {

    private Long id;

    private String numero;
    private LocalDate dateReception;
    private String objet;

    private Expediteur expediteur;
    private Destinataire destinataire;
    private Statut statut;

//    Ceci est un commentaire


}