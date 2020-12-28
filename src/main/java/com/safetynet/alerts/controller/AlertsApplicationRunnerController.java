package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.JsonReaderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertsApplicationRunnerController {

    private static final Logger logger = LogManager.getLogger(AlertsApplicationRunnerController.class);

    @Autowired
    private JsonReaderService jsonReaderService;

    public void loadInitialData()
    {
        jsonReaderService.readDataFromJsonFile();
    }
}
