package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.PersonInfoService;
import com.safetynet.alerts.service.PersonService;
import com.safetynet.alerts.viewObjects.PersonInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonInfoController {

    private static final Logger logger = LogManager.getLogger(PersonInfoController.class);

    @Autowired
    private PersonInfoService personInfoService;

    @GetMapping("/personInfo")
    public Iterable<PersonInfo> getPersonsInfo(@RequestParam String firstname, @RequestParam String lastname) {
        //TODO : voir si le endpoint est correct car il ne respecte pas la syntaxe personInfo?{firstname}&{lastname} : changer en RequestParam
        logger.info("Requête Get sur le endpoint 'personInfo' avec firstname : {" + firstname + "} et lastname  {" + lastname + "} reçue");


        Iterable<PersonInfo> personInfoIterable = personInfoService.getPersonsInfo(firstname,lastname);

        logger.info("Réponse suite au Get sur le endpoint 'personInfo' avec firstname : {" + firstname + "} et lastname  {" + lastname + "} transmise");

        return personInfoIterable;
    }
}
