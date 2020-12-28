package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.model.dto.ChildAlertDTO;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.service.ChildAlertService;
import com.safetynet.alerts.service.PersonInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChildAlertController {

    private static final Logger logger = LogManager.getLogger(ChildAlertController.class);

    @Autowired
    private ChildAlertService childAlertService;

    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildAlertList(@RequestParam String address) {

        logger.info("Requête Get sur le endpoint 'childAlert' avec address : {" + address + "} reçue");

        List<ChildAlertDTO> childAlertDTOList = childAlertService.getChildAlertDTOListFromAddress(address);
        if (childAlertDTOList != null) {
            logger.info("Réponse suite au Get sur le endpoint 'childAlert' avec address : {" + address + "} transmise");
            return childAlertDTOList;
        } else {
            throw new FunctionalException("childAlert.get.error");
        }
    }
}
