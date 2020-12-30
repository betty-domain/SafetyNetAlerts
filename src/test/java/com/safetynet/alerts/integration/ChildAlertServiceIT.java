package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.dto.ChildAlertDTO;
import com.safetynet.alerts.service.ChildAlertService;
import com.safetynet.alerts.utils.DateUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Tag("IntegrationTests")
@SpringBootTest(properties = {
        "application.runner.enabled=true" })
public class ChildAlertServiceIT {

    @Autowired
    private ChildAlertService childAlertService;

    @SpyBean
    private DateUtils dateUtilsMock;

    @Test
    public void getChildAlertIT() {
        LocalDate nowLocalDateMock = LocalDate.of(2020, 12, 1);
        when(dateUtilsMock.getNowLocalDate()).thenReturn(nowLocalDateMock);

        List<ChildAlertDTO> childAlertDTOList = childAlertService.getChildAlertDTOListFromAddress("1509 Culver St");

        assertThat(childAlertDTOList.size()).isEqualTo(2);
        assertThat(childAlertDTOList.get(0).getFamilyMembers().size()).isEqualTo(4);

        ChildAlertDTO childAlertDTOTenley = childAlertDTOList.stream().filter(childAlertDTO -> childAlertDTO.getFirstName().equalsIgnoreCase("tenley")).findFirst().get();
        assertThat(childAlertDTOTenley.getAge()).isEqualTo(8);
        assertThat(childAlertDTOTenley.getFamilyMembers()).allMatch(familyMemberDTO -> familyMemberDTO.getFirstName().equalsIgnoreCase("John")
                || familyMemberDTO.getFirstName().equalsIgnoreCase("Jacob")
                || familyMemberDTO.getFirstName().equalsIgnoreCase("Felicia")
                || familyMemberDTO.getFirstName().equalsIgnoreCase("Roger"));
    }
}
