package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "application.runner.enabled=true" })
public class PersonIT {

    @Autowired
    private PersonService personService;

    @Test
    public void getAllEmailsForCityIT()
    {
        assertThat(personService.getAllEmailsForCity("Paris").size()).isEqualTo(2);
    }

    @Test
    public void getPersonByFirstNameAndLastNameIT()
    {
        assertThat(personService.getPersonByFirstNameAndLastName("Clive","Ferguson")).isPresent();
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
