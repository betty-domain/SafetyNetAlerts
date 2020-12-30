package com.safetynet.alerts.controller;

import com.safetynet.alerts.TestsUtils.TestsUtils;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.service.FireStationService;
import org.junit.jupiter.api.BeforeEach;
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

@WebMvcTest(controllers = FireStationController.class)
public class FireStationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationService fireStationServiceMock;

    private FireStation fireStation;

    @BeforeEach
    public void initTest() {
        fireStation = new FireStation();
        fireStation.setStation(1);
        fireStation.setAddress("myAddress");
        fireStation.setId(10L);
    }

    @Test
    public void getAllFireStationsTests() throws Exception {
        List<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        when(fireStationServiceMock.getAllFireStations()).thenReturn(fireStationList);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/firestations").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isArray());
    }

    @Test
    public void addFireStationValidTests() throws Exception {

        when(fireStationServiceMock.addFireStation(fireStation)).thenReturn(fireStation);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/firestation").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(fireStation));
        ;

        mockMvc.perform(builder).
                andExpect(status().isOk());

    }

    @Test
    public void updateFireStationValidTests() throws Exception {

        when(fireStationServiceMock.updateFireStation(fireStation)).thenReturn(fireStation);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/firestation").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(fireStation));
        ;

        mockMvc.perform(builder).
                andExpect(status().isOk());

    }

    @Test
    public void deleteFireStationByAddressValidTests() throws Exception {

        when(fireStationServiceMock.deleteFireStationByAddress(fireStation.getAddress())).thenReturn(1);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/firestation/" + fireStation.getAddress()).
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isOk());

    }

    @Test
    public void deleteFireStationByAddressAndStationValidTests() throws Exception {

        when(fireStationServiceMock.deleteFireStationByAddressAndStation(fireStation.getAddress(), fireStation.getStation())).thenReturn(1);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/firestation/" + fireStation.getAddress() + "/" +
                fireStation.getStation().toString()).
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isOk());

    }

    @Test
    public void addFireStationWithException() throws Exception {

        when(fireStationServiceMock.addFireStation(any(FireStation.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/firestation").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(fireStation));

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("firestation.insert.error");
                });
    }

    @Test
    public void updateFireStationWithException() throws Exception {

        when(fireStationServiceMock.updateFireStation(any(FireStation.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/firestation").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(fireStation));

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("firestation.update.error");
                });
    }

    @Test
    public void deleteFireStationByAddressWithException() throws Exception {

        when(fireStationServiceMock.deleteFireStationByAddress(any(String.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/firestation/" + fireStation.getAddress()).
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("firestation.delete.byAddress.error");
                });
    }

    @Test
    public void deleteFireStationByStationWithException() throws Exception {

        when(fireStationServiceMock.deleteFireStationByAddressAndStation(any(String.class), any(Integer.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/firestation/" + fireStation.getAddress() + "/" +
                fireStation.getStation().toString()).
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("firestation.delete.byStation.error");
                });
    }

}
