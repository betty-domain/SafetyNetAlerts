package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.FireStationCommunity;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import com.safetynet.alerts.model.dto.PersonInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonInfoService {

    private static final Logger logger = LogManager.getLogger(PersonInfoService.class);

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PersonRepository personRepository;

    /**
     * Récupère les informations des personnes selon leur nom et prénoms
     * @param firstname prénom
     * @param lastname nom
     * @return liste des personnes avec leurs informations
     */
    public List<PersonInfo> getPersonsInfo(String firstname, String lastname) {
        if (lastname != null) {
            List<Person> personList = personRepository.findAllByLastNameAllIgnoreCase(lastname);
            List<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findAllByLastNameAllIgnoreCase(lastname);

            return convertToPersonInfoIterable(personList, optionalMedicalRecord);
        }
        else {
            return null;
        }
    }

    /**
     * Méthode de conversion d'une liste de personne et de données médicales en liste de PersonInfo
     * @param personList liste des personnes
     * @param medicalRecordList liste des données médicales
     * @return liste consolidée de personneInfo
     */
    private List<PersonInfo> convertToPersonInfoIterable(List<Person> personList, List<MedicalRecord> medicalRecordList) {
        List<PersonInfo> personInfoIterable = new ArrayList<>();

        DateUtils dateUtil = new DateUtils();

        personList.forEach(personIterator -> {
            PersonInfo personInfo = new PersonInfo();
            personInfo.setAddress(personIterator.getAddress());
            personInfo.setMail(personIterator.getEmail());
            personInfo.setLastname(personIterator.getLastName());

            Optional<MedicalRecord> medicalRecordForPerson = medicalRecordList.stream().filter(medicalRecord ->
                    medicalRecord.getFirstName().equalsIgnoreCase(personIterator.getFirstName())
                            && medicalRecord.getLastName().equalsIgnoreCase(personIterator.getLastName())
            ).findFirst();

            if (medicalRecordForPerson.isPresent()) {
                personInfo.setAllergiesList(medicalRecordForPerson.get().getAllergies());
                personInfo.setMedicationList(medicalRecordForPerson.get().getMedications());
                personInfo.setAge(dateUtil.getAge(medicalRecordForPerson.get().getBirthdate()));
            }
            else
            {
                personInfo.setAllergiesList(new ArrayList<>());
                personInfo.setMedicationList(new ArrayList<>());
            }
            personInfoIterable.add(personInfo);
        });

        return personInfoIterable;
    }


    public FireStationCommunity getFireStationCommunity(Integer any) {
        return null;
    }
}
