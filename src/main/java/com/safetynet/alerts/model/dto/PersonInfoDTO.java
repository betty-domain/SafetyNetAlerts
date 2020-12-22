package com.safetynet.alerts.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PersonInfoDTO {
//TODO : voir si on utilise MapStruct ou non pour la transformation en DTO
    private String lastname;
    private String address;
    private int age;
    private String mail;
    private List<String> medicationList = new ArrayList<>();
    private List<String> allergiesList = new ArrayList<>();

}
