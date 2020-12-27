package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.CommunityMemberDTO;
import com.safetynet.alerts.model.dto.FireStationCommunityDTO;
import com.safetynet.alerts.model.dto.FloodInfoByStationDTO;
import com.safetynet.alerts.model.dto.FloodInfoDTO;
import com.safetynet.alerts.model.mapper.CommunityMemberDTOMapper;
import com.safetynet.alerts.model.mapper.FloodInfoDTOMapper;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        Map<String, List<Person>> mapPersonByFireStationAddress = getPersonsByStationNumber(stationNumber);

        if (mapPersonByFireStationAddress != null) {
            List<Person> personList = convertToListOfPerson(mapPersonByFireStationAddress.values());
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

        Map<String, List<Person>> mapPersonByFireStationAddress = getPersonsByStationNumber(stationNumber);

        if (mapPersonByFireStationAddress != null) {
            List<Person> personList = convertToListOfPerson(mapPersonByFireStationAddress.values());

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
    private Map<String, List<Person>> getPersonsByStationNumber(Integer stationNumber) {
        //TODO : voir si c'est opportun d'utiliser cette fonction dans les 3 endspoint gérés par ce service
        if (stationNumber != null) {
            try {
                //on récupère la liste des stations
                List<FireStation> fireStationList = fireStationRepository.findDistinctByStation(stationNumber);

                //extraction de la liste des adresses des stations
                List<String> addressList = new ArrayList<>();
                fireStationList.forEach(fireStationIterator -> {
                    if (fireStationIterator.getAddress()!=null && !fireStationIterator.getAddress().isEmpty()) {
                        addressList.add(fireStationIterator.getAddress());
                    }
                });

                //on récupère la liste des personnes rattachées à la liste des adresses
                List<Person> personList = personRepository.findAllByAddressInOrderByAddress(addressList);

                Map<String, List<Person>> mapPersonByStationAddress = new HashMap<>();

                addressList.forEach(addressIterator ->
                        {
                            List<Person> personListByAdress = personList.stream().filter(person -> person.getAddress().equalsIgnoreCase(addressIterator)).collect(Collectors.toList());
                            if (mapPersonByStationAddress.containsKey(addressIterator)) {
                                mapPersonByStationAddress.get(addressIterator).addAll(personListByAdress);
                            } else {
                                mapPersonByStationAddress.put(addressIterator, personListByAdress);
                            }
                        }
                );
                return mapPersonByStationAddress;
            } catch (Exception exception) {
                logger.error("Erreur lors de la récupération des personnes liées à une station de feu : " + exception.getMessage() + " Stack Trace : " + exception.getStackTrace());
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Convertit une liste de liste de personnes en une seule liste fusionnée
     *
     * @param collectionOfPersonList liste de liste de personnes
     * @return liste unique de personnes
     */
    private List<Person> convertToListOfPerson(Collection<List<Person>> collectionOfPersonList) {
        List<Person> personList = new ArrayList<>();
        collectionOfPersonList.forEach(peopleList -> personList.addAll(peopleList));
        return personList;
    }

    /**
     * Récupère la liste de tous les foyers rattachés à une station
     *
     * @param stations numéro de la station de feu
     * @return liste des foyers rattachés à la station de feu
     */
    public List<FloodInfoByStationDTO> getFloodInfoByStations(Integer stations) {
        if (stations != null) {
            try {
                Map<String, List<Person>> mapPersonByFireStationAddress = getPersonsByStationNumber(stations);

                List<FloodInfoByStationDTO> floodInfoByStationDTOList = new ArrayList<>();

                for (String fireStationAddress : mapPersonByFireStationAddress.keySet()) {

                    if (fireStationAddress != null && !fireStationAddress.isEmpty()) {
                        FloodInfoByStationDTO floodInfoByStationDTO = new FloodInfoByStationDTO();
                        floodInfoByStationDTO.setAddress(fireStationAddress);

                        //extraction de la liste des personnes liées à la station de feu à partir de l'adresse de celle-ci
                        List<Person> personLinkedToFireStation = mapPersonByFireStationAddress.get(fireStationAddress);
                        if (personLinkedToFireStation != null) {
                            List<FloodInfoDTO> floodInfoDTOList = new ArrayList<>();

                            //on construit les données à retourner en récupérant l'âge sur le medicalRecord de la personne parcourue dans la liste
                            personLinkedToFireStation.forEach(personIterator -> {
                                Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findByFirstNameAndLastNameAllIgnoreCase(personIterator.getFirstName(), personIterator.getLastName()
                                );

                                if (optionalMedicalRecord.isPresent()) {
                                    floodInfoDTOList.add(
                                            Mappers.getMapper(FloodInfoDTOMapper.class).
                                                    convertToFloodInfoDTO(personIterator, optionalMedicalRecord.get()));
                                } else {
                                    floodInfoDTOList.add(
                                            Mappers.getMapper(FloodInfoDTOMapper.class).
                                                    convertToFloodInfoDTO(personIterator, new MedicalRecord()));
                                }
                            });
                            floodInfoByStationDTO.setFloodInfoDTOList(floodInfoDTOList);
                            floodInfoByStationDTOList.add(floodInfoByStationDTO);
                        }
                    }
                }
                return floodInfoByStationDTOList;
            } catch (Exception exception) {
                logger.error("Erreur lors de la récupération des informations pour un dégât des eaux : " + exception.getMessage() + " Stack Trace : " + exception.getStackTrace());
                return null;
            }
        }
        return null;
    }
}
