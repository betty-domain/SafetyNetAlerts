package com.safetynet.alerts.model;

import com.safetynet.alerts.model.dto.FireDTO;
import com.safetynet.alerts.model.mapper.FireDTOMapper;
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
public class FireDTOMapperTests {

    @SpyBean
    private DateUtils dateUtilsSpy;

    @Autowired
    private FireDTOMapper fireDTOMapper;

    @Test
    public void fireDTO_MapsCorrect() {
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

        FireDTO fireDTO = fireDTOMapper.convertToFireDTO(person, medicalRecord);

        assertThat(fireDTO.getLastname()).isEqualTo(person.getLastName());
        assertThat(fireDTO.getPhone()).isEqualTo(person.getPhone());
        assertThat(fireDTO.getAllergiesList()).isEqualTo(medicalRecord.getAllergies());
        assertThat(fireDTO.getMedicationList()).isEqualTo(medicalRecord.getMedications());
        assertThat(fireDTO.getAge()).isEqualTo(Period.between(medicalRecord.getBirthDate(), nowLocalDateMock).getYears());
        assertThat(fireDTO.getFireStationNumberList()).isEmpty();

    }

    @Test
    public void fireDTO_NullPerson() {

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

        FireDTO fireDTO = fireDTOMapper.convertToFireDTO(null, medicalRecord);

        assertThat(fireDTO.getLastname()).isNull();
        assertThat(fireDTO.getPhone()).isNull();
        assertThat(fireDTO.getAllergiesList()).isEqualTo(medicalRecord.getAllergies());
        assertThat(fireDTO.getMedicationList()).isEqualTo(medicalRecord.getMedications());
        assertThat(fireDTO.getAge()).isEqualTo(Period.between(medicalRecord.getBirthDate(), nowLocalDateMock).getYears());
        assertThat(fireDTO.getFireStationNumberList()).isEmpty();
    }

    @Test
    public void fireDTO_NullMedicalRecord() {
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

        FireDTO fireDTO = fireDTOMapper.convertToFireDTO(person, medicalRecord);

        assertThat(fireDTO.getLastname()).isEqualTo(person.getLastName());
        assertThat(fireDTO.getPhone()).isEqualTo(person.getPhone());
        assertThat(fireDTO.getAllergiesList()).isEmpty();
        assertThat(fireDTO.getMedicationList()).isEmpty();
        assertThat(fireDTO.getAge()).isEqualTo(Integer.MIN_VALUE);
        assertThat(fireDTO.getFireStationNumberList()).isEmpty();
    }



    @Test
    public void fireDTO_NullValues() {
        assertThat(fireDTOMapper.convertToFireDTO(null, null)).isNull();
    }

}
