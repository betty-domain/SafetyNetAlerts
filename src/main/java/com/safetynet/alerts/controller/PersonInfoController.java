package com.safetynet.alerts.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.model.dto.FireStationCommunity;
import com.safetynet.alerts.model.dto.Views;
import com.safetynet.alerts.service.PersonInfoService;
import com.safetynet.alerts.model.dto.PersonInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonInfoController {

    private static final Logger logger = LogManager.getLogger(PersonInfoController.class);

    @Autowired
    private PersonInfoService personInfoService;

    @GetMapping("/personInfo")
    public List<PersonInfo> getPersonsInfo(@RequestParam String firstname, @RequestParam String lastname) {

        logger.info("Requête Get sur le endpoint 'personInfo' avec firstname : {" + firstname + "} et lastname  {" + lastname + "} reçue");

        List<PersonInfo> personInfoIterable = personInfoService.getPersonsInfo(firstname, lastname);
        if (personInfoIterable != null) {
            logger.info("Réponse suite au Get sur le endpoint 'personInfo' avec firstname : {" + firstname + "} et lastname  {" + lastname + "} transmise");
            return personInfoIterable;
        } else {
            throw new FunctionalException("personInfo.get.error");
        }
    }

    @JsonView(Views.Public.class)
    @GetMapping("/fireStation")
    public FireStationCommunity getFireStationCommunity(@RequestParam Integer stationNumber) {

        logger.info("Requête Get sur le endpoint 'fireStation' avec stationNumber : {" + stationNumber.toString() + "} reçue");

        FireStationCommunity fireStationCommunity = personInfoService.getFireStationCommunity(stationNumber);
        if (fireStationCommunity != null) {
            logger.info("Réponse suite au Get sur le endpoint 'fireStation' avec stationNumber : {" +stationNumber.toString() + "} transmise");
            return fireStationCommunity;
        } else {
            throw new FunctionalException("personInfo.getFireStationCommunity.error");
        }
    }


}
