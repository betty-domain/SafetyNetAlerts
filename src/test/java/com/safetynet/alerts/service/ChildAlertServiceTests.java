package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.ChildAlertDTO;
import com.safetynet.alerts.model.dto.FamilyMemberDTO;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.utils.DateUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class ChildAlertServiceTests {

    @MockBean
    private PersonRepository personRepositoryMock;

    @MockBean
    private MedicalRecordRepository medicalRecordRepositoryMock;

    @SpyBean
    private DateUtils dateUtilsSpy;

    @Autowired
    private ChildAlertService childAlertService;

    @BeforeAll
    private static void setUpAllTest() {

    }

    @BeforeEach
    private void setUpEachTest() {

    }

    @Test
    public void getChildAlertDTOListWithNullValues() {
        assertThat(childAlertService.getChildAlertDTOListFromAddress(null)).isNull();
        verify(personRepositoryMock, Mockito.times(0)).findAllByAddressIgnoreCase(anyString());
        verify(medicalRecordRepositoryMock, Mockito.times(0)).findByFirstNameAndLastNameAllIgnoreCase(anyString(),anyString());
    }

    @Test
    public void getPersonsInfoMappingWithValidList() {

        LocalDate nowLocalDateMock = LocalDate.of(2010, 1, 1);
        when(dateUtilsSpy.getNowLocalDate()).thenReturn(nowLocalDateMock);

        List<Person> personList = new ArrayList<>();

        //Given un enfant avec des autres membres d'un foyer
        Person child1 = new Person();
        child1.setZip("12345");
        child1.setEmail("myEmail");
        child1.setPhone("myPhone");
        child1.setCity("myCity");
        child1.setAddress("myAddress");
        child1.setFirstName("firstName");
        child1.setLastName("lastName");
        personList.add(child1);

        MedicalRecord child1medRec = new MedicalRecord();
        child1medRec.setLastName(child1.getLastName());
        child1medRec.setFirstName(child1.getFirstName());
        child1medRec.setBirthDate(nowLocalDateMock.minusYears(5));

        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(child1.getFirstName(), child1.getLastName())).thenReturn(Optional.of(child1medRec));


        //given la mère de l'enfant avec un dossier médical
        Person mother = new Person();
        mother.setFirstName("motherFirstName");
        mother.setLastName("motherLastName");
        mother.setAddress(child1.getAddress());
        personList.add(mother);

        MedicalRecord motherMedRec = new MedicalRecord();
        motherMedRec.setLastName(mother.getLastName());
        motherMedRec.setFirstName(mother.getFirstName());
        motherMedRec.setBirthDate(nowLocalDateMock.minusYears(35));
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(mother.getFirstName(), mother.getLastName())).thenReturn(Optional.of(motherMedRec));

        //given le père de l'enfant sans dossier médical
        Person father = new Person();
        father.setFirstName("fatherFirstName");
        father.setLastName("fatherLastName");
        father.setAddress(child1.getAddress());
        personList.add(father);
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(father.getFirstName(), father.getLastName())).thenReturn(Optional.empty());


        //given la soeur de l'enfant qui a 18 ans
        Person sister = new Person();
        sister.setLastName("lastnameSister");
        sister.setFirstName("firstnameSister");
        sister.setAddress(child1.getAddress());
        personList.add(sister);

        MedicalRecord sisterMedRec = new MedicalRecord();
        sisterMedRec.setLastName(sister.getLastName());
        sisterMedRec.setFirstName(sister.getFirstName());
        sisterMedRec.setBirthDate(nowLocalDateMock.minusYears(18));
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(sister.getFirstName(), sister.getLastName())).thenReturn(Optional.of(sisterMedRec));

        //given un deuxième enfant sans autres membres de la famille
        Person child2 = new Person();
        child2.setAddress("addressChild2");
        child2.setFirstName("firstNameChild2");
        child2.setLastName("lastNameChild2");
        personList.add(child2);

        MedicalRecord child2MedRec = new MedicalRecord();
        child2MedRec.setLastName(child2.getLastName());
        child2MedRec.setFirstName(child2.getFirstName());
        child2MedRec.setBirthDate(nowLocalDateMock.minusYears(10));
        when(medicalRecordRepositoryMock.findByFirstNameAndLastNameAllIgnoreCase(child2.getFirstName(), child2.getLastName())).thenReturn(Optional.of(child2MedRec));

        //mock des appels aux repository
        when(personRepositoryMock.findAllByAddressIgnoreCase(child1.getAddress())).thenReturn(personList);


        List<ChildAlertDTO> childAlertDTOList = childAlertService.getChildAlertDTOListFromAddress(child1.getAddress());

        //on vérifie qu'on récupèer bien 3 enfants au total
        assertThat(childAlertDTOList.size()).isEqualTo(3);

        ChildAlertDTO child1DTO = childAlertDTOList.get(0);
        List<FamilyMemberDTO> familyMembers = child1DTO.getFamilyMembers();

        assertThat(familyMembers.size()).isEqualTo(3);
        assertThat(child1DTO.getAge()).isEqualTo(5);

        ChildAlertDTO sisterDTO = childAlertDTOList.get(1);
        assertThat(sisterDTO.getAge()).isEqualTo(18);
        assertThat(child1DTO.getFamilyMembers().size()).isEqualTo(sisterDTO.getFamilyMembers().size()).isEqualTo(3);

        ChildAlertDTO child2DTO = childAlertDTOList.get(2);
        assertThat(child2DTO.getFamilyMembers()).isEmpty();



    }
}
