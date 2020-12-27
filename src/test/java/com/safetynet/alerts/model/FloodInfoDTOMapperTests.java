package com.safetynet.alerts.model;

import com.safetynet.alerts.model.dto.FloodInfoDTO;
import com.safetynet.alerts.model.mapper.FloodInfoDTOMapper;

import com.safetynet.alerts.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "application.runner.enabled=false" })
public class FloodInfoDTOMapperTests {
    @MockBean
    private DateUtils dateUtils;

    private FloodInfoDTOMapper mapper = Mappers.getMapper(FloodInfoDTOMapper.class);

    @Test
    public void floodInfoDTO_MapsCorrect() {
        Person person = new Person();
        person.setFirstName("personFirstName");
        person.setLastName("personLastName");
        person.setAddress("personAddress");
        person.setPhone("personPhone");
        person.setZip("personZip");
        person.setEmail("personMail");
        person.setCity("personCity");

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setLastName("medicalRecordLastName");
        medicalRecord.setFirstName("medicalRecordFirstNAme");
        medicalRecord.setBirthDate(LocalDate.of(200, 01, 01));
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("Allergie 1");
        allergiesList.add("Allergie 2");

        medicalRecord.setAllergies(allergiesList);

        List<String> medicamentList = new ArrayList<>();
        medicamentList.add("medicament 1");
        medicamentList.add("medicament 2");

        medicalRecord.setMedications(medicamentList);

        LocalDate nowLocalDateMock = LocalDate.of(2020, 12, 31);
        when(dateUtils.getNowLocalDate()).thenReturn(nowLocalDateMock);

        FloodInfoDTO floodInfoDTO = mapper.convertToFloodInfoDTO(person, medicalRecord);

        assertThat(floodInfoDTO.getLastname()).isEqualTo(person.getLastName());
        assertThat(floodInfoDTO.getPhone()).isEqualTo(person.getPhone());
        assertThat(floodInfoDTO.getAllergiesList()).isEqualTo(medicalRecord.getAllergies());
        assertThat(floodInfoDTO.getMedicationList()).isEqualTo(medicalRecord.getMedications());
        assertThat(floodInfoDTO.getAge()).isEqualTo(Period.between(medicalRecord.getBirthDate(), nowLocalDateMock).getYears());

    }

    @Test
    public void floodInfoDTO_NullPerson() {
        Person person = null;

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setLastName("medicalRecordLastName");
        medicalRecord.setFirstName("medicalRecordFirstNAme");
        medicalRecord.setBirthDate(LocalDate.of(200, 01, 01));
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("Allergie 1");
        allergiesList.add("Allergie 2");

        medicalRecord.setAllergies(allergiesList);

        List<String> medicamentList = new ArrayList<>();
        medicamentList.add("medicament 1");
        medicamentList.add("medicament 2");

        medicalRecord.setMedications(medicamentList);

        LocalDate nowLocalDateMock = LocalDate.of(2020, 12, 31);
        when(dateUtils.getNowLocalDate()).thenReturn(nowLocalDateMock);

        FloodInfoDTO floodInfoDTO = mapper.convertToFloodInfoDTO(person, medicalRecord);

        assertThat(floodInfoDTO.getLastname()).isNull();
        assertThat(floodInfoDTO.getPhone()).isNull();
        assertThat(floodInfoDTO.getAllergiesList()).isEqualTo(medicalRecord.getAllergies());
        assertThat(floodInfoDTO.getMedicationList()).isEqualTo(medicalRecord.getMedications());
        assertThat(floodInfoDTO.getAge()).isEqualTo(Period.between(medicalRecord.getBirthDate(), nowLocalDateMock).getYears());

    }

    @Test
    public void floodInfoDTO_NullMedicalRecord() {
        Person person = new Person();
        person.setFirstName("personFirstName");
        person.setLastName("personLastName");
        person.setAddress("personAddress");
        person.setPhone("personPhone");
        person.setZip("personZip");
        person.setEmail("personMail");
        person.setCity("personCity");

        MedicalRecord medicalRecord = null;

        LocalDate nowLocalDateMock = LocalDate.of(2020, 12, 31);
        when(dateUtils.getNowLocalDate()).thenReturn(nowLocalDateMock);

        FloodInfoDTO floodInfoDTO = mapper.convertToFloodInfoDTO(person, medicalRecord);

        assertThat(floodInfoDTO.getLastname()).isEqualTo(person.getLastName());
        assertThat(floodInfoDTO.getPhone()).isEqualTo(person.getPhone());
        assertThat(floodInfoDTO.getAllergiesList()).isEmpty();
        assertThat(floodInfoDTO.getMedicationList()).isEmpty();
        assertThat(floodInfoDTO.getAge()).isEqualTo(0);
    }

    @Test
    public void floodInfoDTO_NullValues() {
        assertThat(mapper.convertToFloodInfoDTO(null, null)).isNull();
    }

}