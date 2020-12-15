package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public void saveAllPersons(List<Person> personList)
    {
        if (personList!=null) {
            personRepository.saveAll(personList);
        }
        else {//TODO à implémenter
        }
    }
}
