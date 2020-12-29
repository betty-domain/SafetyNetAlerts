package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "application.runner.enabled=false" })
public class UtilsServiceTests {


    @Test
    public void getLastNameListWithNullValues()
    {
        assertThat(UtilsService.getLastNameList(null)).isEmpty();
    }

    @Test
    public void getLastNameListWithValidList()
    {
        List<Person> personList = new ArrayList<>();
        Person person = new Person();
        person.setLastName("lastname");
        person.setFirstName("firstname");
        person.setAddress("address");
        personList.add(person);

        Person person1 = new Person();
        person1.setLastName("person1LastName");
        person.setFirstName("person1Firstname");
        personList.add(person1);

        Person person2 = new Person();
        person2.setLastName("");
        personList.add(person2);

        Person person3 = new Person();
        personList.add(person3);

        List<String> lastnameList =UtilsService.getLastNameList(personList);
        assertThat(lastnameList.size()).isEqualTo(2);
        assertThat(lastnameList.contains(person.getLastName())).isTrue();
        assertThat(lastnameList.contains(person1.getLastName())).isTrue();

    }

    @Test
    public void getStationNumberListWithNullValues()
    {
        assertThat(UtilsService.getStationNumberList(null)).isNull();
    }

    @Test
    public void getStationNumberListWithValidList()
    {
        List<FireStation> fireStationList = new ArrayList<>();
        FireStation fireStation = new FireStation();
        fireStation.setStation(1);
        fireStation.setAddress("Address1");
        fireStation.setId(10L);
        fireStationList.add(fireStation);

        FireStation fireStation1 = new FireStation();
        fireStation1.setStation(2);
        fireStation1.setAddress("Address1");
        fireStation1.setId(20L);
        fireStationList.add(fireStation1);

        FireStation fireStation2 = new FireStation();
        fireStation2.setId(30L);
        fireStation2.setStation(3);
        fireStationList.add(fireStation2);

        List<Integer> stationsNumberList = UtilsService.getStationNumberList(fireStationList);
        assertThat(stationsNumberList.size()).isEqualTo(2);
        assertThat(stationsNumberList.contains(fireStation.getStation()));
        assertThat(stationsNumberList.contains(fireStation1.getStation()));
    }

}
