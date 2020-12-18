package com.safetynet.alerts.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PersonInfo {
//TODO : voir si on utilise MapStruct ou non pour la transformation en DTO
    private String nom;
    private String adresse;
    private int age;
    private String mail;
    private List<String> dosageMedicaments;
    private List<String> allergies;

}
