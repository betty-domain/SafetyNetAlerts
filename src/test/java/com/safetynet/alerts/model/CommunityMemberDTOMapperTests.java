package com.safetynet.alerts.model;

import com.safetynet.alerts.model.dto.CommunityMemberDTO;
import com.safetynet.alerts.model.mapper.CommunityMemberDTOMapper;
import com.safetynet.alerts.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "application.runner.enabled=false" })
public class CommunityMemberDTOMapperTests {

    @MockBean
    private DateUtils dateUtils;

    private CommunityMemberDTOMapper mapper = Mappers.getMapper(CommunityMemberDTOMapper.class);

    @Test
    public void communityMemberDTO_MapsCorrect() {
        Person person = new Person();
        person.setFirstName("personFirstName");
        person.setLastName("personLastName");
        person.setAddress("personAddress");
        person.setPhone("personPhone");
        person.setZip("personZip");
        person.setEmail("personMail");
        person.setCity("personCity");

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setLastName("medicalRecordLastName");
        medicalRecord.setFirstName("medicalRecordFirstNAme");
        medicalRecord.setBirthDate(LocalDate.of(200,01,01));

        LocalDate nowLocalDateMock = LocalDate.of(2020,12,31);
        when(dateUtils.getNowLocalDate()).thenReturn(nowLocalDateMock);

        CommunityMemberDTO communityMemberDTO = mapper.personToCommunityMemberDTO(person,medicalRecord);

        assertThat(communityMemberDTO.getAddress()).isEqualTo(person.getAddress());
        assertThat(communityMemberDTO.getFirstName()).isEqualTo(person.getFirstName());
        assertThat(communityMemberDTO.getLastName()).isEqualTo(person.getLastName());
        assertThat(communityMemberDTO.getPhone()).isEqualTo(person.getPhone());
        assertThat(communityMemberDTO.getAge()).isEqualTo(Period.between(medicalRecord.getBirthDate(),nowLocalDateMock).getYears());


    }

    @Test
    public void communityMemberDTO_NullValues()
    {
        assertThat(mapper.personToCommunityMemberDTO(null,null)).isNull();
    }
}
