package com.safetynet.alerts.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.model.dto.FireStationCommunityDTO;
import com.safetynet.alerts.model.dto.Views;
import com.safetynet.alerts.service.PersonInfoService;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
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
    public List<PersonInfoDTO> getPersonsInfo(@RequestParam String firstname, @RequestParam String lastname) {

        logger.info("Requête Get sur le endpoint 'personInfo' avec firstname : {" + firstname + "} et lastname  {" + lastname + "} reçue");

        List<PersonInfoDTO> personInfoDTOIterable = personInfoService.getPersonsInfo(firstname, lastname);
        if (personInfoDTOIterable != null) {
            logger.info("Réponse suite au Get sur le endpoint 'personInfo' avec firstname : {" + firstname + "} et lastname  {" + lastname + "} transmise");
            return personInfoDTOIterable;
        } else {
            throw new FunctionalException("personInfo.get.error");
        }
    }




}
