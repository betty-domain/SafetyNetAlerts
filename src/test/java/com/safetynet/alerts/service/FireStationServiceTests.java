package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FireStationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "application.runner.enabled=false" })
public class FireStationServiceTests {

    @MockBean
    private FireStationRepository fireStationRepositoryMock;

    @Autowired
    private FireStationService fireStationService;

    private FireStation fireStation;

    @BeforeAll
    private static void setUpAllTest()
    {

    }

    @BeforeEach
    private void  setUpEachTest() {

        fireStation = new FireStation();
        fireStation.setStation(1);
        fireStation.setAddress("myAddress");
        fireStation.setId(10L);
    }

    @Test
    public void saveAllFireStationsWithNullList()
    {
        assertThat(fireStationService.saveAllFireStations(null)).isFalse();
        verify(fireStationRepositoryMock, Mockito.times(0)).saveAll(anyIterable());
    }

    @Test
    public void saveAllFireStationsWithEmptyList()
    {
        List<Person> lstPerson = new ArrayList<>();
        assertThat(fireStationService.saveAllFireStations(null)).isFalse();
        verify(fireStationRepositoryMock, Mockito.times(0)).saveAll(anyIterable());
    }

    @Test
    public void saveAllFireStationsWithList()
    {
        List<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(new FireStation());

        when(fireStationRepositoryMock.saveAll(anyIterable())).thenReturn(new ArrayList<>());
        assertThat(fireStationService.saveAllFireStations(fireStationList)).isTrue();
        verify(fireStationRepositoryMock, Mockito.times(1)).saveAll(anyIterable());
    }
    @Test
    public void saveAllFireStationWithException() {
        List<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(new FireStation());

        given(fireStationRepositoryMock.saveAll(fireStationList)).willAnswer(invocation -> {
            throw new Exception();
        });
        assertThat(fireStationService.saveAllFireStations(fireStationList)).isFalse();
        verify(fireStationRepositoryMock, Mockito.times(1)).saveAll(fireStationList);
    }

    @Test
    public void getAllFireStationsReturnNull()
    {
        when(fireStationRepositoryMock.findAll()).thenReturn(null);
        assertThat(fireStationService.getAllFireStations()).isNull();
    }

    @Test
    public void getAllFireStationsReturnEmptyList()
    {
        when(fireStationRepositoryMock.findAll()).thenReturn(new ArrayList<FireStation>());
        assertThat(fireStationService.getAllFireStations()).isEmpty();
    }

    @Test
    public void getAllFireStationsReturnList()
    {
        List<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);
        when(fireStationRepositoryMock.findAll()).thenReturn(fireStationList);
        assertThat(fireStationService.getAllFireStations()).size().isEqualTo(1);
    }

    @Test
    public void getAllFireStationsWithException()
    {
        given(fireStationRepositoryMock.findAll()).willAnswer(invocation -> { throw new Exception();});
        assertThat(fireStationService.getAllFireStations()).isNull();
        verify(fireStationRepositoryMock, Mockito.times(1)).findAll();


    }
    @Test
    public void addFireStationWithNullFireStation()
    {
        when(fireStationRepositoryMock.save(null)).thenReturn(null);
        verify(fireStationRepositoryMock, Mockito.times(0)).save(any());
        assertThat(fireStationService.addFireStation(null)).isNull();
    }

    @Test
    public void addFireStationWithFireStation()
    {
        when(fireStationRepositoryMock.findFirstByAddressIgnoreCaseAndStation(any(String.class), any(Integer.class))).thenReturn(Optional.empty());
        when(fireStationRepositoryMock.save(fireStation)).thenReturn(fireStation);
        assertThat(fireStationService.addFireStation(fireStation)).isEqualTo(fireStation);
        verify(fireStationRepositoryMock, Mockito.times(1)).save(any());
    }

    @Test
    public void addFireStationWithExistingFireStation()
    {
        when(fireStationRepositoryMock.findFirstByAddressIgnoreCaseAndStation(any(String.class), any(Integer.class))).thenReturn(Optional.of(fireStation));

        assertThat(fireStationService.addFireStation(fireStation)).isEqualTo(null);
        verify(fireStationRepositoryMock, Mockito.times(0)).save(any());
    }

    @Test
    public void addFireStationWithException()
    {
        when(fireStationRepositoryMock.findFirstByAddressIgnoreCaseAndStation(any(String.class), any(Integer.class))).
                thenReturn(Optional.empty());
        given(fireStationRepositoryMock.save(fireStation)).willAnswer(invocation -> { throw new Exception();});

        assertThat(fireStationService.addFireStation(fireStation)).isNull();
        verify(fireStationRepositoryMock, Mockito.times(1)).save(fireStation);
    }

    @Test
    public void updateFireStationWithNullFireStation()
    {
        when(fireStationRepositoryMock.save(null)).thenReturn(null);
        verify(fireStationRepositoryMock, Mockito.times(0)).save(any());
        assertThat(fireStationService.updateFireStation(null)).isNull();
    }

    @Test
    public void updateFireStationWithStation()
    {
        List<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        when(fireStationRepositoryMock.findDistinctByAddressIgnoreCase(any(String.class))).thenReturn(fireStationList);
        when(fireStationRepositoryMock.save(fireStation)).thenReturn(fireStation);
        assertThat(fireStationService.updateFireStation(fireStation)).isEqualTo(fireStation);
        verify(fireStationRepositoryMock, Mockito.times(1)).save(fireStation);
    }

    @Test
    public void updateFireStationWithNonExistingFireStation()
    {
        List<FireStation> fireStationList = new ArrayList<>();
        when(fireStationRepositoryMock.findDistinctByAddressIgnoreCase(any(String.class))).thenReturn(fireStationList);

        assertThat(fireStationService.updateFireStation(fireStation)).isEqualTo(null);
        verify(fireStationRepositoryMock, Mockito.times(0)).save(any());
    }

    @Test
    public void updateFireStationWithException()
    {
        List<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);
        when(fireStationRepositoryMock.findDistinctByAddressIgnoreCase(any(String.class))).thenReturn(fireStationList);

        given(fireStationRepositoryMock.save(fireStation)).willAnswer(invocation -> { throw new Exception();});

        assertThat(fireStationService.updateFireStation(fireStation)).isNull();
        verify(fireStationRepositoryMock, Mockito.times(1)).save(fireStation);
    }

    @Test
    public void deleteFireStationByAddressWithNullFireStation()
    {

        List<FireStation> fireStationList = new ArrayList<>();

        when(fireStationRepositoryMock.findDistinctByAddressIgnoreCase(null)).thenReturn(fireStationList);

        verify(fireStationRepositoryMock, Mockito.times(0)).deleteByAddressIgnoreCase(any(String.class));
        assertThat(fireStationService.deleteFireStationByAddress(null)).isNull();
    }

    @Test
    public void deleteFireStationByAddressAndStationWithNullFireStation()
    {

        List<FireStation> fireStationList = new ArrayList<>();


        when(fireStationRepositoryMock.findFirstByAddressIgnoreCaseAndStation(null,null)).thenReturn(null);

        verify(fireStationRepositoryMock, Mockito.times(0)).deleteByAddressIgnoreCaseAndStation(any(String.class), any(Integer.class));
        assertThat(fireStationService.deleteFireStationByAddressAndStation(null,null)).isNull();
    }

    @Test
    public void deleteFireStationByAddressWithExistingFireStation()
    {
        List<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        when(fireStationRepositoryMock.findDistinctByAddressIgnoreCase(any(String.class))).thenReturn(fireStationList);
        when(fireStationRepositoryMock.deleteByAddressIgnoreCase(any(String.class))).thenReturn(1);
        assertThat(fireStationService.deleteFireStationByAddress(fireStation.getAddress())).isEqualTo(1);
    }

    @Test
    public void deleteFireStationByAddressAndStationWithExistingFireStation()
    {

        when(fireStationRepositoryMock.findFirstByAddressIgnoreCaseAndStation(any(String.class),any(Integer.class))).thenReturn(Optional.of(fireStation));
        when(fireStationRepositoryMock.deleteByAddressIgnoreCaseAndStation(any(String.class),any(Integer.class))).thenReturn(1);
        assertThat(fireStationService.deleteFireStationByAddressAndStation(fireStation.getAddress(),fireStation.getStation())).isEqualTo(1);
    }

    @Test
    public void deleteFireStationByAddressWithNonExistingFireStation()
    {
        when(fireStationRepositoryMock.findDistinctByAddressIgnoreCase(any(String.class))).thenReturn(new ArrayList<>());

        assertThat(fireStationService.deleteFireStationByAddress(fireStation.getAddress())).isEqualTo(null);
        verify(fireStationRepositoryMock, Mockito.times(0)).deleteByAddressIgnoreCase(any(String.class));
    }

    @Test
    public void deleteFireStationByAddressAndStationWithNonExistingFireStation()
    {
        when(fireStationRepositoryMock.findFirstByAddressIgnoreCaseAndStation(any(String.class),any(Integer.class))).thenReturn(Optional.empty());

        assertThat(fireStationService.deleteFireStationByAddressAndStation(fireStation.getAddress(),fireStation.getStation())).isEqualTo(null);
        verify(fireStationRepositoryMock, Mockito.times(0)).deleteByAddressIgnoreCaseAndStation(any(String.class),any(Integer.class));
    }

    @Test
    public void deleteFireStationByAddressWithException()
    {
        List<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);
        when(fireStationRepositoryMock.findDistinctByAddressIgnoreCase(any(String.class))).
                thenReturn(fireStationList);
        given(fireStationRepositoryMock.deleteByAddressIgnoreCase(fireStation.getAddress())).
                willAnswer(invocation -> { throw new Exception();});

        assertThat(fireStationService.deleteFireStationByAddress(fireStation.getAddress())).isNull();
        verify(fireStationRepositoryMock, Mockito.times(1)).deleteByAddressIgnoreCase(fireStation.getAddress());
    }

    @Test
    public void deleteFireStationByAddressAndStationWithException()
    {
        when(fireStationRepositoryMock.findFirstByAddressIgnoreCaseAndStation(any(String.class),any(Integer.class))).
                thenReturn(Optional.of(fireStation));
        given(fireStationRepositoryMock.deleteByAddressIgnoreCaseAndStation(fireStation.getAddress(),fireStation.getStation())).
                willAnswer(invocation -> { throw new Exception();});

        assertThat(fireStationService.deleteFireStationByAddressAndStation(fireStation.getAddress(),
                fireStation.getStation())).isNull();
        verify(fireStationRepositoryMock, Mockito.times(1)).deleteByAddressIgnoreCaseAndStation(fireStation.getAddress(),fireStation.getStation());
    }

}
