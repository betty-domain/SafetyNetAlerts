package com.safetynet.alerts.integration;

import com.safetynet.alerts.model.dto.CommunityMemberDTO;
import com.safetynet.alerts.model.dto.FireDTO;
import com.safetynet.alerts.model.dto.FireStationCommunityDTO;
import com.safetynet.alerts.model.dto.FloodInfoDTO;
import com.safetynet.alerts.model.dto.StationFloodInfoDTO;
import com.safetynet.alerts.service.FireStationCommunityService;
import com.safetynet.alerts.utils.DateUtils;
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
public class FireStationCommunityServiceIT {

    @Autowired
    private FireStationCommunityService fireStationCommunityService;

    @SpyBean
    private DateUtils dateUtilsMock;

    @Test
    public void getFireStationCommunityIT() {

        LocalDate nowLocalDateMock = LocalDate.of(2020, 12, 1);
        when(dateUtilsMock.getNowLocalDate()).thenReturn(nowLocalDateMock);

        //"489 Manchester St", "112 Steppes Pl"
        FireStationCommunityDTO fireStationCommunityDTO = fireStationCommunityService.getFireStationCommunity(4);

        List<CommunityMemberDTO> communityMemberDTOList = fireStationCommunityDTO.getCommunityMemberDTOList();

        assertThat(communityMemberDTOList.size()).isEqualTo(4);
        assertThat(fireStationCommunityDTO.getAdultsCount()).isEqualTo(4);
        assertThat(fireStationCommunityDTO.getChildrenCount()).isEqualTo(0);

        CommunityMemberDTO communityMemberLily = new CommunityMemberDTO();
        communityMemberLily.setAddress("489 Manchester St");
        communityMemberLily.setFirstName("Lily");
        communityMemberLily.setLastName("Cooper");
        communityMemberLily.setPhone("841-874-9845");
        communityMemberLily.setAge(26);

        assertThat(communityMemberDTOList).contains(communityMemberLily);

        CommunityMemberDTO communityMemberTony = new CommunityMemberDTO();
        communityMemberTony.setAddress("112 Steppes Pl");
        communityMemberTony.setFirstName("Tony");
        communityMemberTony.setLastName("Cooper");
        communityMemberTony.setPhone("841-874-6874");
        communityMemberTony.setAge(26);
        assertThat(communityMemberDTOList).contains(communityMemberTony);

        CommunityMemberDTO communityMemberRon = new CommunityMemberDTO();
        communityMemberRon.setAddress("112 Steppes Pl");
        communityMemberRon.setFirstName("Ron");
        communityMemberRon.setLastName("Peters");
        communityMemberRon.setPhone("841-874-8888");
        communityMemberRon.setAge(55);
        assertThat(communityMemberDTOList).contains(communityMemberRon);

        CommunityMemberDTO communityMemberAllison = new CommunityMemberDTO();
        communityMemberAllison.setAddress("112 Steppes Pl");
        communityMemberAllison.setFirstName("Allison");
        communityMemberAllison.setLastName("Boyd");
        communityMemberAllison.setPhone("841-874-9888");
        communityMemberAllison.setAge(55);
        assertThat(communityMemberDTOList).contains(communityMemberAllison);
    }

    @Test
    public void getPhoneListByStationNumberIT() {
        //"908 73rd St" --> 2,"644 Gershwin Cir" -->1,"947 E. Rose Dr"-->3
        List<String> phoneList = fireStationCommunityService.getPhoneListByStationNumber(1);

        assertThat(phoneList.size()).isEqualTo(4);
        assertThat(phoneList).containsExactlyInAnyOrder("841-874-8547", "841-874-7462", "841-874-6512",
                "841-874-7784");
    }

    @Test
    @Transactional
    public void getFloodInfoByStationsIT() {
        LocalDate nowLocalDateMock = LocalDate.of(2020, 12, 1);
        when(dateUtilsMock.getNowLocalDate()).thenReturn(nowLocalDateMock);

        List<Integer> stationsList = new ArrayList<>();
        //"489 Manchester St","112 Steppes Pl"
        stationsList.add(4);
        //"951 LoneTree Rd","892 Downing Ct","29 15th St"
        stationsList.add(2);

        List<StationFloodInfoDTO> stationFloodInfoDTOList = fireStationCommunityService.getFloodInfoByStations(stationsList);
        assertThat(stationFloodInfoDTOList).size().isEqualTo(5);

        //"489 Manchester St"
        StationFloodInfoDTO stationFloodInfoDTOManchester = new StationFloodInfoDTO();
        stationFloodInfoDTOManchester.setAddress("489 Manchester St");
        List<FloodInfoDTO> floodInfoDTOListManchester = new ArrayList<>();
        //person : { "firstName":"Lily", "lastName":"Cooper", "address":"489 Manchester St",  "phone":"841-874-9845"},
        //med : { "firstName":"Lily", "lastName":"Cooper", "birthdate":"03/06/1994", "medications":[], "allergies":[] },
        floodInfoDTOListManchester.add(initFloodInfoDTO(26, "841-874-9845", "Cooper", new ArrayList<>(), new ArrayList<>()));

        StationFloodInfoDTO foundManchester = stationFloodInfoDTOList.stream().filter(stationFloodInfoDTO -> stationFloodInfoDTO.getAddress().equalsIgnoreCase(stationFloodInfoDTOManchester.getAddress())).findFirst().get();
        assertThat(foundManchester.getFloodInfoDTOList()).containsAll(floodInfoDTOListManchester);

        //"112 Steppes Pl"
        StationFloodInfoDTO stationFloodInfoDTOSteppesPl = new StationFloodInfoDTO();
        stationFloodInfoDTOSteppesPl.setAddress("112 Steppes Pl");
        List<FloodInfoDTO> floodInfoDTOListSteppes = new ArrayList<>();
        //{ "firstName":"Ron", "lastName":"Peters","phone":"841-874-8888",
        //{ "birthdate":"04/06/1965", "medications":[], "allergies":[] },
        floodInfoDTOListSteppes.add(initFloodInfoDTO(55, "841-874-8888", "Peters",
                new ArrayList<>(), new ArrayList<>()));

        //{ "firstName":"Allison", "lastName":"Boyd", "phone":"841-874-9888",
        //{ "birthdate":"03/15/1965", "medications":["aznol:200mg"], "allergies":["nillacilan"] },
        floodInfoDTOListSteppes.add(initFloodInfoDTO(55, "841-874-9888", "Boyd",
                Arrays.asList(new String[]{ "aznol:200mg" }), Arrays.asList(new String[]{ "nillacilan" })));

        //{"firstName":"Tony", "lastName":"Cooper", "phone":"841-874-6874", ,
        //{ "birthdate":"03/06/1994", "medications":["hydrapermazol:300mg", "dodoxadin:30mg"], "allergies":["shellfish"] },
        floodInfoDTOListSteppes.add(initFloodInfoDTO(26, "841-874-6874", "Cooper",
                Arrays.asList(new String[]{ "hydrapermazol:300mg", "dodoxadin:30mg" }),
                Arrays.asList(new String[]{ "shellfish" })));

        stationFloodInfoDTOSteppesPl.setFloodInfoDTOList(floodInfoDTOListSteppes);

        StationFloodInfoDTO foundSteppes = stationFloodInfoDTOList.stream().filter(stationFloodInfoDTO -> stationFloodInfoDTO.getAddress().equalsIgnoreCase(stationFloodInfoDTOSteppesPl.getAddress())).findFirst().get();
        assertThat(foundSteppes.getFloodInfoDTOList()).containsAll(floodInfoDTOListSteppes);
    }

    private FloodInfoDTO initFloodInfoDTO(int age, String phone, String lastname, List<String> medication, List<String> allergies) {
        FloodInfoDTO floodInfoDTO = new FloodInfoDTO();
        floodInfoDTO.setAge(age);
        floodInfoDTO.setPhone(phone);
        floodInfoDTO.setLastname(lastname);
        floodInfoDTO.setAllergiesList(allergies);
        floodInfoDTO.setMedicationList(medication);
        return floodInfoDTO;
    }

    private FireDTO initFireDTO(int age, String phone, String lastname, List<String> medication, List<String> allergies,
                                List<Integer> stationsList) {
        FireDTO fireDTO = new FireDTO();
        fireDTO.setFireStationNumberList(stationsList);
        fireDTO.setLastname(lastname);
        fireDTO.setMedicationList(medication);
        fireDTO.setAllergiesList(allergies);
        fireDTO.setPhone(phone);
        fireDTO.setAge(age);
        return fireDTO;
    }

    @Test
    @Transactional
    public void getFireInfoByAddressIT() {
        //"112 Steppes Pl"
        List<FireDTO> fireDTOList = new ArrayList<>();

        List<Integer> stationsList = new ArrayList<>();
        stationsList.add(3);
        stationsList.add(4);

        //{ "firstName":"Ron", "lastName":"Peters","phone":"841-874-8888",
        //{ "birthdate":"04/06/1965", "medications":[], "allergies":[] },
        fireDTOList.add(initFireDTO(55, "841-874-8888", "Peters",
                new ArrayList<>(), new ArrayList<>(),stationsList));

        //{ "firstName":"Allison", "lastName":"Boyd", "phone":"841-874-9888",
        //{ "birthdate":"03/15/1965", "medications":["aznol:200mg"], "allergies":["nillacilan"] },
        fireDTOList.add(initFireDTO(55, "841-874-9888", "Boyd",
                Arrays.asList(new String[]{ "aznol:200mg" }),
                Arrays.asList(new String[]{ "nillacilan" }),stationsList));

        //{"firstName":"Tony", "lastName":"Cooper", "phone":"841-874-6874", ,
        //{ "birthdate":"03/06/1994", "medications":["hydrapermazol:300mg", "dodoxadin:30mg"], "allergies":["shellfish"] },
        fireDTOList.add(initFireDTO(26, "841-874-6874", "Cooper",
                Arrays.asList(new String[]{ "hydrapermazol:300mg", "dodoxadin:30mg" }),
                Arrays.asList(new String[]{ "shellfish" }),stationsList));

        assertThat(fireStationCommunityService.getFireInfoByAddress("112 Steppes Pl")).containsAll(fireDTOList);

    }
}
