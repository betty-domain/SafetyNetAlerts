package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.CommunityMemberDTO;
import com.safetynet.alerts.model.dto.FireStationCommunityDTO;
import com.safetynet.alerts.model.mapper.PersonCommunityMemberDTOMapper;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FireStationCommunityService {

    private static final Logger logger = LogManager.getLogger(FireStationCommunityService.class);

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FireStationRepository fireStationRepository;

    /**
     * Récupérer les personnes rattachées à une station du feu ainsi que le nb d'adultes et d'enfants parmi cette liste
     *
     * @param stationNumber numéro de la station de feu
     * @return Objet consolidant la liste des personnes et le nb d'adultes et d'enfants
     */
    public FireStationCommunityDTO getFireStationCommunity(Integer stationNumber) {
        if (stationNumber != null) {
            //on récupère la liste des stations
            List<FireStation> fireStationList = fireStationRepository.findDistinctByStation(stationNumber);

            //extraction de la liste des adresses des stations
            List<String> addressList = new ArrayList<>();
            fireStationList.forEach(fireStationIterator ->
                    addressList.add(fireStationIterator.getAddress()));

            //on récupère la liste des personnes rattachées à la liste des adresses
            List<Person> personList = personRepository.findAllByAddressInOrderByAddress(addressList);

            List<CommunityMemberDTO> communityMemberDTOList = new ArrayList<>();

            //on construit les données à retourner en récupérant l'âge sur le medicalRecord de la personne parcourue dans la liste
            personList.forEach(personIterator -> {
                MedicalRecord medicalRecord = medicalRecordRepository.findByLastNameAndFirstNameAllIgnoreCase(personIterator.getLastName(),
                        personIterator.getFirstName());

                communityMemberDTOList.add(
                        Mappers.getMapper(PersonCommunityMemberDTOMapper.class).
                                personToCommunityMemberDTO(personIterator, medicalRecord));
            });

            FireStationCommunityDTO fireStationCommunityDTO = new FireStationCommunityDTO();
            fireStationCommunityDTO.setCommunityMemberDTOList(communityMemberDTOList);

            return fireStationCommunityDTO;

        } else {
            return null;
        }
    }
}
