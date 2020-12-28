package com.safetynet.alerts.model.dto;

import com.safetynet.alerts.model.Person;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChildAlertDTO {
    private String lastName;

    private String firstName;

    private int age = Integer.MIN_VALUE;

    private List<FamilyMemberDTO> familyMembers= new ArrayList<>();
}
