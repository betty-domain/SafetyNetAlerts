package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "application.runner.enabled=true" })
public class PersonServiceIT {

    @Autowired
    private PersonService personService;

    @Test
    public void getAllEmailsForCityIT()
    {
        List<String> mailsList =personService.getAllEmailsForCity("Paris");
        assertThat(mailsList.size()).isEqualTo(3);
        assertThat(mailsList).containsExactlyInAnyOrder("gramps@email.com","clivfd@ymail.com","jpeter@email.com");

    }

    @Test
    public void getPersonByFirstNameAndLastNameIT()
    {
        assertThat(personService.getPersonByFirstNameAndLastName("Clive","Ferguson")).isPresent();
    }


}
