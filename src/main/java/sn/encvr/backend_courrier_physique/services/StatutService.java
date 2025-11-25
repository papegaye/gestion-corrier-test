package sn.encvr.backend_courrier_physique.services;

import sn.encvr.backend_courrier_physique.entities.Statut;

import java.util.Collection;
import java.util.Optional;


public interface StatutService {
    Collection<Statut> getAll();
    Optional<Statut> getById(Long id);
    Statut save(Statut statut);
    Statut update(Statut  statut);
    void deleteById(Long id);

}
