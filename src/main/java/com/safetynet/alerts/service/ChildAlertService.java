package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.ChildAlertDTO;
import com.safetynet.alerts.model.mapper.ChildAlertDTOMapper;
import com.safetynet.alerts.model.mapper.FamilyMemberDTOMapper;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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

            List<ChildAlertDTO> childAlertDTOList = new ArrayList<>();

            //on parcourt la liste des personnes pour constituer les DTO à retourner, uniquement s'ils ont un dossier médical existant (sinon impossible de calculer leur âge)
            personList.forEach(personIterator -> {

                Optional<MedicalRecord> medicalRecordLinkedToPersonIterator = medicalRecordRepository.findByFirstNameAndLastNameAllIgnoreCase(personIterator.getFirstName(), personIterator.getLastName());

                if (medicalRecordLinkedToPersonIterator.isPresent()) {
                    // si le dossier médical existe, on peut créer le DTO correspondant à cette personne et ce dossier médical

                    ChildAlertDTO childAlertDTO = childAlertDTOMapper.convertToChildAlertDTO(personIterator, medicalRecordLinkedToPersonIterator.get());

                    //on récupère les membres du foyer pour la personne en cours
                    List<Person> personFamilyMembers = findFamilyMembers(personList,personIterator);

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
     * Recherche les membres du foyer (même adresse) d'une personne donnée dans une liste de personne
     * @param personList liste de personnes à parcourir
     * @param person personne pour laquelle on cherche les membres de la famille
     * @return membres de la famille de la personne recherchée
     */
    private List<Person> findFamilyMembers(List<Person> personList, Person person)
    {
        return personList.stream().
                filter(personIterator -> personIterator.getAddress().equalsIgnoreCase(person.getAddress()) &&
                        !(personIterator.getFirstName().equalsIgnoreCase(person.getFirstName()) && personIterator.getLastName().equalsIgnoreCase(person.getLastName()))).
                collect(Collectors.toList());
    }

}
