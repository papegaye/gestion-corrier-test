package sn.encvr.backend_courrier_physique.dtos;


import lombok.Data;


@Data
public class DestinataireDTO {

    private Long id;

    private String nom;
    private String email;
    private String telephone ;
    private String adresse ;


}