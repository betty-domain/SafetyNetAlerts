package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FireStationService {

    @Autowired
    private FireStationRepository fireStationRepository;

    public void saveAllFireStations(List<FireStation> fireStationsList)
    {
        if (fireStationsList!=null) {
            fireStationRepository.saveAll(fireStationsList);
        }
        else {//TODO à implémenter
        }
    }

    public Iterable<FireStation> getAllFireStations() {
        return  null;
    }

    public FireStation addFireStation(FireStation fireStation) {
        return  null;
    }

    public FireStation updateFireStation(FireStation fireStation) {
        return  null;
    }

    public Integer deleteFireStationByAddress( String address) {
        return Integer.MAX_VALUE;
    }

    public Integer deleteFireStationByAddressAndStation(Integer station, final String address) {
        return Integer.MAX_VALUE;
    }
}
