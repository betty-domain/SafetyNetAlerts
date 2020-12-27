package com.safetynet.alerts.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.model.dto.FireStationCommunityDTO;
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
    public List<String> getPhoneListByFireStation(@RequestParam Integer stationNumber)
    {//TODO : est ce que c'est bien par numéro de station de feu qu'on doit récupérer les éléments ?
        logger.info("Requête Get sur le endpoint 'phoneAlert' avec stationNumber : {" + stationNumber.toString() + "} reçue");

        List<String> phoneList = fireStationCommunityService.getPhoneListByStationNumber(stationNumber);
        if (phoneList != null) {
            logger.info("Réponse suite au Get sur le endpoint 'phoneAlert' avec stationNumber : {" +stationNumber.toString() + "} transmise");
            return phoneList;
        } else {
            throw new FunctionalException("phoneAlert.get.error");
        }
    }
}