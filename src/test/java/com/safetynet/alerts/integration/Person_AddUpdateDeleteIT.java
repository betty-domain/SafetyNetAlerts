package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.MedicalRecordService;
import com.safetynet.alerts.service.PersonService;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@Tag("IntegrationTests")
@SpringBootTest(properties = {
        "application.runner.enabled=true",
        "spring.datasource.url=jdbc:h2:mem:testSafetyNetAlertPersonIT" })
public class Person_AddUpdateDeleteIT {

    @Autowired
    private PersonService personService;

    @Autowired
    private FireStationService fireStationService;

    @Autowired
    private FireStationRepository fireStationRepository;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Test
    public void deletePersonIT() {
        assertThat(personService.deletePerson("John", "Boyd")).isEqualTo(1);
    }

    @Test
    public void deletePersonNonExistingIT() {
        assertThat(personService.deletePerson("Harry", "Potter")).isNull();
    }

    @Test
    public void addPersonIT() {
        Person person = new Person();
        person.setLastName("Domain");
        person.setFirstName("Betty");
        person.setAddress("1 allée des embrumes");
        person.setZip("012345");
        person.setEmail("monemail@email.fr");
        person.setPhone("+330000000");
        person.setCity("Poudelard");

        personService.addPerson(person);
        assertThat(personService.getPersonByFirstNameAndLastName(person.getFirstName(), person.getLastName()).get()).isEqualTo(person);
    }
    @Test
    public void updatePersonIT()
    {
        // given
        Person person = new Person();
        person.setLastName("Cadigan");
        person.setFirstName("Eric");
        person.setAddress("1 allée des embrumes");
        person.setZip("012345");
        person.setEmail("monemail@email.fr");
        person.setPhone("+330000000");
        person.setCity("Poudelard");

        // when
        personService.updatePerson(person);

        Optional<Person> foundPerson = personService.getPersonByFirstNameAndLastName(person.getFirstName(), person.getLastName());

        // then
        assertThat(foundPerson.get()).isEqualTo(person);



    }



}
