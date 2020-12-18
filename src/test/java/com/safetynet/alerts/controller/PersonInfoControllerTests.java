package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.PersonInfo;
import com.safetynet.alerts.service.PersonInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonInfoController.class,
        properties = { "application.runner.enabled=false" })
public class PersonInfoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonInfoService personInfoServiceMock;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getPersonsInfoValidTest() throws Exception {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setNom("myName");
        personInfo.setAdresse("myAddress");
        personInfo.setMail("myMail");
        personInfo.setAllergies(new ArrayList<>());
        personInfo.setDosageMedicaments(new ArrayList<>());
        personInfo.setAge(15);
        List<PersonInfo> personInfoList = new ArrayList<>();
        personInfoList.add(personInfo);

        when(personInfoServiceMock.getPersonsInfo("firstname", "lastname")).thenReturn(personInfoList);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/personInfo").
                param("firstname","firstname").
                param("lastname","lastname").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isArray());
    }

    @Test
    public void getPersonsInfoWithException() throws Exception {

        when(personInfoServiceMock.getPersonsInfo(any(String.class), any(String.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/personInfo").
                param("firstname","firstname").
                param("lastname","lastname").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("personInfo.get.error");
                });
    }
}
