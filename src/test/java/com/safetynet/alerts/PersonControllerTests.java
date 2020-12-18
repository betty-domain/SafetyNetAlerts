package com.safetynet.alerts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.PersonController;
import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonController.class, properties = {
        "application.runner.enabled=false" })
public class PersonControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personServiceMock;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAllPersonsTest() throws Exception {

        Person person = new Person();
        person.setLastName("myLastName");
        person.setFirstName("myFirstName");
        person.setAddress("myAddress");
        person.setCity("myCity");
        person.setPhone("myPhone");
        person.setEmail("myEmail");
        person.setZip("myZip");
        List<Person> personList = new ArrayList<>();

        when(personServiceMock.getAllPersons()).thenReturn(personList);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/persons").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isArray());
    }

    @Test
    public void addPersonValidTest() throws Exception {
        Person person = new Person();
        person.setLastName("myLastName");
        person.setFirstName("myFirstName");
        person.setAddress("myAddress");
        person.setCity("myCity");
        person.setPhone("myPhone");
        person.setEmail("myEmail");
        person.setZip("myZip");

        when(personServiceMock.addPerson(person)).thenReturn(person);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/person").
                contentType(MediaType.APPLICATION_JSON).content(asJsonString(person));

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void addPersonWithException() throws Exception {

        Person person = new Person();
        person.setLastName("myLastName");
        person.setFirstName("myFirstName");
        person.setAddress("myAddress");
        person.setCity("myCity");
        person.setPhone("myPhone");
        person.setEmail("myEmail");
        person.setZip("myZip");

        when(personServiceMock.addPerson(any(Person.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/person").
                contentType(MediaType.APPLICATION_JSON).content(asJsonString(person));

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("person.insert.error");
                });
    }

    @Test
    public void updatePersonValidTest() throws Exception {
        Person person = new Person();
        person.setLastName("myLastName");
        person.setFirstName("myFirstName");
        person.setAddress("myAddress");
        person.setCity("myCity");
        person.setPhone("myPhone");
        person.setEmail("myEmail");
        person.setZip("myZip");

        when(personServiceMock.updatePerson(person)).thenReturn(person);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/person").
                contentType(MediaType.APPLICATION_JSON).content(asJsonString(person));

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void updatePersonWithException() throws Exception {

        Person person = new Person();
        person.setLastName("myLastName");
        person.setFirstName("myFirstName");
        person.setAddress("myAddress");
        person.setCity("myCity");
        person.setPhone("myPhone");
        person.setEmail("myEmail");
        person.setZip("myZip");

        when(personServiceMock.updatePerson(any(Person.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/person").
                contentType(MediaType.APPLICATION_JSON).content(asJsonString(person));

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("person.update.error");
                });
    }

    @Test
    public void deletePersonValidTest() throws Exception {
        Person person = new Person();
        person.setLastName("myLastName");
        person.setFirstName("myFirstName");
        person.setAddress("myAddress");
        person.setCity("myCity");
        person.setPhone("myPhone");
        person.setEmail("myEmail");
        person.setZip("myZip");

        when(personServiceMock.deletePerson(person.getFirstName(), person.getLastName())).thenReturn(1);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/person").
                contentType(MediaType.APPLICATION_JSON).
                param("firstname",person.getFirstName()).
                param("lastname",person.getLastName());

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void deletePersonWithException() throws Exception {

        Person person = new Person();
        person.setLastName("myLastName");
        person.setFirstName("myFirstName");
        person.setAddress("myAddress");
        person.setCity("myCity");
        person.setPhone("myPhone");
        person.setEmail("myEmail");
        person.setZip("myZip");

        when(personServiceMock.deletePerson(any(String.class),any(String.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/person").
                contentType(MediaType.APPLICATION_JSON).
                param("firstname",person.getFirstName()).
                param("lastname",person.getLastName());

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("person.delete.error");
                });
    }
}
