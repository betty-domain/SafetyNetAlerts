package com.safetynet.alerts;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "application.runner.enabled=false" })
public class PersonServiceTest {

    @MockBean
    private PersonRepository personRepositoryMock;

    @Autowired
    private PersonService personService;

    private Person person;

    @BeforeAll
    private static void setUpAllTest()
    {

    }

    @BeforeEach
    private void  setUpEachTest() {
       // personService = new PersonService();
        //personService.setPersonRepository(personRepositoryMock);

        person = new Person();
        person.setZip("12345");
        person.setEmail("myEmail");
        person.setPhone("myPhone");
        person.setCity("myCity");
        person.setAddress("myAddress");
        person.setFirstName("firstName");
        person.setLastName("lastName");
    }

    @Test
    public void saveAllPersonsWithNullList()
    {
        personService.saveAllPersons(null);
        verify(personRepositoryMock, Mockito.times(0)).saveAll(anyIterable());
        //TODO : à compléter une fois le comportement avec une liste null défini
    }

    @Test
    public void saveAllPersonsWithEmptyList()
    {
        List<Person> lstPerson = new ArrayList<>();
        personService.saveAllPersons(null);
        verify(personRepositoryMock, Mockito.times(0)).saveAll(anyIterable());
        //TODO : à compléter une fois le comportement avec une liste null défini
    }

    @Test
    public void saveAllPersonsWithPersonList()
    {
        List<Person> lstPerson = new ArrayList<>();
        lstPerson.add(new Person());

        when(personRepositoryMock.saveAll(anyIterable())).thenReturn(new ArrayList<Person>());
        personService.saveAllPersons(lstPerson);
        verify(personRepositoryMock, Mockito.times(1)).saveAll(anyIterable());
    }

    @Test
    public void getAllPersonsReturnNull()
    {
        when(personRepositoryMock.findAll()).thenReturn(null);
        assertThat(personService.getAllPersons()).isNull();
    }

    @Test
    public void getAllPersonsReturnEmptyList()
    {
        when(personRepositoryMock.findAll()).thenReturn(new ArrayList<Person>());
        assertThat(personService.getAllPersons()).isEmpty();
    }

    @Test
    public void getAllPersonsReturnList()
    {
        List<Person> personList = new ArrayList<>();
        personList.add(person);
        when(personRepositoryMock.findAll()).thenReturn(personList);
        assertThat(personService.getAllPersons()).size().isEqualTo(1);
    }

    @Test
    public void getPersonByFirstNameAndLastNameWithNullValues()
    {
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(null, null)).thenReturn(null);
        assertThat(personService.getPersonByFirstNameAndLastName(null,null)).isNull();
    }

    @Test
    public void getPersonByFirstNameAndLastNameWithEmptyValues()
    {
        Optional<Person> optionalPerson = Optional.empty();
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(optionalPerson);
        assertThat(personService.getPersonByFirstNameAndLastName(new String(),new String())).isNotPresent();
    }

    @Test
    public void getPersonByFirstNameAndLastNameWithValidValues()
    {
        Optional<Person> optionalPerson = Optional.of(person);
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(optionalPerson);
        assertThat(personService.getPersonByFirstNameAndLastName(new String(),new String())).isPresent().get().isInstanceOf(Person.class);
    }

    @Test
    public void addPersonWithNullPerson()
    {
        when(personRepositoryMock.save(null)).thenReturn(null);
        verify(personRepositoryMock, Mockito.times(0)).save(any());
        assertThat(personService.addPerson(null)).isNull();
    }

    @Test
    public void addPersonWithPerson()
    {
        when(personRepositoryMock.save(person)).thenReturn(person);
        assertThat(personService.addPerson(person)).isEqualTo(person);
        verify(personRepositoryMock, Mockito.times(1)).save(any());
    }

}
