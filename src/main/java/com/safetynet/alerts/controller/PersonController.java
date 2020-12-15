package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.JsonReaderService;
import com.safetynet.alerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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

        logger.info("Réponse suite au Post sur le endpoint 'person' envoyées");

        return createdPerson;
    }

    @PutMapping("/person")
    public Person updatePerson(@RequestBody Person person)
    {
        logger.info("Requête Put sur le endpoint 'person' reçue");

        Person updatedPerson = personService.updatePerson(person);
        //TODO : comment retourner un code erreur sur la requête si la personne existe déjà ?
        logger.info("Réponse suite au Put sur le endpoint 'person' traitée");

        return updatedPerson;
    }

    @DeleteMapping("/person/{firstname}/{lastname}")
    public void DeletePerson(@PathVariable("firstname") final String firstname, @PathVariable("lastname") final String lastname)
    {
        logger.info("Requête Delete sur le endpoint 'person' reçue avec les paramètres firstname :" + firstname + " et lastname : " + lastname + " reçue");

        personService.deletePerson(firstname,lastname);

        logger.info("Réponse suite au Delete sur le endpoint 'person' reçue avec les paramètres firstname :" + firstname + " et lastname : " + lastname + " traitée" );
    }
}
