package sn.encvr.backend_courrier_physique.services;


import sn.encvr.backend_courrier_physique.entities.Destinataire;

import java.util.Collection;
import java.util.Optional;


public interface DestinataireService {
    Collection<Destinataire> getAll();
    Optional<Destinataire> getById(Long id);
    Destinataire save(Destinataire destinataire);
    Destinataire update(Destinataire  destinataire);
    void deleteById(Long id);

}
