package sn.encvr.backend_courrier_physique.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.encvr.backend_courrier_physique.dtos.CourrierDTO;
import sn.encvr.backend_courrier_physique.entities.Courrier;
import sn.encvr.backend_courrier_physique.services.CourrierService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/courrier")
public class CourrierController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CourrierService courrierService ;



    @GetMapping
    public List<CourrierDTO> getAll() {
        return courrierService.getAll().stream().map(Courrier -> modelMapper.map(Courrier, CourrierDTO.class)).collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<CourrierDTO> getById(@PathVariable  Long id) {
        Optional<Courrier> courrier = courrierService.getById(id);
        CourrierDTO courrierDTO = modelMapper.map(courrier.get(), CourrierDTO.class);
        return ResponseEntity.ok().body(courrierDTO);
    }


    @PostMapping
    public ResponseEntity<CourrierDTO> create( @RequestBody CourrierDTO courrierDTO) {
        Courrier courrier= modelMapper.map(courrierDTO, Courrier.class);
        Courrier courrier1 = courrierService.save(courrier);
        CourrierDTO response = modelMapper.map(courrier1, CourrierDTO.class);
        return new ResponseEntity<CourrierDTO>(response, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CourrierDTO> update(@PathVariable Long id, @RequestBody CourrierDTO courrierDTO) {
        Optional<Courrier> courrier = courrierService.getById(id);
        if (courrier.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 si l'objet n'existe pas
        }
        Courrier classeRequest = modelMapper.map(courrierDTO, Courrier.class);
        Courrier courrier1 = courrierService.update(classeRequest);
        CourrierDTO classeResponse = modelMapper.map(courrier1, CourrierDTO.class);
        return ResponseEntity.ok().body(classeResponse);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Courrier> courrier = courrierService.getById(id);
        if (courrier.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        courrierService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
