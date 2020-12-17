package com.safetynet.alerts.model;

import lombok.Data;

import java.io.Serializable;

    @Data
    public class MedicalRecordId implements Serializable {
        private String firstName;
        private String lastName;
    }

