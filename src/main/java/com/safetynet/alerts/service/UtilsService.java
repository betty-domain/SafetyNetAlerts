package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilsService {

    /**
     * Extrait la liste des noms de famille d'une liste de persones
     *
     * @param personList
     * @return
     */
    public static List<String> getLastNameList(List<Person> personList) {
        if (personList != null) {
            return personList.stream().filter(person -> person.getLastName() != null && !person.getLastName().isEmpty())
                    .map(person -> person.getLastName()).distinct().collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Recherche d'un dossier médical d'une personne parmi une liste de dossiers médicaux
     *
     * @param medicalRecordList liste de dossiers médicaux
     * @param person            personne pour laquelle on recherche un dossier
     * @return Optional de Dossier médical
     */
    public static Optional<MedicalRecord> findMedicalRecord(List<MedicalRecord> medicalRecordList, Person person) {

        return medicalRecordList.stream().filter(medicalRecord ->
                medicalRecord.getFirstName().equalsIgnoreCase(person.getFirstName())
                        && medicalRecord.getLastName().equalsIgnoreCase(person.getLastName())
        ).findFirst();
    }

}
