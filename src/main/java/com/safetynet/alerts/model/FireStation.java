package com.safetynet.alerts.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.alerts.model.dto.Views;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Firestations")
public class FireStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Private.class)
    private Long id;

    @JsonView(Views.Public.class)
    private String address;

    @JsonView(Views.Public.class)
    private Integer station;
}
