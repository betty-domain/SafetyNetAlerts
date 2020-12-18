package com.safetynet.alerts.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CommunityMemberDTO {

    private String firstName;

    private String lastName;

    private String address;

    private String phone;

    //TODO : est il possible de ne pas générer cette propriété en réponse dans le JSON ?
    private int age;

    public String toString()
    {
        return "CommunityMemberDTO.toString() appel";
    }
}
