package com.safetynet.alerts.service;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Service
public class JsonReaderService {

    private static final Logger logger = LogManager.getLogger(JsonReaderService.class);

    private List<Person> lstPerson;
    private List<FireStation> lstFireStation;
    private List<MedicalRecord> lstMedicalRecords;

    private ObjectMapper objectMapper;

    @Autowired
    private PersonService personService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private FireStationService fireStationService;


    private String filePath;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void ReadDataFromJsonFile() {
        logger.debug("Démarrage du chargement du fichier data.json");

        //TODO : voir si cette syntaxe est acceptable et valable pour les tests unitaires ou s'il faut utiliser une notation avec classpath
        this.filePath = "./src/main/resources/data.json";
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(this.filePath));

            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = (JSONObject) jsonParser.parse(inputStreamReader);

            this.lstPerson = readListPersonFromJsonObject(jsonObject);
            personService.saveAllPersons(this.lstPerson);

            this.lstFireStation = readListFireStationFromJsonObject(jsonObject);
            fireStationService.saveAllFireStations(lstFireStation);

            this.lstMedicalRecords = readListMedicalRecordFromJsonObject(jsonObject);
            medicalRecordService.saveAllMedicalRecords(lstMedicalRecords);

            inputStreamReader.close();

        } catch (IOException | ParseException exception) {
            logger.error("Error while parsing input json file : " + exception.getMessage() + " Stack Strace : " + exception.getStackTrace());
        }

        logger.debug("Chargement du fichier data.json terminé");
    }

    private List<Person> readListPersonFromJsonObject(JSONObject jsonObject) {
        JSONArray personsInJson = (JSONArray) jsonObject.get("persons");

        objectMapper = new ObjectMapper();
        List<Person> personList = new ArrayList<>();
        personsInJson.forEach(itemArray ->
        {
            try {
                personList.add(objectMapper.readValue(itemArray.toString(), Person.class));
            } catch (JsonProcessingException exception) {
                logger.error("Error while parsing input json file - persons : " + exception.getMessage() + " Stack Strace : " + exception.getStackTrace());
                //TODO : voir comment propager l'exception
            }
        });

        return personList;

    }

    private List<FireStation> readListFireStationFromJsonObject(JSONObject jsonObject) {
        JSONArray fireStationsArrayInJson = (JSONArray) jsonObject.get("firestations");

        objectMapper = new ObjectMapper();
        List<FireStation> fireStationList = new ArrayList<>();
        fireStationsArrayInJson.forEach(itemArray ->
        {
            try {
                fireStationList.add(objectMapper.readValue(itemArray.toString(), FireStation.class));

                //TODO : problème d'une station de pompier en double dans le fichier de départ et donc dans la base de données
            } catch (JsonProcessingException exception) {
                logger.error("Error while parsing input json file - firestations : " + exception.getMessage() + " Stack Strace : " + exception.getStackTrace());
                //TODO : voir comment propager l'exception
            }
        });

        return fireStationList;

    }

    private List<MedicalRecord> readListMedicalRecordFromJsonObject(JSONObject jsonObject) {
        JSONArray medicalRecordsArrayInJson = (JSONArray) jsonObject.get("medicalrecords");

        objectMapper = new ObjectMapper();
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        medicalRecordsArrayInJson.forEach(itemArray ->
        {
            try {
                medicalRecordList.add(objectMapper.readValue(itemArray.toString(), MedicalRecord.class));
            } catch (JsonProcessingException exception) {
                logger.error("Error while parsing input json file - medicalRecords : " + exception.getMessage() + " Stack Strace : " + exception.getStackTrace());
                //TODO : voir comment propager l'exception
            }
        });

        return medicalRecordList;

    }
}





