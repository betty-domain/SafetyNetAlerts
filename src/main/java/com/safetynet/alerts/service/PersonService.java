package com.safetynet.alerts.service;

import com.safetynet.alerts.ViewObjects.PersonInfo;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    @Autowired
    private MedicalRecordService medicalRecordService;

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
        //TODO : que faire si plusieurs personnes sont présentes avec ce prénom ET ce nom ?
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
            logger.error("Impossible d'ajouter une personne qui existe déjà, firstname: {" + person.getFirstName() + "} et lastname : {"+ person.getLastName() + "}");
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

    /**
     * Récupère les informations des personnes selon leur nom et prénoms
     * @param firstname prénom
     * @param lastname nom
     * @return liste des personnes avec leurs informations
     */
    public Iterable<PersonInfo> getPersonsInfo(String firstname, String lastname)
    {
        List<Person> personList = personRepository.findAllByFirstNameAndLastNameAllIgnoreCase(firstname, lastname);
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordService.findMedicalRecordByFirstnameAndLastname(firstname, lastname);

        return  convertToPersonInfoIterable(personList,optionalMedicalRecord);
    }

    private Iterable<PersonInfo> convertToPersonInfoIterable(List<Person> personList, Optional<MedicalRecord> optionalMedicalRecord)
    {
        List<PersonInfo> personInfoIterable = new ArrayList<>() ;

        MedicalRecord medicalRecord = new MedicalRecord();
        if (optionalMedicalRecord.isPresent())
        {
            medicalRecord = optionalMedicalRecord.get();
        }

        DateUtils dateUtil = new DateUtils();

        final MedicalRecord finalMedicalRecord = medicalRecord;
        personList.forEach(person -> {
            PersonInfo personInfo = new PersonInfo();
            personInfo.setAdresse(person.getAddress());
            personInfo.setAge(dateUtil.getAge(finalMedicalRecord.getBirthdate()));
            personInfo.setMail(person.getEmail());
            personInfo.setAllergies(finalMedicalRecord.getAllergies());
            personInfo.setNom(person.getLastName());
            personInfo.setDosageMedicaments(finalMedicalRecord.getMedications());
            personInfoIterable.add(personInfo);
        });

        return  personInfoIterable;
    }
}
