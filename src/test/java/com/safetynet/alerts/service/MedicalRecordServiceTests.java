package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
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
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class MedicalRecordServiceTests {

    @MockBean
    private MedicalRecordRepository medicalRecordRepositoryMock;

    @Autowired
    private MedicalRecordService medicalRecordService;

    private MedicalRecord medicalRecord;

    @BeforeAll
    private static void setUpAllTest() {

    }

    @BeforeEach
    private void setUpEachTest() {
        medicalRecord = new MedicalRecord();
        medicalRecord.setLastName("myLastName");
        medicalRecord.setFirstName("myFirstName");
        medicalRecord.setBirthDate(LocalDate.of(2000, 10, 15));

        List<String> allergiesList = new ArrayList<>();
        allergiesList.add("Allergie 1");
        allergiesList.add("Allergie 2");

        medicalRecord.setAllergies(allergiesList);

        List<String> medicationList = new ArrayList<>();
        medicationList.add("medicament 1");
        medicationList.add("medicament 2");
        medicalRecord.setMedications(medicationList);
    }

    @Test
    public void saveAllMedicalRecordsWithNullList() {
        assertThat(medicalRecordService.saveAllMedicalRecords(null)).isFalse();
        verify(medicalRecordRepositoryMock, Mockito.times(0)).saveAll(anyIterable());
    }

    @Test
    public void saveAllMedicalRecordsWithEmptyList() {
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        assertThat(medicalRecordService.saveAllMedicalRecords(null)).isFalse();
        verify(medicalRecordRepositoryMock, Mockito.times(0)).saveAll(anyIterable());
    }

    @Test
    public void saveAllMedicalRecordsWithMedicalRecordList() {
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        medicalRecordList.add(new MedicalRecord());

        when(medicalRecordRepositoryMock.saveAll(anyIterable())).thenReturn(new ArrayList<>());
        assertThat(medicalRecordService.saveAllMedicalRecords(medicalRecordList)).isTrue();
        verify(medicalRecordRepositoryMock, Mockito.times(1)).saveAll(anyIterable());
    }

    @Test
    public void saveAllMedicalRecordWithException() {
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        medicalRecordList.add(new MedicalRecord());

        given(medicalRecordRepositoryMock.saveAll(medicalRecordList)).willAnswer(invocation -> {
            throw new Exception();
        });
        assertThat(medicalRecordService.saveAllMedicalRecords(medicalRecordList)).isFalse();
        verify(medicalRecordRepositoryMock, Mockito.times(1)).saveAll(medicalRecordList);

    }

    @Test
    public void getAllMedicalRecordsReturnNull() {
        when(medicalRecordRepositoryMock.findAll()).thenReturn(null);
        assertThat(medicalRecordService.getAllMedicalRecords()).isNull();
    }

    @Test
    public void getAllMedicalRecordsReturnEmptyList() {
        when(medicalRecordRepositoryMock.findAll()).thenReturn(new ArrayList<>());
        assertThat(medicalRecordService.getAllMedicalRecords()).isEmpty();
    }

    @Test
    public void getAllMedicalRecordsReturnList() {
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        medicalRecordList.add(medicalRecord);
        when(medicalRecordRepositoryMock.findAll()).thenReturn(medicalRecordList);
        assertThat(medicalRecordService.getAllMedicalRecords()).size().isEqualTo(1);
    }

    @Test
    public void getAllMedicalRecordsWithException() {
        given(medicalRecordRepositoryMock.findAll()).willAnswer(invocation -> {
            throw new Exception();
        });
        assertThat(medicalRecordService.getAllMedicalRecords()).isNull();
        verify(medicalRecordRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    public void getMedicalRecordByFirstNameAndLastNameWithNullValues() {
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(null, null)).thenReturn(null);
        assertThat(medicalRecordService.getMedicalRecordByFirstNameAndLastName(null, null)).isNull();
    }

    @Test
    public void getMedicalRecordByFirstNameAndLastNameWithEmptyValues() {
        Optional<MedicalRecord> optionalMedicalRecord = Optional.empty();
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(optionalMedicalRecord);
        assertThat(medicalRecordService.getMedicalRecordByFirstNameAndLastName(new String(), new String())).isNotPresent();
    }

    @Test
    public void getMedicalRecordByFirstNameAndLastNameWithValidValues() {
        Optional<MedicalRecord> optionalMedicalRecord = Optional.of(medicalRecord);
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(medicalRecord.getFirstName(), medicalRecord.getLastName())).thenReturn(optionalMedicalRecord);
        assertThat(medicalRecordService.getMedicalRecordByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName())).isPresent().get().isInstanceOf(MedicalRecord.class);
    }

    @Test
    public void getMedicalRecordByFirstNameAndLastNameWithException() {
        given(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).
                willAnswer(invocation -> {
                    throw new Exception();
                });

        assertThat(medicalRecordService.getMedicalRecordByFirstNameAndLastName(new String(), new String())).isNull();
        verify(medicalRecordRepositoryMock, Mockito.times(1)).findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class));
    }

    @Test
    public void addMedicalRecordWithNullMedicalRecord() {
        when(medicalRecordRepositoryMock.save(null)).thenReturn(null);
        verify(medicalRecordRepositoryMock, Mockito.times(0)).save(any());
        assertThat(medicalRecordService.addMedicalRecord(null)).isNull();
    }

    @Test
    public void addMedicalRecordWithMedicalRecord() {
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(medicalRecordRepositoryMock.save(medicalRecord)).thenReturn(medicalRecord);
        assertThat(medicalRecordService.addMedicalRecord(medicalRecord)).isEqualTo(medicalRecord);
        verify(medicalRecordRepositoryMock, Mockito.times(1)).save(any());
    }

    @Test
    public void addMedicalRecordWithExistingMedicalRecord() {
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));

        assertThat(medicalRecordService.addMedicalRecord(medicalRecord)).isEqualTo(null);
        verify(medicalRecordRepositoryMock, Mockito.times(0)).save(any());
    }

    @Test
    public void addMedicalRecordWithException() {
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());
        given(medicalRecordRepositoryMock.save(medicalRecord)).willAnswer(invocation -> {
            throw new Exception();
        });

        assertThat(medicalRecordService.addMedicalRecord(medicalRecord)).isNull();
        verify(medicalRecordRepositoryMock, Mockito.times(1)).save(medicalRecord);
    }

    @Test
    public void updateMedicalRecordWithNullMedicalRecord() {
        when(medicalRecordRepositoryMock.save(null)).thenReturn(null);
        verify(medicalRecordRepositoryMock, Mockito.times(0)).save(any());
        assertThat(medicalRecordService.updateMedicalRecord(null)).isNull();
    }

    @Test
    public void updateMedicalRecordWithMedicalRecord() {
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordRepositoryMock.save(medicalRecord)).thenReturn(medicalRecord);
        assertThat(medicalRecordService.updateMedicalRecord(medicalRecord)).isEqualTo(medicalRecord);
        verify(medicalRecordRepositoryMock, Mockito.times(1)).save(medicalRecord);
    }

    @Test
    public void updateMedicalRecordWithNonExistingMedicalRecord() {
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());

        assertThat(medicalRecordService.updateMedicalRecord(medicalRecord)).isEqualTo(null);
        verify(medicalRecordRepositoryMock, Mockito.times(0)).save(any());
    }

    @Test
    public void updateMedicalRecordWithException() {
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));

        given(medicalRecordRepositoryMock.save(medicalRecord)).willAnswer(invocation -> {
            throw new Exception();
        });

        assertThat(medicalRecordService.updateMedicalRecord(medicalRecord)).isNull();
        verify(medicalRecordRepositoryMock, Mockito.times(1)).save(medicalRecord);
    }

    @Test
    public void deleteMedicalRecordWithNullMedicalRecord() {
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(null, null)).thenReturn(Optional.empty());

        verify(medicalRecordRepositoryMock, Mockito.times(0)).deleteMedicalRecordByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class));
        assertThat(medicalRecordService.deleteMedicalRecord(null, null)).isNull();
    }

    @Test
    public void deleteMedicalRecordWithExistingMedicalRecord() {
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordRepositoryMock.deleteMedicalRecordByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(1);
        assertThat(medicalRecordService.deleteMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName())).isEqualTo(1);
    }

    @Test
    public void deleteMedicalRecordWithNonExistingMedicalRecord() {
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());

        assertThat(medicalRecordService.deleteMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName())).isEqualTo(null);
        verify(medicalRecordRepositoryMock, Mockito.times(0)).
                deleteMedicalRecordByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class));
    }

    @Test
    public void deleteMedicalRecordWithException() {
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        given(medicalRecordRepositoryMock.deleteMedicalRecordByFirstNameAndLastNameAllIgnoreCase(medicalRecord.getFirstName(), medicalRecord.getLastName())).
                willAnswer(invocation -> {
                    throw new Exception();
                });

        assertThat(medicalRecordService.deleteMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName())).isNull();
        verify(medicalRecordRepositoryMock, Mockito.times(1)).
                deleteMedicalRecordByFirstNameAndLastNameAllIgnoreCase(medicalRecord.getFirstName(), medicalRecord.getLastName());
    }

}
