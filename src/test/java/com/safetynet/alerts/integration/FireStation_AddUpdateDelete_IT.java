package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.service.FireStationService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "application.runner.enabled=true",
        "spring.datasource.url=jdbc:h2:mem:testSafetyNetAlertFireStationIT" })
public class FireStation_AddUpdateDelete_IT {

    @Autowired
    private FireStationService fireStationService;

    @Autowired
    private FireStationRepository fireStationRepository;

    @Test
    public void deleteFireStationByAddressAndStationIT() {
        assertThat(fireStationService.deleteFireStationByAddressAndStation("951 LoneTree RD", 2)).isEqualTo(1);
    }

    @Test
    public void deleteFireStationByAddressAndStationNonExistingIT() {
        assertThat(fireStationService.deleteFireStationByAddressAndStation("748 Townings DR", 5)).isNull();
    }

    @Test
    public void deleteFireStationByAddressIT() {
        assertThat(fireStationService.deleteFireStationByAddress("748 Townings DR")).isEqualTo(2);
    }

    @Test
    public void deleteFireStationByAddressNonExistingIT() {
        assertThat(fireStationService.deleteFireStationByAddress("Paris city road")).isNull();
    }

    @Test
    public void addFireStationIT() {
        FireStation fireStation = new FireStation();
        fireStation.setStation(40);
        fireStation.setAddress("Adresse de la nouvelle caserne de pompiers n°40");

        fireStationService.addFireStation(fireStation);

        Optional<FireStation> foundFireStation = fireStationRepository.findFirstByAddressIgnoreCaseAndStation(fireStation.getAddress(), fireStation.getStation());

        assertThat(foundFireStation.get().getStation()).isEqualTo(fireStation.getStation());
        assertThat(foundFireStation.get().getAddress()).isEqualTo(fireStation.getAddress());

    }

    @Test
    public void updateFireStationIT()
    {
        FireStation fireStation = new FireStation();
        fireStation.setAddress("29 15th st");
        fireStation.setStation(5);

        fireStationService.updateFireStation(fireStation);

        FireStation foundFireStation = fireStationRepository.findFirstByAddressIgnoreCaseAndStation(fireStation.getAddress(),fireStation.getStation()).get();

        assertThat(foundFireStation.getStation()).isEqualTo(fireStation.getStation());
        assertThat(foundFireStation.getAddress()).isEqualToIgnoringCase(fireStation.getAddress());
    }
}
