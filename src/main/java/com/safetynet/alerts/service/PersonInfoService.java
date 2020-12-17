package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import com.safetynet.alerts.viewObjects.PersonInfo;
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
        List<Person> personList = personRepository.findAllByFirstNameAndLastNameAllIgnoreCase(firstname, lastname);
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordService.findMedicalRecordByFirstnameAndLastname(firstname, lastname);

        return  convertToPersonInfoIterable(personList,optionalMedicalRecord);
    }

    private Iterable<PersonInfo> convertToPersonInfoIterable(List<Person> personList, Optional<MedicalRecord> optionalMedicalRecord)
    {
        List<PersonInfo> personInfoIterable = new ArrayList<>() ;

        MedicalRecord medicalRecord = new MedicalRecord();
        if (optionalMedicalRecord.isPresent())
        {
            medicalRecord = optionalMedicalRecord.get();
        }

        DateUtils dateUtil = new DateUtils();

        final MedicalRecord finalMedicalRecord = medicalRecord;
        personList.forEach(person -> {
            PersonInfo personInfo = new PersonInfo();
            personInfo.setAdresse(person.getAddress());
            personInfo.setAge(dateUtil.getAge(finalMedicalRecord.getBirthdate()));
            personInfo.setMail(person.getEmail());
            personInfo.setAllergies(finalMedicalRecord.getAllergies());
            personInfo.setNom(person.getLastName());
            personInfo.setDosageMedicaments(finalMedicalRecord.getMedications());
            personInfoIterable.add(personInfo);
        });

        return  personInfoIterable;
    }
}
