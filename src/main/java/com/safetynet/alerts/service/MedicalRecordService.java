package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    /**
     * Sauvergarde la liste des données médicales
     * @param medicalRecordList liste des données médicales à sauvegarder
     */
    public void saveAllMedicalRecords(List<MedicalRecord> medicalRecordList)
    {
        if (medicalRecordList!=null) {
            medicalRecordRepository.saveAll(medicalRecordList);
        }
        else {
            //TODO à implémenter
        }
    }

    /**
     * Récupère la liste des donnée médicale correspondant à un nom de famille
     * @param lastname prénom recherché
     * @return liste des données médicales des patients corresponadnt au nom de famille fourni
     */
    public List<MedicalRecord> getListMedicalRecordByLastname(String lastname) {
        if (lastname != null) {
            return medicalRecordRepository.findAllByLastNameAllIgnoreCase(lastname);
        } else {
            return null;
        }
    }
}
