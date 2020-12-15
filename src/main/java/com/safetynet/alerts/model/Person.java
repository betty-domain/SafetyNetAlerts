package com.safetynet.alerts.model;

import lombok.Data;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="Persons")
public class Person {

    public Person()
    {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    //TODO quelle annotation peut-on utiliser pour obliger un non empty sur les chaines de caractères ?
    private String firstName;

    @NonNull
    private String lastName;
    @NonNull
    private String address;
    @NonNull
    private String city;
    @NonNull
    private String zip;
    @NonNull
    private String phone;
    @NonNull
    private String email;

}
