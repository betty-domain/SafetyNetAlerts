package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FireStationService {

    private static final Logger logger = LogManager.getLogger(FireStationService.class);

    @Autowired
    private FireStationRepository fireStationRepository;

    public boolean saveAllFireStations(List<FireStation> fireStationsList)
    {
        if (fireStationsList!=null && !fireStationsList.isEmpty()) {
            try {
                fireStationRepository.saveAll(fireStationsList);
                return true;
            }
            catch (Exception exception)
            {
                logger.error("Erreur lors de l'enregistrement de la liste des personnes " + exception.getMessage() + " , Stack Trace : " + exception.getStackTrace());
                //TODO voir comment faire suivre l'exception et arrêter le programme éventuellement ?

            }
        }
        return false;
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

    public Integer deleteFireStationByAddressAndStation(String address,Integer station) {
        return Integer.MAX_VALUE;
    }
}
