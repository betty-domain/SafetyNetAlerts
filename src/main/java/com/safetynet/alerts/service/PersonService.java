package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    /**
     * Sauvegarde la liste des personnes passées en paramètre
     *
     * @param personList Liste des personnes à sauvegarder
     */
    public void saveAllPersons(List<Person> personList) {

        if (personList != null) {
            personRepository.saveAll(personList);
        } else {//TODO à implémenter
        }
    }

    /**
     * Retourne l'ensemble des personnes existantes
     *
     * @return Liste des personnes
     */
    public Iterable<Person> getAllPersons() {
        return personRepository.findAll();
    }

    /**
     * Retourne l'ensemble des personnes existantes
     *
     * @return Liste des personnes
     */
    public Optional<Person> getPersonByFirstNameAndLastName(String firstname, String lastname) {
        return personRepository.findByFirstNameAndLastNameAllIgnoreCase(firstname, lastname);
    }

    /**
     * Sauvegarde une personne
     * @param person personne à sauvegarder
     */
    public Person addPerson(Person person) {

        Optional<Person> personOptional = this.getPersonByFirstNameAndLastName(person.getFirstName(),person.getLastName());
        if (personOptional.isPresent())
        {
            //TODO : ajouter une trace dans les logs pour signifier que la personne existe déjà
            return null;
        }
        else {
            personRepository.save(person);
            return person;
        }
    }

    /**
     * Met à jour une personne si elle existe déjà
     * @param person Personne à mettre à jour
     * @return Personne mise à jour, null si la mise à jour a échoué ou que la personne n'existait pas
     */
    public Person updatePerson(Person person)
    {
        Optional<Person> personOptional = this.getPersonByFirstNameAndLastName(person.getFirstName(),person.getLastName());

        if (personOptional.isPresent())
        {
            Person personToUpdate = personOptional.get();
            //TODO : normalement le not null est garanti par l'annotation sur la classe ?
            if (person.getAddress()!=null) {
                personToUpdate.setAddress(person.getAddress());
            }
            if (person.getCity()!=null) {
                personToUpdate.setCity(person.getCity());
            }
            if (person.getEmail()!=null) {
                personToUpdate.setEmail(person.getEmail());
            }

            if (person.getPhone()!=null) {
                personToUpdate.setPhone(person.getPhone());
            }

            if (person.getZip()!=null) {
                personToUpdate.setZip(person.getZip());
            }
            personRepository.save(personToUpdate);
            return personToUpdate;
        }
        else
        {
            //TODO : fatu-il tracer une exception ici ou une info pour signifier que l'utilisateur existe déjà?
            return null;
        }
    }

    /**
     * Suppression d'une personne si elle existe
     * @param firstname prénom de la personne à supprimer
     * @param lastname nom de la personne à supprimer
     */
    public void deletePerson(String firstname, String lastname)
    {
        personRepository.deletePersonByFirstNameAndLastNameAllIgnoreCase(firstname, lastname);
    }
}
