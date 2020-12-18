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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "application.runner.enabled=false" })
public class PersonServiceTests {

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
        assertThat(personService.saveAllPersons(null)).isFalse();
        verify(personRepositoryMock, Mockito.times(0)).saveAll(anyIterable());
    }

    @Test
    public void saveAllPersonsWithEmptyList()
    {
        List<Person> lstPerson = new ArrayList<>();
        assertThat(personService.saveAllPersons(null)).isFalse();
        verify(personRepositoryMock, Mockito.times(0)).saveAll(anyIterable());
    }

    @Test
    public void saveAllPersonsWithPersonList()
    {
        List<Person> lstPerson = new ArrayList<>();
        lstPerson.add(new Person());

        when(personRepositoryMock.saveAll(anyIterable())).thenReturn(new ArrayList<Person>());
        assertThat(personService.saveAllPersons(lstPerson)).isTrue();
        verify(personRepositoryMock, Mockito.times(1)).saveAll(anyIterable());
    }
    @Test
    public void saveAllPersonWithException() {
        List<Person> lstPerson = new ArrayList<>();
        lstPerson.add(new Person());

        given(personRepositoryMock.saveAll(lstPerson)).willAnswer(invocation -> {
            throw new Exception();
        });
        assertThat(personService.saveAllPersons(lstPerson)).isFalse();
        verify(personRepositoryMock, Mockito.times(1)).saveAll(lstPerson);

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
    public void getAllPersonsWithException()
    {
        given(personRepositoryMock.findAll()).willAnswer(invocation -> { throw new Exception();});
        assertThat(personService.getAllPersons()).isNull();
        verify(personRepositoryMock, Mockito.times(1)).findAll();


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
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(person.getFirstName(), person.getLastName())).thenReturn(optionalPerson);
        assertThat(personService.getPersonByFirstNameAndLastName(person.getFirstName(),person.getLastName())).isPresent().get().isInstanceOf(Person.class);
    }

    @Test
    public void getPersonByFirstNameAndLastNameWithException()
    {
        given(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).willAnswer(invocation -> { throw new Exception();});

        assertThat(personService.getPersonByFirstNameAndLastName(new String(),new String())).isNull();
        verify(personRepositoryMock, Mockito.times(1)).findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class));
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
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(personRepositoryMock.save(person)).thenReturn(person);
        assertThat(personService.addPerson(person)).isEqualTo(person);
        verify(personRepositoryMock, Mockito.times(1)).save(any());
    }

    @Test
    public void addPersonWithExistingPerson()
    {
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(person));

        assertThat(personService.addPerson(person)).isEqualTo(null);
        verify(personRepositoryMock, Mockito.times(0)).save(any());
    }

    @Test
    public void addPersonWithException()
    {
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());given(personRepositoryMock.save(person)).willAnswer(invocation -> { throw new Exception();});

        assertThat(personService.addPerson(person)).isNull();
        verify(personRepositoryMock, Mockito.times(1)).save(person);
    }

    @Test
    public void updatePersonWithNullPerson()
    {
        when(personRepositoryMock.save(null)).thenReturn(null);
        verify(personRepositoryMock, Mockito.times(0)).save(any());
        assertThat(personService.updatePerson(null)).isNull();
    }

    @Test
    public void updatePersonWithPerson()
    {
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(person));
        when(personRepositoryMock.save(person)).thenReturn(person);
        assertThat(personService.updatePerson(person)).isEqualTo(person);
        verify(personRepositoryMock, Mockito.times(1)).save(person);
    }

    @Test
    public void updatePersonWithNonExistingPerson()
    {
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());

        assertThat(personService.updatePerson(person)).isEqualTo(null);
        verify(personRepositoryMock, Mockito.times(0)).save(any());
    }

    @Test
    public void updatePersonWithException()
    {
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(person));

        given(personRepositoryMock.save(person)).willAnswer(invocation -> { throw new Exception();});

        assertThat(personService.updatePerson(person)).isNull();
        verify(personRepositoryMock, Mockito.times(1)).save(person);
    }

    @Test
    public void deletePersonWithNullPerson()
    {
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(null, null)).thenReturn(Optional.empty());

        verify(personRepositoryMock, Mockito.times(0)).deletePersonByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class));
        assertThat(personService.deletePerson(null,null)).isNull();
    }

    @Test
    public void deletePersonWithExistingPerson()
    {
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(person));
        when(personRepositoryMock.deletePersonByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(1);
        assertThat(personService.deletePerson(person.getFirstName(), person.getLastName())).isEqualTo(1);
    }

    @Test
    public void deletePersonWithNonExistingPerson()
    {
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());

        assertThat(personService.deletePerson(person.getFirstName(),person.getLastName())).isEqualTo(null);
        verify(personRepositoryMock, Mockito.times(0)).deletePersonByFirstNameAndLastNameAllIgnoreCase(any(String.class),any(String.class));
    }

    @Test
    public void deletePersonWithException()
    {
        when(personRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(person));
        given(personRepositoryMock.deletePersonByFirstNameAndLastNameAllIgnoreCase(person.getFirstName(),person.getLastName())).willAnswer(invocation -> { throw new Exception();});

        assertThat(personService.deletePerson(person.getFirstName(), person.getLastName())).isNull();
        verify(personRepositoryMock, Mockito.times(1)).deletePersonByFirstNameAndLastNameAllIgnoreCase(person.getFirstName(), person.getLastName());
    }

}
