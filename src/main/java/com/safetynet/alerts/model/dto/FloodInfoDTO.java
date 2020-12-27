package com.safetynet.alerts.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FloodInfoDTO {
    private String lastname;
    private String phone;
    private int age;
    private List<String> medicationList = new ArrayList<>();
    private List<String> allergiesList = new ArrayList<>();
}
