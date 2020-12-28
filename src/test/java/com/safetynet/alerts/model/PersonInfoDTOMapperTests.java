package com.safetynet.alerts.model;

import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.model.mapper.PersonInfoDTOMapper;
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

@SpringBootTest(properties = {
        "application.runner.enabled=false" })
public class PersonInfoDTOMapperTests {

    @SpyBean
    private DateUtils dateUtilsSpy;

    @Autowired
    private PersonInfoDTOMapper personInfoDTOMapper;

    @Test
    public void personInfoDTO_MapsCorrect() {
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

        PersonInfoDTO personInfoDTO = personInfoDTOMapper.personToPersonInfoDTO(person, medicalRecord);

        assertThat(personInfoDTO.getAddress()).isEqualTo(person.getAddress());
        assertThat(personInfoDTO.getLastname()).isEqualTo(person.getLastName());
        assertThat(personInfoDTO.getMail()).isEqualTo(person.getEmail());
        assertThat(personInfoDTO.getAllergiesList()).isEqualTo(medicalRecord.getAllergies());
        assertThat(personInfoDTO.getMedicationList()).isEqualTo(medicalRecord.getMedications());
        assertThat(personInfoDTO.getAge()).isEqualTo(Period.between(medicalRecord.getBirthDate(), nowLocalDateMock).getYears());

    }

    @Test
    public void personInfoDTO_NullPerson() {
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

        PersonInfoDTO personInfoDTO = personInfoDTOMapper.personToPersonInfoDTO(person, medicalRecord);

        assertThat(personInfoDTO.getAddress()).isNull();
        assertThat(personInfoDTO.getLastname()).isNull();
        assertThat(personInfoDTO.getMail()).isNull();
        assertThat(personInfoDTO.getAllergiesList()).isEqualTo(medicalRecord.getAllergies());
        assertThat(personInfoDTO.getMedicationList()).isEqualTo(medicalRecord.getMedications());
        assertThat(personInfoDTO.getAge()).isEqualTo(Period.between(medicalRecord.getBirthDate(), nowLocalDateMock).getYears());

    }

    @Test
    public void personInfoDTO_NullMedicalRecord() {
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

        PersonInfoDTO personInfoDTO = personInfoDTOMapper.personToPersonInfoDTO(person, medicalRecord);

        assertThat(personInfoDTO.getAddress()).isEqualTo(person.getAddress());
        assertThat(personInfoDTO.getLastname()).isEqualTo(person.getLastName());
        assertThat(personInfoDTO.getMail()).isEqualTo(person.getEmail());
        assertThat(personInfoDTO.getAllergiesList()).isEmpty();
        assertThat(personInfoDTO.getMedicationList()).isEmpty();
        assertThat(personInfoDTO.getAge()).isEqualTo(Integer.MIN_VALUE);

    }

    @Test
    public void personInfoDTO_NullValues() {
        assertThat(personInfoDTOMapper.personToPersonInfoDTO(null, null)).isNull();
    }

}
