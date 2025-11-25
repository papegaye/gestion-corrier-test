package sn.encvr.backend_courrier_physique.services;


import sn.encvr.backend_courrier_physique.entities.Expediteur;

import java.util.Collection;
import java.util.Optional;


public interface ExpediteurService {

    Collection<Expediteur> getAll();
    Optional<Expediteur> getById(Long id);
    Expediteur save(Expediteur expediteur);
    Expediteur update(Expediteur  expediteur);
    void deleteById(Long id);

}
