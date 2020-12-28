package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.ChildAlertDTO;
import com.safetynet.alerts.model.mapper.ChildAlertDTOMapper;
import com.safetynet.alerts.model.mapper.FamilyMemberDTOMapper;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.Constants;
import org.hibernate.event.internal.DefaultPersistOnFlushEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChildAlertService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    MedicalRecordRepository medicalRecordRepository;

    @Autowired
    ChildAlertDTOMapper childAlertDTOMapper;

    @Autowired
    FamilyMemberDTOMapper familyMemberDTOMapper;

    /**
     * Récupération d'une liste d'enfants (18ans ou moins) habitant à cette adresse. La liste comprend les enfants identifiés ainsi que la liste des autres membres du foyer pour ces enfants
     *
     * @param address adresse pour laquelle on cherche la liste d'enfants
     * @return Liste d'enfants, liste vide s'il n'y a personne pour l'adresse données
     */
    public List<ChildAlertDTO> getChildAlertDTOListFromAddress(String address) {
        if (address != null) {

            List<Person> personList = personRepository.findAllByAddressIgnoreCase(address);

            List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAllByLastNameInAllIgnoreCase(getLastNameList(personList));

            List<ChildAlertDTO> childAlertDTOList = new ArrayList<>();

            //on parcourt la liste des personnes pour constituer les DTO à retourner, uniquement s'ils ont un dossier médical existant (sinon impossible de calculer leur âge)
            personList.forEach(personIterator -> {

                //on cherche le dossier médical de la personne
                Optional<MedicalRecord> medicalRecordLinkedToPersonIterator = medicalRecordList.stream().filter(medicalRecord ->
                        medicalRecord.getFirstName().equalsIgnoreCase(personIterator.getFirstName())
                                && medicalRecord.getLastName().equalsIgnoreCase(personIterator.getLastName())
                ).findFirst();

                if (medicalRecordLinkedToPersonIterator.isPresent()) {
                    // si le dossier médical existe, on peut créer le DTO correspondant à cette personne et ce dossier médical

                    ChildAlertDTO childAlertDTO = childAlertDTOMapper.convertToChildAlertDTO(personIterator, medicalRecordLinkedToPersonIterator.get());

                    //on récupère les membres du foyer pour la personne en cours (on suppose que même foyer = même adresse)
                    List<Person> personFamilyMembers = personList.stream().
                            filter(person -> person.getAddress().equalsIgnoreCase(personIterator.getAddress()) &&
                                    !(person.getFirstName().equalsIgnoreCase(personIterator.getFirstName()) && person.getLastName().equalsIgnoreCase(personIterator.getLastName()))).
                            collect(Collectors.toList());
                    childAlertDTO.setFamilyMembers(familyMemberDTOMapper.personListToFamilyMemberDTOList(personFamilyMembers));

                    childAlertDTOList.add(childAlertDTO);
                }
            });

            //on ne retourne que les éléments chilAlert dont l'age est <=18 ans
            return childAlertDTOList.stream().filter(childAlertDTO -> childAlertDTO.getAge() <= Constants.AGE_MAX_CHILDREN).collect(Collectors.toList());

        }
        return null;

    }

    /**
     * Extrait la liste des noms de famille d'une liste de persones
     *
     * @param personList
     * @return
     */
    private List<String> getLastNameList(List<Person> personList) {
        if (personList != null) {
            return personList.stream().map(person -> person.getLastName()).distinct().collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
