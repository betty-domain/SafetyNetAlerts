package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonController {

    private static final Logger logger = LogManager.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;

    @GetMapping("/persons")
    public Iterable<Person> getAllPersons() {
        logger.info("Requête Get sur le endpoint 'persons' reçue");

        Iterable<Person> personIterable = personService.getAllPersons();

        logger.info("Réponse suite à la requête Get sur le endpoint persons transmise");

        return personIterable;
    }

    @PostMapping("/person")
    public Person addPerson(@Validated @RequestBody Person person) {
        logger.info("Requête Post sur le endpoint 'person' reçue");

        Person createdPerson = personService.addPerson(person);

        if (createdPerson != null) {
            logger.info("Réponse suite au Post sur le endpoint 'person' envoyée");
            return createdPerson;
        } else {
            throw new FunctionalException("person.insert.error");

        }
    }

    @PutMapping("/person")
    public Person updatePerson(@RequestBody Person person) {
        logger.info("Requête Put sur le endpoint 'person' reçue");

        Person updatedPerson = personService.updatePerson(person);
        if (updatedPerson != null) {
            logger.info("Réponse suite au Put sur le endpoint 'person' envoyée");
            return updatedPerson;
        } else {
            throw new FunctionalException("person.update.error");
        }
    }

    @DeleteMapping("/person")
    public Integer deletePerson(@RequestParam String firstname, @RequestParam String lastname) {
        logger.info("Requête Delete sur le endpoint 'person' reçue avec les paramètres firstname :" + firstname + " et lastname : " + lastname + " reçue");

        Integer deleteResult = personService.deletePerson(firstname, lastname);
        if (deleteResult != null) {
            logger.info("Réponse suite au Delete sur le endpoint 'person' reçue avec les paramètres firstname :" + firstname + " et lastname : " + lastname + " envoyée");
            return deleteResult;
        } else {
            throw new FunctionalException("person.delete.error");
        }
    }

    @GetMapping("/communityEmail")
    public Iterable<String> getCommunityEmail(@RequestParam String city) {
        logger.info("Requête Get sur le endpoint 'communityEmail' reçue");

        List<String> emailList = personService.getAllEmailsForCity(city);
        if (emailList != null) {
            logger.info("Réponse suite à la requête Get sur le endpoint communityEmail transmise");
            return emailList;
        }
        else
        {
            throw new FunctionalException("communityEmail.get.error");
        }
    }

}
