package com.safetynet.alerts.model;

import com.safetynet.alerts.model.dto.ChildAlertDTO;
import com.safetynet.alerts.model.dto.FamilyMemberDTO;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.model.mapper.ChildAlertDTOMapper;
import com.safetynet.alerts.model.mapper.FamilyMemberDTOMapper;
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
public class ChildAlertDTOMapperTests {

    @SpyBean
    DateUtils dateUtilsSpy;

    @Autowired
    private ChildAlertDTOMapper childAlertDTOMapper;

    @Test
    public void convertToChildAlertDTO_MapsCorrect() {
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

        ChildAlertDTO childAlertDTO = childAlertDTOMapper.convertToChildAlertDTO(person, medicalRecord);

        assertThat(childAlertDTO.getFirstName()).isEqualTo(person.getFirstName());
        assertThat(childAlertDTO.getLastName()).isEqualTo(person.getLastName());
        assertThat(childAlertDTO.getAge()).isEqualTo(Period.between(medicalRecord.getBirthDate(), nowLocalDateMock).getYears());
    }

    @Test
    public void convertToChildAlertDTO_nullPerson() {

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

        ChildAlertDTO childAlertDTO = childAlertDTOMapper.convertToChildAlertDTO(null, medicalRecord);

        assertThat(childAlertDTO.getFirstName()).isNull();
        assertThat(childAlertDTO.getLastName()).isNull();
        assertThat(childAlertDTO.getAge()).isEqualTo(Period.between(medicalRecord.getBirthDate(), nowLocalDateMock).getYears());

    }

    @Test
    public void convertToChildAlertDTO_nullMedicalRecords() {
        Person person = new Person();
        person.setFirstName("personFirstName");
        person.setLastName("personLastName");
        person.setAddress("personAddress");
        person.setPhone("personPhone");
        person.setZip("personZip");
        person.setEmail("personMail");
        person.setCity("personCity");

        LocalDate nowLocalDateMock = LocalDate.of(2010, 12, 31);
        when(dateUtilsSpy.getNowLocalDate()).thenReturn(nowLocalDateMock);

        ChildAlertDTO childAlertDTO = childAlertDTOMapper.convertToChildAlertDTO(person, null);

        assertThat(childAlertDTO.getFirstName()).isEqualTo(person.getFirstName());
        assertThat(childAlertDTO.getLastName()).isEqualTo(person.getLastName());
        assertThat(childAlertDTO.getAge()).isEqualTo(Integer.MIN_VALUE);

    }

    @Test
    public void convertToChildAlertDTO_nullValues() {

        assertThat(childAlertDTOMapper.convertToChildAlertDTO(null,null)).isNull();
    }
}
