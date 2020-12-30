package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.model.dto.ChildAlertDTO;
import com.safetynet.alerts.model.dto.FamilyMemberDTO;
import com.safetynet.alerts.service.ChildAlertService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChildAlertController.class)
public class ChildAlertControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChildAlertService childAlertService;

    @Test
    public void getChildAlertListValidTest() throws Exception {

        ChildAlertDTO childAlertDTO = new ChildAlertDTO();
        childAlertDTO.setLastName("myLastName");
        childAlertDTO.setFirstName("myFirstName");
        childAlertDTO.setAge(5);

        FamilyMemberDTO familyMemberDTO = new FamilyMemberDTO();
        familyMemberDTO.setFirstName("fmFN1");
        familyMemberDTO.setLastName("fmLN1");

        FamilyMemberDTO familyMemberDTO1 = new FamilyMemberDTO();
        familyMemberDTO1.setLastName("fmLN2");
        familyMemberDTO1.setFirstName("fmFN2");

        List<FamilyMemberDTO> familyMemberDTOList = new ArrayList<>();
        familyMemberDTOList.add(familyMemberDTO);
        familyMemberDTOList.add(familyMemberDTO1);

        childAlertDTO.setFamilyMembers(familyMemberDTOList);

        ChildAlertDTO childAlertDTO1 = new ChildAlertDTO();
        childAlertDTO1.setFirstName("firstNameChild2");
        childAlertDTO1.setLastName("lastnameChild2");
        childAlertDTO.setAge(15);

        List<ChildAlertDTO> childAlertDTOList = new ArrayList<>();
        childAlertDTOList.add(childAlertDTO);
        childAlertDTOList.add(childAlertDTO1);

        when(childAlertService.getChildAlertDTOListFromAddress("address")).thenReturn(childAlertDTOList);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/childAlert").
                param("address","address").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isArray());

        //TODO : voir comment mieux vérifier la sortie en json
    }


    @Test
    public void getPersonsInfoWithException() throws Exception {

        when(childAlertService.getChildAlertDTOListFromAddress(anyString())).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/childAlert").
                param("address","address").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("childAlert.get.error");
                });
    }
}
