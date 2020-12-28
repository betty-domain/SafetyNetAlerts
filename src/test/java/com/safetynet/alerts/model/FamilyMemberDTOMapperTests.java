package com.safetynet.alerts.model;

import com.safetynet.alerts.model.dto.FamilyMemberDTO;
import com.safetynet.alerts.model.mapper.FamilyMemberDTOMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "application.runner.enabled=false" })
public class FamilyMemberDTOMapperTests {

    @Autowired
    private FamilyMemberDTOMapper familyMemberDTOMapper;

    @Test
    public void familyMemberDTO_MapsCorrect() {
        Person person = new Person();
        person.setFirstName("personFirstName");
        person.setLastName("personLastName");
        person.setAddress("personAddress");
        person.setPhone("personPhone");
        person.setZip("personZip");
        person.setEmail("personMail");
        person.setCity("personCity");

        FamilyMemberDTO familyMemberDTO = familyMemberDTOMapper.personToFamilyMemberDTO(person);

        assertThat(familyMemberDTO.getFirstName()).isEqualTo(person.getFirstName());
        assertThat(familyMemberDTO.getLastName()).isEqualTo(person.getLastName());
    }

    @Test
    public void familyMemberDTO_nullPerson() {

        assertThat(familyMemberDTOMapper.personToFamilyMemberDTO(null)).isNull();
    }

    @Test
    public void familyMemberDTOList_NullList()
    {
        assertThat(familyMemberDTOMapper.personListToFamilyMemberDTOList(null)).isNull();
    }

    @Test
    public void familyMemberDTOList_ValidMapping()
    {
        Person person = new Person();
        person.setFirstName("personFirstName");
        person.setLastName("personLastName");
        person.setAddress("personAddress");
        person.setPhone("personPhone");
        person.setZip("personZip");
        person.setEmail("personMail");
        person.setCity("personCity");

        Person secondPerson = new Person();
        secondPerson.setAddress("secondAddress");
        secondPerson.setPhone("secondPhone");
        secondPerson.setLastName("secondLastName");
        secondPerson.setFirstName("secondFirstName");
        secondPerson.setEmail("secondEmail");
        secondPerson.setCity("secondCity");
        secondPerson.setZip("secondZip");

        List<Person> personList = new ArrayList<>();
        personList.add(person);
        personList.add(secondPerson);

        List<FamilyMemberDTO> familyMemberDTOList = familyMemberDTOMapper.personListToFamilyMemberDTOList(personList);

        assertThat(familyMemberDTOList.size()).isEqualTo(2);
        assertThat(familyMemberDTOList.get(0).getFirstName()).isEqualTo(person.getFirstName());
        assertThat(familyMemberDTOList.get(1).getFirstName()).isEqualTo(secondPerson.getFirstName());
    }
}
