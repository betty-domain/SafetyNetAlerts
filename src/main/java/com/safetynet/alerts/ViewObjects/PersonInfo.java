package com.safetynet.alerts.ViewObjects;

import lombok.Data;

import java.util.List;

@Data
public class PersonInfo {

    private String nom;
    private String adresse;
    private int age;
    private String mail;
    private List<String> dosageMedicaments;
    private List<String> allergies;

}
