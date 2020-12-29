package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.mapper.PersonInfoDTOMapper;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
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

    @Autowired
    private PersonInfoDTOMapper personInfoDTOMapper;

    /**
     * Récupère les informations des personnes selon leur nom et prénoms
     *
     * @param firstname prénom
     * @param lastname  nom
     * @return liste des personnes avec leurs informations
     */
    public List<PersonInfoDTO> getPersonsInfo(String firstname, String lastname) {
        if (lastname != null) {
            List<Person> personList = personRepository.findAllByLastNameAllIgnoreCase(lastname);
            List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAllByLastNameAllIgnoreCase(lastname);

            List<PersonInfoDTO> personInfoDTOList = new ArrayList<>();

            personList.forEach(personIterator -> {

                Optional<MedicalRecord> medicalRecordForPerson = UtilsService.findMedicalRecord(medicalRecordList, personIterator);
                personInfoDTOList.add(personInfoDTOMapper.personToPersonInfoDTO(personIterator, medicalRecordForPerson.orElse(null)));

            });

            return personInfoDTOList;
        } else {
            return null;
        }
    }

}
