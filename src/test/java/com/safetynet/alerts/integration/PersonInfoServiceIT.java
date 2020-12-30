package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.service.PersonInfoService;
import com.safetynet.alerts.service.PersonService;
import com.safetynet.alerts.utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
@Tag("IntegrationTests")
@SpringBootTest(properties = {
        "application.runner.enabled=true" })
public class PersonInfoServiceIT {

    @Autowired
    private PersonInfoService personInfoService;

    @SpyBean
    private DateUtils dateUtilsMock;
    
    @Test
    @Transactional
    public void getPersonsInfoIT() {
        LocalDate nowLocalDateMock = LocalDate.of(2020, 12, 1);
        when(dateUtilsMock.getNowLocalDate()).thenReturn(nowLocalDateMock);

        List<PersonInfoDTO> personInfoDTOList = new ArrayList<>();

        //{ "firstName":"Sophia", "lastName":"Zemicks", "address":"892 Downing Ct", "city":"Culver", "zip":"97451", "phone":"841-874-7878", "email":"soph@email.com" },
        //"birthdate":"03/06/1988", "medications":["aznol:60mg", "hydrapermazol:900mg", "pharmacol:5000mg", "terazine:500mg"], "allergies":["peanut", "shellfish", "aznol"] },
        personInfoDTOList.add(initPersonInfoDTO("892 Downing Ct", 32,"Zemicks", "soph@email.com",
                Arrays.asList(new String[]{ "aznol:60mg", "hydrapermazol:900mg", "pharmacol:5000mg", "terazine:500mg" }),
                Arrays.asList(new String[]{ "peanut", "shellfish", "aznol" })));

        //    { "firstName":"Warren", "lastName":"Zemicks", "address":"892 Downing Ct", "city":"Culver", "zip":"97451", "phone":"841-874-7512", "email":"ward@email.com" },
        //"birthdate":"03/06/1985", "medications":[], "allergies":[] },

        personInfoDTOList.add(initPersonInfoDTO("892 Downing Ct", 35,"Zemicks", "ward@email.com",
                new ArrayList<>(),new ArrayList<>()));

        //    { "firstName":"Zach", "lastName":"Zemicks", "address":"892 Downing Ct", "city":"Culver", "zip":"97451", "phone":"841-874-7512", "email":"zarc@email.com" },
        //"birthdate":"03/06/2017", "medications":[], "allergies":[] },
        //
        personInfoDTOList.add(initPersonInfoDTO("892 Downing Ct", 3,"Zemicks", "zarc@email.com",
                new ArrayList<>(),new ArrayList<>()));

        assertThat(personInfoService.getPersonsInfo("Sophia", "Zemicks")).containsAll(personInfoDTOList);
    }

    private PersonInfoDTO initPersonInfoDTO(String address, int age, String lastname, String mail, List<String> medication, List<String> allergies) {
        PersonInfoDTO personInfoDTO = new PersonInfoDTO();
        personInfoDTO.setAddress(address);
        personInfoDTO.setAge(age);
        personInfoDTO.setAllergiesList(allergies);
        personInfoDTO.setLastname(lastname);
        personInfoDTO.setMail(mail);
        personInfoDTO.setMedicationList(medication);

        return personInfoDTO;
    }
}
