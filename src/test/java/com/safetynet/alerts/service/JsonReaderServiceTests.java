package com.safetynet.alerts.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = {
        "application.runner.enabled=false" })
public class JsonReaderServiceTests {

    @MockBean
    private PersonService personServiceMock;

    @MockBean
    private MedicalRecordService medicalRecordServiceMock;

    @MockBean
    private FireStationService fireStationServiceMock;

    @Autowired
    private JsonReaderService jsonReaderService;

    @Value("${data.jsonFilePath.personError}")
    private String personErrorFilePath;

    @Test
    public void readDataFromJsonFileWithFileException()
    {
        ReflectionTestUtils.setField(jsonReaderService, "filePath", "falsePath");

        jsonReaderService.readDataFromJsonFile();
        verify(personServiceMock, Mockito.times(0)).saveAllPersons(anyList());
        verify(medicalRecordServiceMock,Mockito.times(0)).saveAllMedicalRecords(anyList());
        verify(fireStationServiceMock,Mockito.times(0)).saveAllFireStations(anyList());
    }

    @Test
    public void readDataFromJsonFileWithParsingException()
    {

        ReflectionTestUtils.setField(jsonReaderService, "filePath", personErrorFilePath);
        jsonReaderService.readDataFromJsonFile();

        verify(personServiceMock).saveAllPersons(argThat(personList -> personList.size() == 2  ));
        verify(fireStationServiceMock).saveAllFireStations(argThat(fireStationList -> fireStationList.size() == 1  ));
        verify(medicalRecordServiceMock).saveAllMedicalRecords(argThat(medicalRecordList -> medicalRecordList.size() == 2  ));
    }
}
