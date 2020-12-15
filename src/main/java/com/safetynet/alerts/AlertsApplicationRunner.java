package com.safetynet.alerts;

//import com.safetynet.alerts.controller.AlertsApplicationRunnerController;
import com.safetynet.alerts.controller.AlertsApplicationRunnerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AlertsApplicationRunner implements CommandLineRunner {

    @Autowired
    private AlertsApplicationRunnerController runnerController;

    @Override
    public void run(final String... args) throws Exception {

        runnerController.LoadInitialData();
    }
}
