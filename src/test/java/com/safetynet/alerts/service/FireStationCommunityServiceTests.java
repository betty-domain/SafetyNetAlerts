package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.CommunityMemberDTO;
import com.safetynet.alerts.model.dto.FireDTO;
import com.safetynet.alerts.model.dto.FireStationCommunityDTO;
import com.safetynet.alerts.model.dto.StationFloodInfoDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class FireStationCommunityServiceTests {

    @MockBean
    private PersonRepository personRepositoryMock;

    @MockBean
    private MedicalRecordRepository medicalRecordRepositoryMock;

    @MockBean
    private FireStationRepository fireStationRepositoryMock;

    @SpyBean
    private DateUtils dateUtilsSpy;

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

        //given une liste de 3 personnes habitant sur 2 adresses différentes
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
        person2.setAddress("adresse2");
        personList.add(person2);
        when(personRepositoryMock.findAllByAddressInOrderByAddress(any())).thenReturn(personList);

        //given 2 casernes de pompiers sur le même numéro mais pas la même adresse
        List<FireStation> fireStationList = new ArrayList<>();
        FireStation fireStation = new FireStation();
        fireStation.setStation(1);
        fireStation.setAddress(person.getAddress());
        fireStationList.add(fireStation);

        FireStation fireStation1 = new FireStation();
        fireStation1.setStation(1);
        fireStation1.setAddress(person2.getAddress());
        fireStationList.add(fireStation1);
        when(fireStationRepositoryMock.findDistinctByStation(1)).thenReturn(fireStationList);

        //given un dossier médical retourné pour la personne person et person2
        LocalDate birthDate = LocalDate.of(1910, 10, 10);

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setBirthDate(birthDate);
        when(dateUtilsSpy.getNowLocalDate()).thenReturn(birthDate.plusYears(5));
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));

        //pour la person1, on ne retourne pas de dossier médical
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(person1.getFirstName(), person1.getLastName())).thenReturn(Optional.empty());

        FireStationCommunityDTO fireStationCommunityDTO = fireStationCommunityService.getFireStationCommunity(1);

        assertThat(fireStationCommunityDTO.getAdultsCount()).isEqualTo(0);
        assertThat(fireStationCommunityDTO.getChildrenCount()).isEqualTo(2);
        assertThat(fireStationCommunityDTO.getCommunityMemberDTOList()).size().isEqualTo(3);

        //on vérifie le DTO de l'objet Person
        CommunityMemberDTO communityMemberDTOPerson = fireStationCommunityDTO.getCommunityMemberDTOList().stream().filter(
                communityMemberDTO -> communityMemberDTO.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                        communityMemberDTO.getLastName().equalsIgnoreCase(person.getLastName())).findFirst().get();
        assertThat(communityMemberDTOPerson.getAge()).isEqualTo(5);

        //on vérifie le DTO de l'objet Person1
        CommunityMemberDTO communityMemberDTOPerson1 = fireStationCommunityDTO.getCommunityMemberDTOList().stream().filter(
                communityMemberDTO -> communityMemberDTO.getFirstName().equalsIgnoreCase(person1.getFirstName()) &&
                        communityMemberDTO.getLastName().equalsIgnoreCase(person1.getLastName())).findFirst().get();
        assertThat(communityMemberDTOPerson1.getAge()).isEqualTo(Integer.MIN_VALUE);

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

        assertThat(fireStationCommunityDTO.getAdultsCount()).isEqualTo(0);
        assertThat(fireStationCommunityDTO.getChildrenCount()).isEqualTo(0);
        assertThat(fireStationCommunityDTO.getCommunityMemberDTOList()).size().isEqualTo(1);
        assertThat(fireStationCommunityDTO.getCommunityMemberDTOList().get(0).getAge()).isEqualTo(Integer.MIN_VALUE);

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
        given(fireStationRepositoryMock.findDistinctByStationIn(anyList())).willAnswer(invocation -> {
            throw new Exception();
        });

        List<Integer> stationsList = new ArrayList<>();
        stationsList.add(1);
        assertThat(fireStationCommunityService.getFloodInfoByStations(stationsList)).isNull();
    }

    @Test
    public void getFloodInfoByStationsReturnListOfValues() {
        List<FireStation> fireStationList = new ArrayList<>();
        List<Person> personList = new ArrayList<>();
        List<Integer> stationsNumberList = new ArrayList<>();

        personList.add(person);

        //given une station de feu avec 2 personnes rattachée
        FireStation fireStation = new FireStation();
        fireStation.setStation(1);
        fireStation.setAddress(person.getAddress());
        fireStationList.add(fireStation);
        stationsNumberList.add(fireStation.getStation());

        Person person1 = new Person();
        person1.setFirstName("firstname1");
        person1.setLastName("lastname1");
        person1.setAddress(person.getAddress());
        personList.add(person1);

        //given une deuxième station de feu avec 1 personne rattachée
        FireStation fireStation1 = new FireStation();
        fireStation1.setStation(2);
        fireStation1.setAddress("fs1.address");
        fireStationList.add(fireStation1);
        stationsNumberList.add(fireStation1.getStation());

        Person person2 = new Person();
        person2.setFirstName("firstname2");
        person2.setLastName("lastname2");
        person2.setAddress(fireStation1.getAddress());
        personList.add(person2);

        //given une troisième station de feu sans personne rattachée à la même adresse
        FireStation fireStation2 = new FireStation();
        fireStation2.setStation(2);
        fireStation2.setAddress("fs2.address");
        fireStationList.add(fireStation2);
        stationsNumberList.add(fireStation2.getStation());

        MedicalRecord medicalRecord = new MedicalRecord();
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("allergie1");
        allergiesList.add("allergie2");
        medicalRecord.setAllergies(allergiesList);
        List<String> medicationsList = new ArrayList<>();
        medicationsList.add("medication");
        medicalRecord.setMedications(medicationsList);
        medicalRecord.setBirthDate(LocalDate.of(1910, 10, 10));

        when(fireStationRepositoryMock.findDistinctByStationIn(anyList())).thenReturn(fireStationList);
        when(personRepositoryMock.findAllByAddressInOrderByAddress(anyList())).thenReturn(personList);

        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(person2.getFirstName(), person2.getLastName())).thenReturn(Optional.empty());

        List<StationFloodInfoDTO> stationFloodInfoDTOList = fireStationCommunityService.getFloodInfoByStations(stationsNumberList);
        assertThat(stationFloodInfoDTOList.size()).isEqualTo(3);

        //on récupère le DTO correspondant à l'adresse de la caserne de pompier Firestation
        Optional<StationFloodInfoDTO> stationFloodInfoDTOFireStation = stationFloodInfoDTOList.stream().filter(
                stationFloodInfoDTO -> stationFloodInfoDTO.getAddress().equalsIgnoreCase(fireStation.getAddress())
        ).findFirst();

        // on vérifie qu'il contient bien 2 personnes avec les allergies et médicaments renseignés dans le bouhcon du dossier médical retourné pour toute personne
        assertThat(stationFloodInfoDTOFireStation.get().getFloodInfoDTOList().size()).isEqualTo(2);
        assertThat(stationFloodInfoDTOFireStation.get().getFloodInfoDTOList().get(0).getAllergiesList()).isEqualTo(medicalRecord.getAllergies());
        assertThat(stationFloodInfoDTOFireStation.get().getFloodInfoDTOList().get(0).getMedicationList()).isEqualTo(medicalRecord.getMedications());

        //on récupère le DTO correspondant à l'adresse de la caserne de pompier Firestation1
        Optional<StationFloodInfoDTO> stationFloodInfoDTOFireStation1 = stationFloodInfoDTOList.stream().filter(
                stationFloodInfoDTO -> stationFloodInfoDTO.getAddress().equalsIgnoreCase(fireStation1.getAddress())
        ).findFirst();
        // on vérifie qu'il contient bien 1 personne avec les allergies et médicaments vides car pas de dossier médical dans le bouhcon du dossier médical retourné pour cette personne
        assertThat(stationFloodInfoDTOFireStation1.get().getFloodInfoDTOList().size()).isEqualTo(1);
        assertThat(stationFloodInfoDTOFireStation1.get().getFloodInfoDTOList().get(0).getAllergiesList()).isEmpty();
        assertThat(stationFloodInfoDTOFireStation1.get().getFloodInfoDTOList().get(0).getMedicationList()).isEmpty();
    }

    @Test
    public void getFireByAddressWithNullValues() {
        assertThat(fireStationCommunityService.getFireInfoByAddress(null)).isNull();
    }

    @Test
    public void getFireByAddressWithException() {
        given(fireStationRepositoryMock.findDistinctByAddressIgnoreCase(anyString())).willAnswer(invocation -> {
            throw new Exception();
        });

        assertThat(fireStationCommunityService.getFireInfoByAddress("myAddress")).isNull();
    }

    @Test
    public void getFireByAddressReturnListOfValues() {
        List<FireStation> fireStationList = new ArrayList<>();
        List<Person> personList = new ArrayList<>();

        //given le dossier médical retourné par défaut pour l'ensemble des personnes
        MedicalRecord medicalRecord = new MedicalRecord();
        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("allergie1");
        allergiesList.add("allergie2");
        medicalRecord.setAllergies(allergiesList);
        List<String> medicationsList = new ArrayList<>();
        medicationsList.add("medication");
        medicalRecord.setMedications(medicationsList);
        medicalRecord.setBirthDate(LocalDate.of(1910, 10, 10));
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));

        //given 2 casernes de pompiers à la même adresse
        FireStation fireStation = new FireStation();
        fireStation.setStation(1);
        fireStation.setAddress(person.getAddress());
        fireStationList.add(fireStation);

        FireStation fireStation1 = new FireStation();
        fireStation1.setStation(2);
        fireStation1.setAddress(person.getAddress());
        fireStationList.add(fireStation1);
        when(fireStationRepositoryMock.findDistinctByAddressIgnoreCase(anyString())).thenReturn(fireStationList);

        //given 3 habitants à cette adresse dont une personne sans dossier médical
        personList.add(person);

        Person person1 = new Person();
        person1.setFirstName("firstname1");
        person1.setLastName("lastname1");
        person1.setAddress(person.getAddress());
        personList.add(person1);


        Person person2 = new Person();
        person2.setFirstName("firstname2");
        person2.setLastName("lastname2");
        person2.setAddress(person.getAddress());
        personList.add(person2);
        //pas de dossier médical pour cette personne
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(person2.getFirstName(), person2.getLastName())).thenReturn(Optional.empty());

        when(personRepositoryMock.findAllByAddressIgnoreCase(anyString())).thenReturn(personList);

        List<FireDTO> fireDTOList = fireStationCommunityService.getFireInfoByAddress("myAddress");
        assertThat(fireDTOList.size()).isEqualTo(3);

        FireDTO fireDTOPerson = fireDTOList.stream().filter(fireDTO -> fireDTO.getLastname().equalsIgnoreCase(person.getLastName())).findFirst().get();

        assertThat(fireDTOPerson.getFireStationNumberList().size()).isEqualTo(2);
        assertThat(fireDTOPerson.getAllergiesList()).isEqualTo(medicalRecord.getAllergies());
        assertThat(fireDTOPerson.getMedicationList()).isEqualTo(medicalRecord.getMedications());

        FireDTO fireDTOPerson2 = fireDTOList.stream().filter(fireDTO -> fireDTO.getLastname().equalsIgnoreCase(person2.getLastName())).findFirst().get();
        assertThat(fireDTOPerson2.getFireStationNumberList().size()).isEqualTo(2);
        assertThat(fireDTOPerson2.getAllergiesList()).isEmpty();
        assertThat(fireDTOPerson2.getMedicationList()).isEmpty();
        assertThat(fireDTOPerson2.getAge()).isEqualTo(Integer.MIN_VALUE);

    }

}
