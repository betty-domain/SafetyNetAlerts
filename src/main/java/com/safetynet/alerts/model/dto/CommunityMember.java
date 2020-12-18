package com.safetynet.alerts.model.dto;

import lombok.Data;

@Data
public class CommunityMember {

    private String firstname;
    private String lastname;
    private String address;
    private String phone;

    //TODO : est il possible de ne pas générer cette propriété en réponse dans le JSON ?
    private int age;
}
