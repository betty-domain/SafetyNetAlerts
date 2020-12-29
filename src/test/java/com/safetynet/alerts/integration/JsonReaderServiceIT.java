package com.safetynet.alerts.integration;

import com.safetynet.alerts.service.FireStationService;
import com.safetynet.alerts.service.JsonReaderService;
import com.safetynet.alerts.service.MedicalRecordService;
import com.safetynet.alerts.service.PersonService;
import org.json.simple.JSONArray;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Autowired
    private JsonReaderService jsonReaderService;

    @Test
    public void CheckLoadedDataFromJsonFile()
    {
        assertThat(personService.getAllPersons()).size().isEqualTo(23);
        assertThat(medicalRecordService.getAllMedicalRecords()).size().isEqualTo(23);
        assertThat(fireStationService.getAllFireStations()).size().isEqualTo(13);
    }


}
