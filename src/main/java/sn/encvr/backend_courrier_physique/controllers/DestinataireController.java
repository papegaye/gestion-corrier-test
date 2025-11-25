package sn.encvr.backend_courrier_physique.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.encvr.backend_courrier_physique.dtos.DestinataireDTO;
import sn.encvr.backend_courrier_physique.entities.Destinataire;
import sn.encvr.backend_courrier_physique.services.DestinataireService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/destinataire")
public class DestinataireController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DestinataireService destinataireService ;


    @GetMapping
    public List<DestinataireDTO> getAll() {
        return destinataireService.getAll().stream().map(Destinataire -> modelMapper.map(Destinataire, DestinataireDTO.class)).collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<DestinataireDTO> getById(@PathVariable Long id) {
        Optional<Destinataire> destinataire = destinataireService.getById(id);
        DestinataireDTO destinataireDTO = modelMapper.map(destinataire.get(), DestinataireDTO.class);
        return ResponseEntity.ok().body(destinataireDTO);
    }


    @PostMapping
    public ResponseEntity<DestinataireDTO> create( @RequestBody DestinataireDTO destinataireDTO) {
        Destinataire destinataire= modelMapper.map(destinataireDTO, Destinataire.class);
        Destinataire destinataire1 = destinataireService.save(destinataire);
        DestinataireDTO response = modelMapper.map(destinataire1, DestinataireDTO.class);
        return new ResponseEntity<DestinataireDTO>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinataireDTO> update(@PathVariable Long id, @RequestBody DestinataireDTO destinataireDTO) {
        Optional<Destinataire> destinataire = destinataireService.getById(id);
        if (destinataire.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 si l'objet n'existe pas
        }
        Destinataire classeRequest = modelMapper.map(destinataireDTO, Destinataire.class);
        Destinataire destinataire1 = destinataireService.update(classeRequest);
        DestinataireDTO classeResponse = modelMapper.map(destinataire1, DestinataireDTO.class);
        return ResponseEntity.ok().body(classeResponse);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Destinataire> destinataire = destinataireService.getById(id);
        if (destinataire.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        destinataireService.deleteById(id);
        return ResponseEntity.noContent().build();
    }



}
