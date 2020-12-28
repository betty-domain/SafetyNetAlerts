package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "application.runner.enabled=false" })
public class UtilsServiceTests {


    @Test
    public void getLastNameListWithNullValues()
    {
        assertThat(UtilsService.getLastNameList(null)).isEmpty();
    }

    @Test
    public void getLastNameListWithValidList()
    {
        List<Person> personList = new ArrayList<>();
        Person person = new Person();
        person.setLastName("lastname");
        person.setFirstName("firstname");
        person.setAddress("address");
        personList.add(person);

        Person person1 = new Person();
        person1.setLastName("person1LastName");
        person.setFirstName("person1Firstname");
        personList.add(person1);

        Person person2 = new Person();
        person2.setLastName("");
        personList.add(person2);

        Person person3 = new Person();
        personList.add(person3);

        List<String> lastnameList =UtilsService.getLastNameList(personList);
        assertThat(lastnameList.size()).isEqualTo(2);
        assertThat(lastnameList.contains(person.getLastName())).isTrue();
        assertThat(lastnameList.contains(person1.getLastName())).isTrue();

    }
}
