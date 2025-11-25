package sn.encvr.backend_courrier_physique.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.encvr.backend_courrier_physique.entities.Courrier;
import sn.encvr.backend_courrier_physique.repositories.CourrierRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class CourrierImp implements CourrierService {

    @Autowired
    private CourrierRepository courrierRepository ;


    @Override
    public Collection<Courrier> getAll() {
        return courrierRepository.findAll();
    }

    @Override
    public Optional<Courrier> getById(Long id) {
        return courrierRepository.findById(id);
    }

    @Override
    public Courrier save(Courrier courrier) {
        return courrierRepository.save(courrier);
    }

    @Override
    public Courrier update(Courrier courrier) {
        return courrierRepository.save(courrier);
    }

    @Override
    public void deleteById(Long id) {
        courrierRepository.deleteById(id);

    }
}
