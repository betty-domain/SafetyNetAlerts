package com.safetynet.alerts.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.model.dto.Views;
import com.safetynet.alerts.service.FireStationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FireStationController {

    private static final Logger logger = LogManager.getLogger(FireStationController.class);

    @Autowired
    private FireStationService fireStationService;

    @GetMapping("/firestations")
    @JsonView(Views.Public.class)
    public Iterable<FireStation> getAllPersons() {
        logger.info("Requête Get sur le endpoint 'firestations' reçue");

        Iterable<FireStation> fireStationIterable = fireStationService.getAllFireStations();

        logger.info("Réponse suite à la requête Get sur le endpoint firestations transmise");

        return fireStationIterable;
    }


    @PostMapping("/firestation")
    @JsonView(Views.Public.class)
    public FireStation addFireStation(@Validated @RequestBody FireStation fireStation) {
        logger.info("Requête Post sur le endpoint 'firestation' reçue");

        FireStation createdFireStation = fireStationService.addFireStation(fireStation);

        if (createdFireStation != null) {
            logger.info("Réponse suite au Post sur le endpoint 'firestation' envoyée");
            return createdFireStation;
        } else {
            throw new FunctionalException("firestation.insert.error");
        }
    }

    @PutMapping("/firestation")
    @JsonView(Views.Public.class)
    public FireStation updateFireStation(@RequestBody FireStation fireStation)
    {
        logger.info("Requête Put sur le endpoint 'firestation' reçue");

        FireStation updatedFireStation = fireStationService.updateFireStation(fireStation);
        if (updatedFireStation!=null )
        {
            logger.info("Réponse suite au Put sur le endpoint 'firestation' envoyée");
            return updatedFireStation;
        }
        else
        {
            throw new FunctionalException("firestation.update.error");
        }
    }

    @DeleteMapping("/firestation/{address}")
    public Integer deleteFireStation(@PathVariable String address)
    {
        logger.info("Requête Delete sur le endpoint 'firestation' reçue avec les paramètres address:" + address + " reçue");

        Integer deleteResult = fireStationService.deleteFireStationByAddress(address);
        if (deleteResult!=null) {
            logger.info("Réponse suite au Delete sur le endpoint 'firestation' reçue avec les paramètres address :" + address + " envoyée");
            return deleteResult;
        }
        else
        {
            throw new FunctionalException("firestation.delete.byAddress.error");
        }
    }

    @DeleteMapping("/firestation/{address}/{station}")
    public Integer deleteFireStation(@PathVariable String address, @PathVariable Integer station)
    {
        logger.info("Requête Delete sur le endpoint 'firestation' reçue avec les paramètres station :" + station + " reçue");

        Integer deleteResult = fireStationService.deleteFireStationByAddressAndStation(address,station);
        if (deleteResult!=null) {
            logger.info("Réponse suite au Delete sur le endpoint 'firestation' reçue avec les paramètres station :" + station + " envoyée");
            return deleteResult;
        }
        else
        {
            throw new FunctionalException("firestation.delete.byStation.error");
        }
    }
}
