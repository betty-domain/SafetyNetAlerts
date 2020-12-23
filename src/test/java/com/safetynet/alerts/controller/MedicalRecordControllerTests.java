package com.safetynet.alerts.controller;

import com.safetynet.alerts.TestsUtils.TestsUtils;
import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class, properties = {
        "application.runner.enabled=false" })
public class MedicalRecordControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    private MedicalRecord medicalRecord;

    @BeforeEach
    private void initTests()
    {
        medicalRecord = new MedicalRecord();
        medicalRecord.setLastName("myLastName");
        medicalRecord.setFirstName("myFirstName");
        medicalRecord.setBirthDate(LocalDate.of(2000,10,15));

        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("Allergie 1");
        allergiesList.add("Allergie 2");

        medicalRecord.setAllergies(allergiesList);

        List<String> medicationList = new ArrayList<>();
        medicationList.add("medicament 1");
        medicationList.add("medicament 2");
        medicalRecord.setMedications(medicationList);
    }

    @Test
    public void getAllMedicalRecordTest() throws Exception {

        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        medicalRecordList.add(medicalRecord);

        when(medicalRecordService.getAllMedicalRecords()).thenReturn(medicalRecordList);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/medicalrecords").
                contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isArray());
    }

    @Test
    public void addMedicalRecordValidTest() throws Exception {

        when(medicalRecordService.addMedicalRecord(medicalRecord)).thenReturn(medicalRecord);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/medicalrecord").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(medicalRecord));

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void addMedicalRecordWithException() throws Exception {

        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/medicalrecord").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(medicalRecord));

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("medicalRecord.insert.error");
                });
    }

    @Test
    public void updateMedicalRecordValidTest() throws Exception {

        when(medicalRecordService.updateMedicalRecord(medicalRecord)).thenReturn(medicalRecord);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/medicalrecord").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(medicalRecord));

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void updateMedicalRecordWithException() throws Exception {

        when(medicalRecordService.updateMedicalRecord(any(MedicalRecord.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/medicalrecord").
                contentType(MediaType.APPLICATION_JSON).content(TestsUtils.asJsonString(medicalRecord));

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("medicalRecord.update.error");
                });
    }

    @Test
    public void deleteMedicalRecordValidTest() throws Exception {

        when(medicalRecordService.deleteMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName())).thenReturn(1);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/medicalrecord").
                contentType(MediaType.APPLICATION_JSON).
                param("firstname",medicalRecord.getFirstName()).
                param("lastname",medicalRecord.getLastName());

        mockMvc.perform(builder).
                andExpect(status().isOk());
    }

    @Test
    public void deleteMedicalRecordWithException() throws Exception {

        when(medicalRecordService.deleteMedicalRecord(any(String.class),any(String.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/medicalrecord").
                contentType(MediaType.APPLICATION_JSON).
                param("firstname",medicalRecord.getFirstName()).
                param("lastname",medicalRecord.getLastName());

        mockMvc.perform(builder).
                andExpect(status().isBadRequest()).
                andExpect(mvcResult ->
                {
                    assertThat(mvcResult.getResolvedException()).isInstanceOf(FunctionalException.class);
                    assertThat(mvcResult.getResolvedException().getMessage()).isEqualTo("medicalRecord.delete.error");
                });
    }
}
