package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.CommunityMemberDTO;
import com.safetynet.alerts.model.dto.FireStationCommunityDTO;
import com.safetynet.alerts.model.mapper.PersonCommunityMemberDTOMapper;
import com.safetynet.alerts.model.mapper.PersonInfoDTOMapper;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
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
                Optional<MedicalRecord> medicalRecordForPerson = medicalRecordList.stream().filter(medicalRecord ->
                        medicalRecord.getFirstName().equalsIgnoreCase(personIterator.getFirstName())
                                && medicalRecord.getLastName().equalsIgnoreCase(personIterator.getLastName())
                ).findFirst();

                if (medicalRecordForPerson.isPresent()) {
                    personInfoDTOList.add(
                            Mappers.getMapper(PersonInfoDTOMapper.class).
                                    personToPersonInfoDTO(personIterator, medicalRecordForPerson.get()));
                } else
                {
                    personInfoDTOList.add(
                            Mappers.getMapper(PersonInfoDTOMapper.class).
                                    personToPersonInfoDTO(personIterator, new MedicalRecord()));
                }
            });

            return personInfoDTOList;
        } else {
            return null;
        }
    }

}
