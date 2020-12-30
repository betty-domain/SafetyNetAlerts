package com.safetynet.alerts.integration;

import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.JsonReaderService;
import com.safetynet.alerts.service.MedicalRecordService;
import com.safetynet.alerts.service.PersonService;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("IntegrationTests")
@SpringBootTest(properties = {
        "application.runner.enabled=true" })
public class JsonReaderServiceIT {

    //INFO : au chargement de la classe, le fichier json est chargé puisqu'on a activé le application.runner

    @Autowired
    private PersonService personService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private FireStationService fireStationService;

    @Test
    public void CheckLoadedDataFromJsonFile()
    {
        assertThat(personService.getAllPersons()).size().isEqualTo(23);
        assertThat(medicalRecordService.getAllMedicalRecords()).size().isEqualTo(23);
        assertThat(fireStationService.getAllFireStations()).size().isEqualTo(13);
    }

}
