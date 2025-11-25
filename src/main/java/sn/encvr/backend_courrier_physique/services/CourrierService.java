package sn.encvr.backend_courrier_physique.services;


import sn.encvr.backend_courrier_physique.entities.Courrier;

import java.util.Collection;
import java.util.Optional;

public interface CourrierService {
    Collection<Courrier> getAll();
    Optional<Courrier> getById(Long id);
    Courrier save(Courrier courrier);
    Courrier update(Courrier  courrier);
    void deleteById(Long id);

}
