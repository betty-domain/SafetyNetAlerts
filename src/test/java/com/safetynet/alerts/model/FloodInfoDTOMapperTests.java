package com.safetynet.alerts.model;

import com.safetynet.alerts.model.dto.FloodInfoDTO;
import com.safetynet.alerts.model.mapper.FloodInfoDTOMapper;

import com.safetynet.alerts.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class FloodInfoDTOMapperTests {

    @SpyBean
    private DateUtils dateUtilsSpy;

    @Autowired
    private FloodInfoDTOMapper floodInfoDTOMapper;

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
        medicalRecord.setBirthDate(LocalDate.of(2000, 01, 01));
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("Allergie 1");
        allergiesList.add("Allergie 2");

        medicalRecord.setAllergies(allergiesList);

        List<String> medicamentList = new ArrayList<>();
        medicamentList.add("medicament 1");
        medicamentList.add("medicament 2");

        medicalRecord.setMedications(medicamentList);

        LocalDate nowLocalDateMock = LocalDate.of(2010, 12, 31);
        when(dateUtilsSpy.getNowLocalDate()).thenReturn(nowLocalDateMock);

        FloodInfoDTO floodInfoDTO = floodInfoDTOMapper.convertToFloodInfoDTO(person, medicalRecord);

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
        medicalRecord.setBirthDate(LocalDate.of(2000, 01, 01));
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("Allergie 1");
        allergiesList.add("Allergie 2");

        medicalRecord.setAllergies(allergiesList);

        List<String> medicamentList = new ArrayList<>();
        medicamentList.add("medicament 1");
        medicamentList.add("medicament 2");

        medicalRecord.setMedications(medicamentList);

        LocalDate nowLocalDateMock = LocalDate.of(2010, 12, 31);
        when(dateUtilsSpy.getNowLocalDate()).thenReturn(nowLocalDateMock);

        FloodInfoDTO floodInfoDTO = floodInfoDTOMapper.convertToFloodInfoDTO(person, medicalRecord);

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

        LocalDate nowLocalDateMock = LocalDate.of(2010, 12, 31);
        when(dateUtilsSpy.getNowLocalDate()).thenReturn(nowLocalDateMock);

        FloodInfoDTO floodInfoDTO = floodInfoDTOMapper.convertToFloodInfoDTO(person, medicalRecord);

        assertThat(floodInfoDTO.getLastname()).isEqualTo(person.getLastName());
        assertThat(floodInfoDTO.getPhone()).isEqualTo(person.getPhone());
        assertThat(floodInfoDTO.getAllergiesList()).isEmpty();
        assertThat(floodInfoDTO.getMedicationList()).isEmpty();
        assertThat(floodInfoDTO.getAge()).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    public void floodInfoDTO_NullValues() {
        assertThat(floodInfoDTOMapper.convertToFloodInfoDTO(null, null)).isNull();
    }

}
