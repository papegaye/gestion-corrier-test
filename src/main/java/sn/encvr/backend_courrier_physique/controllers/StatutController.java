package sn.encvr.backend_courrier_physique.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.encvr.backend_courrier_physique.dtos.StatutDTO;
import sn.encvr.backend_courrier_physique.entities.Statut;
import sn.encvr.backend_courrier_physique.services.StatutService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/statut")
public class StatutController {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StatutService statutService ;

    @GetMapping
    public List<StatutDTO> getAll() {
        return statutService.getAll().stream().map(Statut -> modelMapper.map(Statut, StatutDTO.class)).collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<StatutDTO> getById(@PathVariable  Long id) {
        Optional<Statut> statut = statutService.getById(id);
        StatutDTO statutDTO = modelMapper.map(statut.get(), StatutDTO.class);
        return ResponseEntity.ok().body(statutDTO);
    }

    @PostMapping
    public ResponseEntity<StatutDTO> create( @RequestBody StatutDTO statutDTO) {
        Statut statut= modelMapper.map(statutDTO, Statut.class);
        Statut statut1 = statutService.save(statut);
        StatutDTO response = modelMapper.map(statut1, StatutDTO.class);
        return new ResponseEntity<StatutDTO>(response, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<StatutDTO> update(@PathVariable Long id, @RequestBody StatutDTO statutDTO) {
        Optional<Statut> statut = statutService.getById(id);
        if (statut.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 si l'objet n'existe pas
        }
        Statut classeRequest = modelMapper.map(statutDTO, Statut.class);
        Statut statut1 = statutService.update(classeRequest);
        StatutDTO classeResponse = modelMapper.map(statut1, StatutDTO.class);
        return ResponseEntity.ok().body(classeResponse);


    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Statut> statut = statutService.getById(id);
        if (statut.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        statutService.deleteById(id);
        return ResponseEntity.noContent().build();
    }











}
