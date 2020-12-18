package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import com.safetynet.alerts.model.dto.PersonInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonInfoService {

    private static final Logger logger = LogManager.getLogger(PersonInfoService.class);

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private PersonRepository personRepository;

    /**
     * Récupère les informations des personnes selon leur nom et prénoms
     * @param firstname prénom
     * @param lastname nom
     * @return liste des personnes avec leurs informations
     */
    public Iterable<PersonInfo> getPersonsInfo(String firstname, String lastname)
    {
        //TODO à modifier pour ne chercherque par nom de famille
        List<Person> personList = personRepository.findAllByLastNameAllIgnoreCase(lastname);
        List<MedicalRecord> optionalMedicalRecord = medicalRecordService.getListMedicalRecordByLastname(lastname);

        return  convertToPersonInfoIterable(personList,optionalMedicalRecord);
    }

    private Iterable<PersonInfo> convertToPersonInfoIterable(List<Person> personList, List<MedicalRecord> medicalRecordList) {
        List<PersonInfo> personInfoIterable = new ArrayList<>();

        DateUtils dateUtil = new DateUtils();

        personList.forEach(personIterator -> {
            PersonInfo personInfo = new PersonInfo();
            personInfo.setAdresse(personIterator.getAddress());
            personInfo.setMail(personIterator.getEmail());
            personInfo.setNom(personIterator.getLastName());

            Optional<MedicalRecord> medicalRecordForPerson = medicalRecordList.stream().filter(medicalRecord ->
                    medicalRecord.getFirstName().equalsIgnoreCase(personIterator.getFirstName())
                            && medicalRecord.getLastName().equalsIgnoreCase(personIterator.getLastName())
            ).findFirst();

            if (medicalRecordForPerson.isPresent()) {
                personInfo.setAllergies(medicalRecordForPerson.get().getAllergies());
                personInfo.setDosageMedicaments(medicalRecordForPerson.get().getMedications());
                personInfo.setAge(dateUtil.getAge(medicalRecordForPerson.get().getBirthdate()));
            }
            personInfoIterable.add(personInfo);
        });

        return personInfoIterable;
    }
}
