package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.CommunityMemberDTO;
import com.safetynet.alerts.model.dto.FireStationCommunity;
import com.safetynet.alerts.model.dto.PersonCommunityMemberDTOMapper;
import com.safetynet.alerts.repository.FireStationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import com.safetynet.alerts.model.dto.PersonInfo;
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

    @Autowired
    private FireStationRepository fireStationRepository;

    /**
     * Récupère les informations des personnes selon leur nom et prénoms
     *
     * @param firstname prénom
     * @param lastname  nom
     * @return liste des personnes avec leurs informations
     */
    public List<PersonInfo> getPersonsInfo(String firstname, String lastname) {
        if (lastname != null) {
            List<Person> personList = personRepository.findAllByLastNameAllIgnoreCase(lastname);
            List<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findAllByLastNameAllIgnoreCase(lastname);

            return convertToPersonInfoIterable(personList, optionalMedicalRecord);
        } else {
            return null;
        }
    }

    /**
     * Méthode de conversion d'une liste de personne et de données médicales en liste de PersonInfo
     *
     * @param personList        liste des personnes
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
                personInfo.setAge(dateUtil.getAge(medicalRecordForPerson.get().getBirthDate()));
            } else {
                personInfo.setAllergiesList(new ArrayList<>());
                personInfo.setMedicationList(new ArrayList<>());
            }
            personInfoIterable.add(personInfo);
        });

        return personInfoIterable;
    }

    /**
     * Récupérer les personnes rattachées à une station du feu ainsi que le nb d'adultes et d'enfants parmi cette liste
     * @param stationNumber numéro de la station de feu
     * @return Objet consolidant la liste des personnes et le nb d'adultes et d'enfants
     */
    public FireStationCommunity getFireStationCommunity(Integer stationNumber) {
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
                                personToCommunityMemberDTO(personIterator,medicalRecord));
            });

            FireStationCommunity fireStationCommunity = new FireStationCommunity();
            fireStationCommunity.setCommunityMemberDTOList(communityMemberDTOList);

            return fireStationCommunity;

        } else {
            return null;
        }
    }
}
