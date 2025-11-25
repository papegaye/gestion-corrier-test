package sn.encvr.backend_courrier_physique.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.encvr.backend_courrier_physique.dtos.ExpediteurDTO;
import sn.encvr.backend_courrier_physique.entities.Expediteur;
import sn.encvr.backend_courrier_physique.services.ExpediteurService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/expediteur")
public class ExpediteurController {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExpediteurService expediteurService ;



    @GetMapping
    public List<ExpediteurDTO> getAll() {
        return expediteurService.getAll().stream().map(Expediteur -> modelMapper.map(Expediteur, ExpediteurDTO.class)).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpediteurDTO> getById(@PathVariable Long id) {
        Optional<Expediteur> expediteur = expediteurService.getById(id);
        ExpediteurDTO expediteurDTO = modelMapper.map(expediteur.get(), ExpediteurDTO.class);
        return ResponseEntity.ok().body(expediteurDTO);
    }

    @PostMapping
    public ResponseEntity<ExpediteurDTO> create( @RequestBody ExpediteurDTO expediteurDTO) {
        Expediteur expediteur= modelMapper.map(expediteurDTO, Expediteur.class);
        Expediteur expediteur1 = expediteurService.save(expediteur);
        ExpediteurDTO response = modelMapper.map(expediteur1, ExpediteurDTO.class);
        return new ResponseEntity<ExpediteurDTO>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpediteurDTO> update(@PathVariable Long id, @RequestBody ExpediteurDTO expediteurDTO) {
        Optional<Expediteur> expediteur = expediteurService.getById(id);
        if (expediteur.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 si l'objet n'existe pas
        }
        Expediteur classeRequest = modelMapper.map(expediteurDTO, Expediteur.class);
        Expediteur expediteur1 = expediteurService.update(classeRequest);
        ExpediteurDTO classeResponse = modelMapper.map(expediteur1, ExpediteurDTO.class);
        return ResponseEntity.ok().body(classeResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Expediteur> expediteur = expediteurService.getById(id);
        if (expediteur.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        expediteurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }




}
