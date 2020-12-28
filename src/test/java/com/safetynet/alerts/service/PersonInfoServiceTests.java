package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "application.runner.enabled=false" })
public class PersonInfoServiceTests {

    @MockBean
    private PersonRepository personRepositoryMock;

    @MockBean
    private MedicalRecordRepository medicalRecordRepositoryMock;

    @SpyBean
    private DateUtils dateUtilsSpy;

    @Autowired
    private PersonInfoService personInfoService;

    private Person person;

    private List<Person> personList;

    private List<MedicalRecord> medicalRecordList;

    @BeforeAll
    private static void setUpAllTest() {

    }

    @BeforeEach
    private void setUpEachTest() {
        person = new Person();
        person.setZip("12345");
        person.setEmail("myEmail");
        person.setPhone("myPhone");
        person.setCity("myCity");
        person.setAddress("myAddress");
        person.setFirstName("firstName");
        person.setLastName("lastName");

    }

    @Test
    public void getPersonsInfoWithNullValues() {
        assertThat(personInfoService.getPersonsInfo(null, null)).isNull();
        verify(personRepositoryMock, Mockito.times(0)).findAllByLastNameAllIgnoreCase(any(String.class));
        verify(medicalRecordRepositoryMock, Mockito.times(0)).findAllByLastNameAllIgnoreCase(any(String.class));
    }

    @Test
    public void getPersonsInfoMappingWithValidList() {

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName(person.getFirstName());
        medicalRecord.setLastName(person.getLastName());

        LocalDate birthDate = LocalDate.of(2000, 5, 14);

        medicalRecord.setBirthDate(birthDate);
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("Allergie 1");
        allergiesList.add("Allergie 2");

        medicalRecord.setAllergies(allergiesList);

        List<String> medicamentList = new ArrayList<>();
        medicamentList.add("medicament 1");
        medicamentList.add("medicament 2");

        medicalRecord.setMedications(medicamentList);

        Person secondPerson = new Person();
        secondPerson.setZip("");
        secondPerson.setEmail("");
        secondPerson.setPhone("");
        secondPerson.setCity("");
        secondPerson.setAddress("");
        secondPerson.setFirstName("second Person firstname");
        secondPerson.setLastName(person.getLastName());

        MedicalRecord secondMedicalRecord = new MedicalRecord();
        secondMedicalRecord.setFirstName(secondPerson.getFirstName());
        secondMedicalRecord.setLastName(secondPerson.getLastName());
        secondMedicalRecord.setBirthDate(LocalDate.of(1970, 12, 31));
        List<String> secondAllergiesList = new ArrayList<>();
        secondAllergiesList.add("Second Allergie 1");

        secondMedicalRecord.setAllergies(secondAllergiesList);
        secondMedicalRecord.setMedications(new ArrayList<>());

        Person thirdPerson = new Person();
        thirdPerson.setZip("");
        thirdPerson.setEmail("");
        thirdPerson.setPhone("");
        thirdPerson.setCity("");
        thirdPerson.setAddress("");
        thirdPerson.setFirstName("third Person firstname");
        thirdPerson.setLastName(person.getLastName());

        personList = new ArrayList<>();
        personList.add(person);
        personList.add(secondPerson);
        personList.add(thirdPerson);

        medicalRecordList = new ArrayList<>();
        medicalRecordList.add(medicalRecord);
        medicalRecordList.add(secondMedicalRecord);

        when(personRepositoryMock.findAllByLastNameAllIgnoreCase(person.getLastName())).thenReturn(personList);
        when(medicalRecordRepositoryMock.findAllByLastNameAllIgnoreCase(person.getLastName())).thenReturn(medicalRecordList);
        LocalDate nowMockLocalDate = LocalDate.of(2010, 12, 31);
        when(dateUtilsSpy.getNowLocalDate()).thenReturn(nowMockLocalDate);

        List<PersonInfoDTO> personInfosListDTO = personInfoService.getPersonsInfo(person.getFirstName(), person.getLastName());

        assertThat(personInfosListDTO).size().isEqualTo(3);
        }

}
