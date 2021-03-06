package com.safetynet.alerts.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.model.dto.FireDTO;
import com.safetynet.alerts.model.dto.FireStationCommunityDTO;
import com.safetynet.alerts.model.dto.StationFloodInfoDTO;
import com.safetynet.alerts.model.dto.Views;
import com.safetynet.alerts.service.FireStationCommunityService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FireStationCommunityController {

    private static final Logger logger = LogManager.getLogger(FireStationCommunityController.class);

    @Autowired
    private FireStationCommunityService fireStationCommunityService;

    @JsonView(Views.Public.class)
    @GetMapping("/fireStation")
    public FireStationCommunityDTO getFireStationCommunity(@RequestParam Integer stationNumber) {

        logger.info("Requête Get sur le endpoint 'fireStation' avec stationNumber : {" + stationNumber.toString() + "} reçue");

        FireStationCommunityDTO fireStationCommunityDTO = fireStationCommunityService.getFireStationCommunity(stationNumber);
        if (fireStationCommunityDTO != null) {
            logger.info("Réponse suite au Get sur le endpoint 'fireStation' avec stationNumber : {" +stationNumber.toString() + "} transmise");
            return fireStationCommunityDTO;
        } else {
            throw new FunctionalException("fireStationCommunity.getFireStationCommunity.error");
        }
    }

    @GetMapping("/phoneAlert")
    public List<String> getPhoneListByFireStation(@RequestParam Integer firestation)
    {
        logger.info("Requête Get sur le endpoint 'phoneAlert' avec stationNumber : {" + firestation.toString() + "} reçue");

        List<String> phoneList = fireStationCommunityService.getPhoneListByStationNumber(firestation);
        if (phoneList != null) {
            logger.info("Réponse suite au Get sur le endpoint 'phoneAlert' avec stationNumber : {" +firestation.toString() + "} transmise");
            return phoneList;
        } else {
            throw new FunctionalException("phoneAlert.get.error");
        }
    }

    @GetMapping("/flood/stations")
    public List<StationFloodInfoDTO> getFloodInfoByFireStation(@RequestParam List<Integer> stations)
    {
        logger.info("Requête Get sur le endpoint 'flood' reçue");

        List<StationFloodInfoDTO> stationFloodInfoDTOList = fireStationCommunityService.getFloodInfoByStations(stations);
        if (stationFloodInfoDTOList != null) {
            logger.info("Réponse suite au Get sur le endpoint 'flood' transmise");
            return stationFloodInfoDTOList;
        } else {
            throw new FunctionalException("flood.get.error");
        }
    }

    @GetMapping("/fire")
    public List<FireDTO> getFireInfoByAddress(@RequestParam String address)
    {
        logger.info("Requête Get sur le endpoint 'fire' reçue avec l'adresse : " + address);

        List<FireDTO> fireDTOList = fireStationCommunityService.getFireInfoByAddress(address);
        if (fireDTOList != null) {
            logger.info("Réponse suite au Get sur le endpoint 'fire' avec l'adresse : " + address + " transmise");
            return fireDTOList;
        } else {
            throw new FunctionalException("fire.get.error");
        }
    }

}