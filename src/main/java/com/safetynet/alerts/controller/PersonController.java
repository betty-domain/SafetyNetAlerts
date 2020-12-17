package com.safetynet.alerts.controller;

import com.safetynet.alerts.viewObjects.PersonInfo;
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

        //TODO : comment retourner un code erreur sur la requête si la personne existe déjà ? erreur 403 à retourner (HTTPResponse et ResponseStatus)
        Person createdPerson = personService.addPerson(person);

        logger.info("Réponse suite au Post sur le endpoint 'person' envoyée");

        return createdPerson;
    }

    @PutMapping("/person")
    public Person updatePerson(@RequestBody Person person)
    {
        logger.info("Requête Put sur le endpoint 'person' reçue");

        Person updatedPerson = personService.updatePerson(person);
        //TODO : comment retourner un code erreur sur la requête si la personne existe déjà ?
        logger.info("Réponse suite au Put sur le endpoint 'person' envoyée");

        return updatedPerson;
    }

    @DeleteMapping("/person")
    public void deletePerson(@RequestParam String firstname, @RequestParam String lastname)
    {
        //TODO : retourner l'id de la personne supprimée
        logger.info("Requête Delete sur le endpoint 'person' reçue avec les paramètres firstname :" + firstname + " et lastname : " + lastname + " reçue");

        personService.deletePerson(firstname,lastname);

        logger.info("Réponse suite au Delete sur le endpoint 'person' reçue avec les paramètres firstname :" + firstname + " et lastname : " + lastname + " envoyée" );
    }

    @GetMapping("/personInfo")
    public Iterable<PersonInfo> getPersonsInfo(@RequestParam String firstname, @RequestParam String lastname) {
        //TODO : voir si le endpoint est correct car il ne respecte pas la syntaxe personInfo?{firstname}&{lastname} : changer en RequestParam
        logger.info("Requête Get sur le endpoint 'personInfo' avec firstname : {" + firstname + "} et lastname  {" + lastname + "} reçue");


        Iterable<PersonInfo> personInfoIterable = personService.getPersonsInfo(firstname,lastname);

        logger.info("Réponse suite au Get sur le endpoint 'personInfo' avec firstname : {" + firstname + "} et lastname  {" + lastname + "} transmise");

        return personInfoIterable;
    }
}
