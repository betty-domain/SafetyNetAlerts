package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.CommunityMemberDTO;
import com.safetynet.alerts.model.dto.FireDTO;
import com.safetynet.alerts.model.dto.FireStationCommunityDTO;
import com.safetynet.alerts.model.dto.StationFloodInfoDTO;
import com.safetynet.alerts.model.dto.FloodInfoDTO;
import com.safetynet.alerts.model.mapper.CommunityMemberDTOMapper;
import com.safetynet.alerts.model.mapper.FireDTOMapper;
import com.safetynet.alerts.model.mapper.FloodInfoDTOMapper;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private CommunityMemberDTOMapper communityMemberDTOMapper;

    @Autowired
    private FloodInfoDTOMapper floodInfoDTOMapper;

    @Autowired
    private FireDTOMapper fireDTOMapper;

    /**
     * Récupérer les personnes rattachées à une station du feu ainsi que le nb d'adultes et d'enfants parmi cette liste
     *
     * @param stationNumber numéro de la station de feu
     * @return Objet consolidant la liste des personnes et le nb d'adultes et d'enfants
     */
    public FireStationCommunityDTO getFireStationCommunity(Integer stationNumber) {

        List<Person> personList = getPersonListByStationNumber(stationNumber);

        if (personList != null) {
            List<CommunityMemberDTO> communityMemberDTOList = new ArrayList<>();

            //on construit les données à retourner en récupérant l'âge sur le medicalRecord de la personne parcourue dans la liste
            personList.forEach(personIterator -> {
                Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findByFirstNameAndLastNameAllIgnoreCase(personIterator.getFirstName(), personIterator.getLastName());
                communityMemberDTOList.add(communityMemberDTOMapper.personToCommunityMemberDTO(personIterator, optionalMedicalRecord.orElse(null)));
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

        List<Person> personList = getPersonListByStationNumber(stationNumber);

        if (personList != null) {
            return personList.stream().filter(personIteratorFilter -> personIteratorFilter.getPhone() != null && !personIteratorFilter.getPhone().isEmpty()).
                    map(personIterator -> personIterator.getPhone()).distinct().collect(Collectors.toList());
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
    private List<Person> getPersonListByStationNumber(Integer stationNumber) {
        if (stationNumber != null) {
            try {
                //on récupère la liste des stations
                List<FireStation> fireStationList = fireStationRepository.findDistinctByStation(stationNumber);

                //extraction de la liste des adresses des stations
                List<String> addressList = getAddressListFromFireStationList(fireStationList);

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

    /**
     * Récupère la liste des adresses d'une liste de stations de feu
     *
     * @param fireStationList liste de stations de feu
     * @return liste des adresses dédoublonnée
     */
    private List<String> getAddressListFromFireStationList(List<FireStation> fireStationList) {
        List<String> addressList = new ArrayList<>();
        if (fireStationList != null) {
            fireStationList.forEach(fireStationIterator -> {
                if (fireStationIterator.getAddress() != null && !fireStationIterator.getAddress().isEmpty()) {
                    addressList.add(fireStationIterator.getAddress());
                }
            });
        }
        return addressList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Récupère la liste de tous les foyers rattachés à une station
     *
     * @param stations numéro de la station de feu
     * @return liste des foyers rattachés à la station de feu
     */
    public List<StationFloodInfoDTO> getFloodInfoByStations(List<Integer> stations) {
        if (stations != null) {
            try {
                Map<String, List<Person>> mapPersonByFireStationAddress = getPersonsByStationNumber(stations);

                List<StationFloodInfoDTO> stationFloodInfoDTOList = new ArrayList<>();

                for (String fireStationAddress : mapPersonByFireStationAddress.keySet()) {

                    StationFloodInfoDTO stationFloodInfoDTO = new StationFloodInfoDTO();
                    stationFloodInfoDTO.setAddress(fireStationAddress);

                    //extraction de la liste des personnes liées à la station de feu à partir de l'adresse de celle-ci
                    List<Person> personLinkedToFireStation = mapPersonByFireStationAddress.get(fireStationAddress);

                    if (personLinkedToFireStation != null) {
                        List<FloodInfoDTO> floodInfoDTOList = new ArrayList<>();

                        //on construit les données à retourner en récupérant l'âge sur le medicalRecord de la personne parcourue dans la liste
                        personLinkedToFireStation.forEach(personIterator -> {
                            Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findByFirstNameAndLastNameAllIgnoreCase(personIterator.getFirstName(), personIterator.getLastName());
                            floodInfoDTOList.add(floodInfoDTOMapper.convertToFloodInfoDTO(personIterator, optionalMedicalRecord.orElse(null)));

                        });
                        stationFloodInfoDTO.setFloodInfoDTOList(floodInfoDTOList);
                        stationFloodInfoDTOList.add(stationFloodInfoDTO);
                    }
                }
                return stationFloodInfoDTOList;
            } catch (Exception exception) {
                logger.error("Erreur lors de la récupération des informations pour un dégât des eaux : " + exception.getMessage() + " Stack Trace : " + exception.getStackTrace());
                return null;
            }
        }
        return null;
    }

    /**
     * récupère la liste des personnes associées aux station de feu représentées par des numéros de stations
     *
     * @param stationsNumberList liste des numéros des stations de feu souhaitées
     * @return liste des personnes associées aux stations demandées, regroupées par adresse
     */
    private Map<String, List<Person>> getPersonsByStationNumber(List<Integer> stationsNumberList) {

        try {
            //on récupère la liste des stations
            List<FireStation> fireStationList = fireStationRepository.findDistinctByStationIn(stationsNumberList.stream().distinct().collect(Collectors.toList()));

            //extraction de la liste distincte des adresses des stations
            List<String> addressList = getAddressListFromFireStationList(fireStationList);

            //on récupère la liste des personnes rattachées à la liste des adresses
            List<Person> personList = personRepository.findAllByAddressInOrderByAddress(addressList);

            Map<String, List<Person>> mapPersonByStationAddress = new HashMap<>();

            addressList.forEach(addressIterator ->
                    {
                        List<Person> personListByAdress = personList.stream().filter(person -> person.getAddress().equalsIgnoreCase(addressIterator)).collect(Collectors.toList());
                        mapPersonByStationAddress.put(addressIterator, personListByAdress);
                    }
            );
            return mapPersonByStationAddress;
        } catch (Exception exception) {
            logger.error("Erreur lors de la récupération des personnes liées à une station de feu : " + exception.getMessage() + " Stack Trace : " + exception.getStackTrace());
            return null;
        }
    }

    /**
     * Récupère les informations suite à un feu fonction de l'adresse fourni
     *
     * @param address addresse recherchés
     * @return Liste des habitants vivant à l'adresse donnée ainsi que le numéro de la caserne de pompiers la desservant
     */
    public List<FireDTO> getFireInfoByAddress(String address) {

        if (address != null) {
            try {
                //on récupère la liste des casernes de pompiers qui desservent cette addresse
                List<FireStation> fireStation = fireStationRepository.findDistinctByAddressIgnoreCase(address);
                List<Integer> fireStationNumberList = UtilsService.getStationNumberList(fireStation);

                //on récupère la liste des personnes qui vivent à cette adresse
                List<Person> personList = personRepository.findAllByAddressIgnoreCase(address);

                List<FireDTO> fireDTOList = new ArrayList<>();

                //on fait le mapping des informations pour chaque personne de la liste
                personList.forEach(personIterator -> {
                    FireDTO fireDTO = null;
                    Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findByFirstNameAndLastNameAllIgnoreCase(personIterator.getFirstName(), personIterator.getLastName());

                    fireDTO = fireDTOMapper.convertToFireDTO(personIterator, medicalRecord.orElse(null));

                    fireDTO.setFireStationNumberList(fireStationNumberList);
                    fireDTOList.add(fireDTO);
                });

                return fireDTOList;

            } catch (Exception exception) {
                logger.error("Erreur lors de la récupération des informations pour un feu: " + exception.getMessage() + " Stack Trace : " + exception.getStackTrace());
                return null;
            }

        }
        return null;
    }
}
