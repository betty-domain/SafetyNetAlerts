package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.CommunityMemberDTO;
import com.safetynet.alerts.model.dto.FireStationCommunityDTO;
import com.safetynet.alerts.model.mapper.CommunityMemberDTOMapper;
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
import java.util.Optional;
import java.util.stream.Collectors;

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

        List<Person> personList = getPersonsByStationNumber(stationNumber);
        if (personList != null) {
            List<CommunityMemberDTO> communityMemberDTOList = new ArrayList<>();

            //on construit les données à retourner en récupérant l'âge sur le medicalRecord de la personne parcourue dans la liste
            personList.forEach(personIterator -> {
                Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findByFirstNameAndLastNameAllIgnoreCase(personIterator.getFirstName(), personIterator.getLastName()
                );

                if (optionalMedicalRecord.isPresent()) {
                    communityMemberDTOList.add(
                            Mappers.getMapper(CommunityMemberDTOMapper.class).
                                    personToCommunityMemberDTO(personIterator, optionalMedicalRecord.get()));
                } else {
                    communityMemberDTOList.add(
                            Mappers.getMapper(CommunityMemberDTOMapper.class).
                                    personToCommunityMemberDTO(personIterator, new MedicalRecord()));
                }
            });

            FireStationCommunityDTO fireStationCommunityDTO = new FireStationCommunityDTO();
            fireStationCommunityDTO.setCommunityMemberDTOList(communityMemberDTOList);

            return fireStationCommunityDTO;

        } else {
            return null;
        }
    }

    /**
     * Retourne la liste des téléphones des résidents rattachés au numéro de station de feu fourni
     *
     * @param stationNumber numéro des stations de feu
     * @return liste des numéro de téléphone des résidents rattachés au numéro fourni (correspondant à plusieurs stations de feu)
     */
    public List<String> getPhoneListByStationNumber(Integer stationNumber) {

        List<Person> personList = getPersonsByStationNumber(stationNumber);
        if (personList != null) {
            return personList.stream().filter(personIteratorFilter -> personIteratorFilter.getPhone() != null && !personIteratorFilter.getPhone().isEmpty()).
                    map(personIterator -> personIterator.getPhone()).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    /**
     * récupère la liste des personnes associées aux station de feu représentées par un numéro
     *
     * @param stationNumber numéro des stations de feu souhaitées
     * @return liste des personnes associées aux stations demandées
     */
    private List<Person> getPersonsByStationNumber(Integer stationNumber) {
        if (stationNumber != null) {
            try {
                //on récupère la liste des stations
                List<FireStation> fireStationList = fireStationRepository.findDistinctByStation(stationNumber);

                //extraction de la liste des adresses des stations
                List<String> addressList = new ArrayList<>();
                fireStationList.forEach(fireStationIterator ->
                        addressList.add(fireStationIterator.getAddress()));

                //on récupère la liste des personnes rattachées à la liste des adresses
                return personRepository.findAllByAddressInOrderByAddress(addressList);
            } catch (Exception exception) {
                logger.error("Erreur lors de la récupération des personnes liées à une station de feu : " + exception.getMessage() + " Stack Trace : " + exception.getStackTrace());
                return null;
            }
        } else {
            return null;
        }
    }
}
