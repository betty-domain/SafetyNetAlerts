package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FireStationController.class, properties = {
        "application.runner.enabled=false" })
public class FireStationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationService fireStationServiceMock;

    public void getPersonByFireStationWithException()
    {


    }

}
