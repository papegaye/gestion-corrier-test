package sn.encvr.backend_courrier_physique.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.encvr.backend_courrier_physique.entities.Destinataire;
import sn.encvr.backend_courrier_physique.repositories.DestinataireRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class DestinataireImp implements DestinataireService {

    @Autowired
    private DestinataireRepository destinataireRepository ;

    @Override
    public Collection<Destinataire> getAll() {
        return destinataireRepository.findAll();
    }

    @Override
    public Optional<Destinataire> getById(Long id) {
        return destinataireRepository.findById(id);
    }

    @Override
    public Destinataire save(Destinataire destinataire) {
        return destinataireRepository.save(destinataire);
    }

    @Override
    public Destinataire update(Destinataire destinataire) {
        return destinataireRepository.save(destinataire);
    }

    @Override
    public void deleteById(Long id) {
        destinataireRepository.deleteById(id);

    }
}
