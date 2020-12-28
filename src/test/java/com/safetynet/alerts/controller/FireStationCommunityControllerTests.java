package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.model.dto.FireStationCommunityDTO;
import com.safetynet.alerts.model.dto.StationFloodInfoDTO;
import com.safetynet.alerts.service.FireStationCommunityService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = FireStationCommunityController.class,
        properties = { "application.runner.enabled=false" })
public class FireStationCommunityControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationCommunityService fireStationCommunityServiceMock;

    @Test
    public void getFireStationCommunityWithException() throws Exception{

        when(fireStationCommunityServiceMock.getFireStationCommunity(any(Integer.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/fireStation").
                param("stationNumber","1").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("fireStationCommunity.getFireStationCommunity.error");
                });
    }

    @Test
    public void getFireStationCommunityValidTest() throws Exception{

        FireStationCommunityDTO fireStationCommunityDTO = new FireStationCommunityDTO();

        when(fireStationCommunityServiceMock.getFireStationCommunity(any(Integer.class))).thenReturn(fireStationCommunityDTO);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/fireStation").
                param("stationNumber","1").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void getPhoneListByFireStationWithException() throws Exception{

        when(fireStationCommunityServiceMock.getPhoneListByStationNumber(any(Integer.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/phoneAlert").
                param("firestation","1").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("phoneAlert.get.error");
                });
    }

    @Test
    public void getPhoneListByFireStationValidTest() throws Exception{

        List<String> phoneList = new ArrayList<>();
        phoneList.add("phone1");
        phoneList.add("phone2");

        when(fireStationCommunityServiceMock.getPhoneListByStationNumber(1)).thenReturn(phoneList);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/phoneAlert").
                param("firestation","1").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void getFloodInfoByFireStationWithException() throws Exception{

        when(fireStationCommunityServiceMock.getFloodInfoByStations(any(Integer.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/flood/stations").
                param("stations","1").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("flood.get.error");
                });
    }

    @Test
    public void getFloodInfoByFireStationValidTest() throws Exception{

        List<StationFloodInfoDTO> stationFloodInfoDTOList = new ArrayList<>();

        when(fireStationCommunityServiceMock.getFloodInfoByStations(1)).thenReturn(stationFloodInfoDTOList);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/flood/stations").
                param("stations","1").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }
}
