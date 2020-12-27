package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.FireStationCommunityDTO;
import com.safetynet.alerts.model.dto.FloodInfoByStationDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "application.runner.enabled=false" })
public class FireStationCommunityServiceTests {

    @MockBean
    private PersonRepository personRepositoryMock;

    @MockBean
    private MedicalRecordRepository medicalRecordRepositoryMock;

    @MockBean
    private FireStationRepository fireStationRepositoryMock;

    @MockBean
    private DateUtils dateUtilsMock;

    @Autowired
    private FireStationCommunityService fireStationCommunityService;

    private Person person;

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

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getFireStationCommunityWithNullValues() {
        assertThat(fireStationCommunityService.getFireStationCommunity(null)).isNull();
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
        medicalRecord.setBirthDate(LocalDate.of(1910, 10, 10));

        when(fireStationRepositoryMock.findDistinctByStation(1)).thenReturn(fireStationList);
        when(personRepositoryMock.findAllByAddressInOrderByAddress(any())).thenReturn(personList);
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));

        assertThat(fireStationCommunityService.getFireStationCommunity(1).getAdultsCount()).isEqualTo(1);
        assertThat(fireStationCommunityService.getFireStationCommunity(1).getChildrenCount()).isEqualTo(0);
        assertThat(fireStationCommunityService.getFireStationCommunity(1).getCommunityMemberDTOList()).size().isEqualTo(1);

    }

    @Test
    public void getFireStationCommunityWithNullMedicalRecord() {

        FireStation fireStation = new FireStation();
        fireStation.setStation(1);
        fireStation.setAddress(person.getAddress());

        List<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        List<Person> personList = new ArrayList<>();
        personList.add(person);

        when(fireStationRepositoryMock.findDistinctByStation(1)).thenReturn(fireStationList);
        when(personRepositoryMock.findAllByAddressInOrderByAddress(any())).thenReturn(personList);
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());

        FireStationCommunityDTO fireStationCommunityDTO = fireStationCommunityService.getFireStationCommunity(1);

        assertThat(fireStationCommunityDTO.getAdultsCount()).isEqualTo(1);
        assertThat(fireStationCommunityDTO.getChildrenCount()).isEqualTo(0);
        assertThat(fireStationCommunityDTO.getCommunityMemberDTOList()).size().isEqualTo(1);

    }

    @Test
    public void getPhoneListByStationNumberWithNullValues() {
        assertThat(fireStationCommunityService.getPhoneListByStationNumber(null)).isNull();
    }

    @Test
    public void getPhoneListByStationNumberWithException() {
        given(fireStationRepositoryMock.findDistinctByStation(any(Integer.class))).willAnswer(invocation -> {
            throw new Exception();
        });
        assertThat(fireStationCommunityService.getPhoneListByStationNumber(1)).isNull();
    }

    @Test
    public void getPhoneListByStationNumberWithEmptyFireStationList() {
        when(fireStationRepositoryMock.findDistinctByStation(any(Integer.class))).thenReturn(new ArrayList<>());
        when(personRepositoryMock.findAllByAddressInOrderByAddress(anyList())).thenReturn(new ArrayList<>());
        assertThat(fireStationCommunityService.getPhoneListByStationNumber(1)).isEmpty();
    }

    @Test
    public void getPhoneListByStationNumberReturnListOfString() {

        Person secondPerson = new Person();
        secondPerson.setPhone("secondPhone");
        secondPerson.setAddress(person.getAddress());

        Person thirdPerson = new Person();
        thirdPerson.setAddress(person.getAddress());
        List<Person> personList = new ArrayList<>();
        personList.add(person);
        personList.add(secondPerson);
        personList.add(thirdPerson);

        List<FireStation> fireStationList = new ArrayList<>();

        FireStation fireStation = new FireStation();
        fireStation.setStation(1);
        fireStation.setAddress(person.getAddress());
        fireStationList.add(fireStation);

        when(fireStationRepositoryMock.findDistinctByStation(1)).thenReturn(fireStationList);
        when(personRepositoryMock.findAllByAddressInOrderByAddress(anyList())).thenReturn(personList);
        assertThat(fireStationCommunityService.getPhoneListByStationNumber(1)).size().isEqualTo(2);
    }

    @Test
    public void getFloodInfoByStationsWithNullValues() {
        assertThat(fireStationCommunityService.getFloodInfoByStations(null)).isNull();
    }

    @Test
    public void getFloodInfoByStationsWithException() {
        given(fireStationRepositoryMock.findDistinctByStation(any(Integer.class))).willAnswer(invocation -> {
            throw new Exception();
        });
        assertThat(fireStationCommunityService.getFloodInfoByStations(1)).isNull();
    }

    @Test
    public void getFloodInfoByStationsReturnListOfValues() {
        List<FireStation> fireStationList = new ArrayList<>();

        FireStation fireStation = new FireStation();
        fireStation.setStation(1);
        fireStation.setAddress(person.getAddress());
        fireStationList.add(fireStation);

        FireStation fireStation1 = new FireStation();
        fireStation1.setStation(1);
        fireStation1.setAddress("fs1.address");
        fireStationList.add(fireStation1);

        List<Person> personList = new ArrayList<>();
        personList.add(person);

        Person person1 = new Person();
        person1.setFirstName("firstname1");
        person1.setLastName("lastname1");
        person1.setAddress(person.getAddress());
        personList.add(person1);

        Person person2 = new Person();
        person2.setFirstName("firstname2");
        person2.setLastName("lastname2");
        person2.setAddress(fireStation1.getAddress());
        personList.add(person2);

        MedicalRecord medicalRecord = new MedicalRecord();
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("allergie1");
        allergiesList.add("allergie2");
        medicalRecord.setAllergies(allergiesList);
        List<String> medicationsList = new ArrayList<>();
        medicationsList.add("medication");
        medicalRecord.setMedications(medicationsList);
        medicalRecord.setBirthDate(LocalDate.of(1910, 10, 10));

        when(fireStationRepositoryMock.findDistinctByStation(1)).thenReturn(fireStationList);
        when(personRepositoryMock.findAllByAddressInOrderByAddress(anyList())).thenReturn(personList);
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));

        List<FloodInfoByStationDTO> floodInfoByStationDTOList = fireStationCommunityService.getFloodInfoByStations(1);
        assertThat(floodInfoByStationDTOList.size()).isEqualTo(2);
        assertThat(floodInfoByStationDTOList.get(0).getFloodInfoDTOList().size()).isEqualTo(2);
        assertThat(floodInfoByStationDTOList.get(1).getFloodInfoDTOList().size()).isEqualTo(1);

    }
}
