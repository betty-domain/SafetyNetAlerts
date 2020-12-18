package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.PersonInfo;
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

    @MockBean
    private FireStationRepository fireStationRepositoryMock;

    @MockBean
    private DateUtils dateUtilsMock;

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
        medicalRecord.setBirthDate(LocalDate.of(2000, 5, 14));
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
        LocalDate nowMockLocalDate = LocalDate.of(2020, 12, 31);
        when(dateUtilsMock.getNowLocalDate()).thenReturn(nowMockLocalDate);

        List<PersonInfo> personInfosList = personInfoService.getPersonsInfo(person.getFirstName(), person.getLastName());

        assertThat(personInfosList).size().isEqualTo(3);
        assertThat(isPersonInfoEqualToPerson(personInfosList.get(0), person, medicalRecord)).isTrue();
        assertThat(isPersonInfoEqualToPerson(personInfosList.get(1), secondPerson, secondMedicalRecord)).isTrue();
        assertThat(isPersonInfoEqualToPerson(personInfosList.get(2), thirdPerson, null)).isTrue();
        assertThat(personInfosList.get(0).getAge()).isEqualTo(Period.between(medicalRecord.getBirthDate(), nowMockLocalDate).getYears());
    }

    private boolean isPersonInfoEqualToPerson(PersonInfo personInfoToCompare, Person personToCompare, MedicalRecord medicalRecordToCompare) {
        if (medicalRecordToCompare != null) {
            return personInfoToCompare.getAddress().equalsIgnoreCase(personToCompare.getAddress()) &&
                    personInfoToCompare.getAllergiesList().equals(medicalRecordToCompare.getAllergies()) &&
                    personInfoToCompare.getMail().equalsIgnoreCase(personToCompare.getEmail()) &&
                    personInfoToCompare.getMedicationList().equals(medicalRecordToCompare.getMedications()) &&
                    personInfoToCompare.getLastname().equalsIgnoreCase(personToCompare.getLastName());
        } else {
            return personInfoToCompare.getAddress().equalsIgnoreCase(personToCompare.getAddress()) &&
                    personInfoToCompare.getAllergiesList().equals(new ArrayList<>()) &&
                    personInfoToCompare.getMail().equalsIgnoreCase(personToCompare.getEmail()) &&
                    personInfoToCompare.getMedicationList().equals(new ArrayList<>()) &&
                    personInfoToCompare.getLastname().equalsIgnoreCase(personToCompare.getLastName());
        }
    }

    @Test
    public void getFireStationCommunityWithNullValues() {
        assertThat(personInfoService.getFireStationCommunity(null)).isNull();
        verify(fireStationRepositoryMock, Mockito.times(0)).findDistinctByStation(any(Integer.class));

    }

    @Test
    public void getFireStationCommunityWithValidValues() {

        FireStation fireStation = new FireStation();
        fireStation.setStation(1);
        fireStation.setAddress(person.getAddress());

        List<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        List<Person> personList = new ArrayList<>();
        personList.add(person);

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setBirthDate(LocalDate.of(1910,10,10));

        when(fireStationRepositoryMock.findDistinctByStation(1)).thenReturn(fireStationList);
        when(personRepositoryMock.findAllByAddressInOrderByAddress(any())).thenReturn(personList);
        when(medicalRecordRepositoryMock.findByLastNameAndFirstNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(medicalRecord);

        assertThat(personInfoService.getFireStationCommunity(1).getAdultsCount()).isEqualTo(1);
        assertThat(personInfoService.getFireStationCommunity(1).getChildsCount()).isEqualTo(0);
        assertThat(personInfoService.getFireStationCommunity(1).getCommunityMemberDTOList()).size().isEqualTo(1);

    }

}
