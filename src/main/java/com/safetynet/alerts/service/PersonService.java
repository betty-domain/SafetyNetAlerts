package com.safetynet.alerts.service;

import com.safetynet.alerts.viewObjects.PersonInfo;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.event.internal.OnUpdateVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private static final Logger logger = LogManager.getLogger(PersonService.class);

    @Autowired
    private PersonRepository personRepository;

    /**
     * Sauvegarde la liste des personnes passées en paramètre
     *
     * @param personList Liste des personnes à sauvegarder
     */
    public boolean saveAllPersons(List<Person> personList) {

        if (personList != null && !personList.isEmpty()) {
            try
            {
                personRepository.saveAll(personList);
                return true;
            }
            catch (Exception exception)
            {
                logger.error("Erreur lors de l'enregistrement de la liste des personnes " + exception.getMessage() + " , Stack Trace : " + exception.getStackTrace());
                //TODO voir comment faire suivre l'exception et arrêter le programme éventuellement ?
            }
        }
        return false;
    }

    /**
     * Retourne l'ensemble des personnes existantes
     *
     * @return Liste des personnes
     */
    public Iterable<Person> getAllPersons() {
        try {
            return personRepository.findAll();
        } catch (Exception exception) {
            logger.error("Erreur lors de la récupération de la liste des personnes : " + exception.getMessage() + " Stack Trace + " + exception.getStackTrace());
            return null;
        }
    }

    /**
     * Retourne l'ensemble des personnes existantes
     *
     * @return Liste des personnes
     */
    public Optional<Person> getPersonByFirstNameAndLastName(String firstname, String lastname) {
        try {
            return personRepository.findByFirstNameAndLastNameAllIgnoreCase(firstname, lastname);
        } catch (Exception exception) {
            logger.error("Erreur lors de la récupération d'une personne : " + exception.getMessage() + " Stack Trace + " + exception.getStackTrace());
            return null;
        }
    }

    /**
     * Sauvegarde une personne si elle n'existe pas déjà
     * @param person personne à sauvegarder,
     * @return personne enregistrée, null si elle existait déjà
     */
    public Person addPerson(Person person) {
        if (person != null) {
            Optional<Person> personOptional = this.getPersonByFirstNameAndLastName(person.getFirstName(),person.getLastName());
            if (personOptional.isPresent())
            {
                logger.error("Erreur lors de l'ajout d'une personne déjà existante");
                return null;
            }
            else {
                try {
                    personRepository.save(person);
                }
                catch (Exception exception) {
                    logger.error("Erreur lors de l'ajout d'une personne :" + exception.getMessage() + " StackTrace : " + exception.getStackTrace());
                    return null;
                }
            }
        }
        return person;
    }

    /**
     * Met à jour une personne si elle existe déjà
     * @param person Personne à mettre à jour
     * @return Personne mise à jour, null si la mise à jour a échoué ou que la personne n'existait pas
     */
    public Person updatePerson(Person person)
    {
        if (person!=null) {
            Optional<Person> personOptional = this.getPersonByFirstNameAndLastName(person.getFirstName(), person.getLastName());

            if (personOptional.isPresent()) {
                Person personToUpdate = personOptional.get();

                personToUpdate.setAddress(person.getAddress());
                personToUpdate.setCity(person.getCity());
                personToUpdate.setEmail(person.getEmail());
                personToUpdate.setPhone(person.getPhone());
                personToUpdate.setZip(person.getZip());

                try {
                    personRepository.save(personToUpdate);
                    return personToUpdate;
                } catch (Exception exception) {
                    logger.error("Erreur lors de la mise à jour d'une personne : " + exception.getMessage() + " StackTrace : " + exception.getStackTrace());
                    return null;
                }
            } else {
                logger.error("Erreur lors de la mise à jour d'une personne : la personne n'existe pas");
                return null;
            }
        }
        else
        {
            logger.error("Erreur lors de la mise à jour d'une personne : object null envoyé");
            return null;
        }
    }

    /**
     * Suppression d'une personne si elle existe
     * @param firstname prénom de la personne à supprimer
     * @param lastname nom de la personne à supprimer
     */
    public Integer deletePerson(String firstname, String lastname)
    {
        Optional<Person> personOptional = this.getPersonByFirstNameAndLastName(firstname,lastname);
        if (personOptional.isPresent()) {
            try {
                return personRepository.deletePersonByFirstNameAndLastNameAllIgnoreCase(firstname, lastname);

            } catch (Exception exception) {
                logger.error("Erreur lors de la suppression d'une personne :" + exception.getMessage() + " StackTrace : " + exception.getStackTrace());
                return null;
            }
        }
        else
        {
            logger.error("Erreur lors de la suppression d'une personne inexistante");
            return null;
        }
    }




}
