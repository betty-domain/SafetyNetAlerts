package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "application.runner.enabled=true",
        "spring.datasource.url=jdbc:h2:mem:testSafetyNetAlertMedicalRecordIT" })
public class MedicalRecord_AddUpdateDelete_IT {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Test
    public void deleteMedicalRecordIT() {
        assertThat(medicalRecordService.deleteMedicalRecord("John", "Boyd")).isEqualTo(1);
    }

    @Test
    public void deleteMedicalRecordNonExistingIT() {
        assertThat(medicalRecordService.deleteMedicalRecord("Harry", "Potter")).isNull();
    }

    @Test
    @Transactional
    public void addMedicalRecordIT() {
        //TODO : revoir pourquio le prolème lazy loading n'est pas apparu avant et faut-il mettre des transactional ailleurs ?
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setLastName("Domain");
        medicalRecord.setFirstName("Betty");
        List<String> medication = new ArrayList<>();
        medication.add("medicament:500mg");
        medication.add("medicament2:30mg");
        medicalRecord.setMedications(medication);
        List<String> allergies = new ArrayList<>();
        allergies.add("allergie 1");
        allergies.add("allergie 2");
        medicalRecord.setAllergies(allergies);
        medicalRecord.setBirthDate(LocalDate.of(2000, 5, 15));

        medicalRecordService.addMedicalRecord(medicalRecord);
        MedicalRecord foundMedicalRecord = medicalRecordService.getMedicalRecordByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName()).get();

        assertThat(foundMedicalRecord.getBirthDate()).isEqualTo(medicalRecord.getBirthDate());
        assertThat(foundMedicalRecord.getFirstName()).isEqualTo(medicalRecord.getFirstName());
        assertThat(foundMedicalRecord.getLastName()).isEqualTo(medicalRecord.getLastName());
        assertThat(foundMedicalRecord.getMedications().toArray()).isEqualTo(medicalRecord.getMedications().toArray());
        assertThat(foundMedicalRecord.getAllergies().toArray()).isEqualTo(medicalRecord.getAllergies().toArray());

    }

    @Test
    @Transactional
    public void updateMedicalRecordIT()
    {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setLastName("Cadigan");
        medicalRecord.setFirstName("Eric");
        List<String> medication = new ArrayList<>();
        medication.add("medicament:500mg");
        medication.add("medicament2:30mg");
        medicalRecord.setMedications(medication);
        List<String> allergies = new ArrayList<>();
        allergies.add("allergie 1");
        allergies.add("allergie 2");
        medicalRecord.setAllergies(allergies);
        medicalRecord.setBirthDate(LocalDate.of(2000, 5, 15));

        medicalRecordService.updateMedicalRecord(medicalRecord);
        MedicalRecord foundMedicalRecord = medicalRecordService.getMedicalRecordByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName()).get();

        assertThat(foundMedicalRecord.getBirthDate()).isEqualTo(medicalRecord.getBirthDate());
        assertThat(foundMedicalRecord.getFirstName()).isEqualTo(medicalRecord.getFirstName());
        assertThat(foundMedicalRecord.getLastName()).isEqualTo(medicalRecord.getLastName());
        assertThat(foundMedicalRecord.getMedications().toArray()).isEqualTo(medicalRecord.getMedications().toArray());
        assertThat(foundMedicalRecord.getAllergies().toArray()).isEqualTo(medicalRecord.getAllergies().toArray());

    }
}
