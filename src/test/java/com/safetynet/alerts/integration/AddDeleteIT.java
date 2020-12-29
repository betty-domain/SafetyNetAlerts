package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.MedicalRecordService;
import com.safetynet.alerts.service.PersonService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = {
        "application.runner.enabled=true",
        "spring.datasource.url=jdbc:h2:mem:testSafetyNetAlertDeleteIT" })
public class AddDeleteIT {

    @Autowired
    private PersonService personService;

    @Autowired
    private FireStationService fireStationService;

    @Autowired
    FireStationRepository fireStationRepository;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Test
    public void deletePersonIT() {
        assertThat(personService.deletePerson("John", "Boyd")).isEqualTo(1);
    }

    @Test
    public void deletePersonNonExistingIT() {
        assertThat(personService.deletePerson("Harry", "Potter")).isNull();
    }

    @Test
    public void addPersonIT() {
        Person person = new Person();
        person.setLastName("Domain");
        person.setFirstName("Betty");
        person.setAddress("1 allée des embrumes");
        person.setZip("012345");
        person.setEmail("monemail@email.fr");
        person.setPhone("+330000000");
        person.setCity("Poudelard");

        Assertions.assertThat(personService.addPerson(person)).isEqualTo(person);
    }

    @Test
    public void deleteFireStationByAddressAndStationIT()
    {
        assertThat(fireStationService.deleteFireStationByAddressAndStation("951 LoneTree RD",2)).isEqualTo(1);
    }

    @Test
    public void deleteFireStationByAddressAndStationNonExistingIT()
    {
        assertThat(fireStationService.deleteFireStationByAddressAndStation("748 Townings DR",5)).isNull();
    }

    @Test
    public void deleteFireStationByAddressIT()
    {
        assertThat(fireStationService.deleteFireStationByAddress("748 Townings DR")).isEqualTo(2);
    }

    @Test
    public void deleteFireStationByAddressNonExistingIT()
    {
        assertThat(fireStationService.deleteFireStationByAddress("Paris city road")).isNull();
    }

    @Test
    public void addFireStation()
    {
        FireStation fireStation = new FireStation();
        fireStation.setStation(40);
        fireStation.setAddress("Adresse de la nouvelle caserne de pompiers n°40");

        fireStationService.addFireStation(fireStation);

        Optional<FireStation> foundFireStation = fireStationRepository.findFirstByAddressIgnoreCaseAndStation(fireStation.getAddress(),fireStation.getStation());

        assertThat(foundFireStation.get().getStation()).isEqualTo(fireStation.getStation());
        assertThat(foundFireStation.get().getAddress()).isEqualTo(fireStation.getAddress());

    }
}
