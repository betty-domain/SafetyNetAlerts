package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FunctionalException;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MedicalRecordController {

    private static final Logger logger = LogManager.getLogger(MedicalRecordController.class);

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/medicalrecords")
    public Iterable<MedicalRecord> getAllMedicalRecords() {
        logger.info("Requête Get sur le endpoint 'medicalrecords' reçue");

        Iterable<MedicalRecord> medicalRecordIterable = medicalRecordService.getAllMedicalRecords();

        logger.info("Réponse suite à la requête Get sur le endpoint medicalrecords transmise");

        return medicalRecordIterable;
    }

    @PostMapping("/medicalrecord")
    public MedicalRecord addMedicalRecord(@Validated @RequestBody MedicalRecord medicalRecord) {
        logger.info("Requête Post sur le endpoint 'medicalRecord' reçue");

        MedicalRecord createdMedicalRecord = medicalRecordService.addMedicalRecord(medicalRecord);

        if (createdMedicalRecord != null) {
            logger.info("Réponse suite au Post sur le endpoint 'medicalRecord' envoyée");
            return createdMedicalRecord;
        } else {
            throw new FunctionalException("medicalRecord.insert.error");

        }
    }

    @PutMapping("/medicalrecord")
    public MedicalRecord updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        logger.info("Requête Put sur le endpoint 'medicalrecord' reçue");

        MedicalRecord updatedMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecord);
        if (updatedMedicalRecord != null) {
            logger.info("Réponse suite au Put sur le endpoint 'medicalrecord' envoyée");
            return updatedMedicalRecord;
        } else {
            throw new FunctionalException("medicalRecord.update.error");
        }
    }

    @DeleteMapping("/medicalrecord")
    public Integer deleteMedicalRecord(@RequestParam String firstname, @RequestParam String lastname) {
        logger.info("Requête Delete sur le endpoint 'medicalrecord' reçue avec les paramètres firstname :" + firstname + " et lastname : " + lastname + " reçue");

        Integer deleteResult = medicalRecordService.deleteMedicalRecord(firstname, lastname);
        if (deleteResult != null) {
            logger.info("Réponse suite au Delete sur le endpoint 'medicalrecord' reçue avec les paramètres firstname :" + firstname + " et lastname : " + lastname + " envoyée");
            return deleteResult;
        } else {
            throw new FunctionalException("medicalRecord.delete.error");
        }
    }

}
