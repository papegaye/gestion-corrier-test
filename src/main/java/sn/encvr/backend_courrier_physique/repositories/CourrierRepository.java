package sn.encvr.backend_courrier_physique.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import sn.encvr.backend_courrier_physique.entities.Courrier;

public interface CourrierRepository extends JpaRepository<Courrier, Long> {

}
