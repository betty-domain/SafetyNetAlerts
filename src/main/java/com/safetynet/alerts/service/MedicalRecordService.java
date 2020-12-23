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

    public Iterable<MedicalRecord> getAllMedicalRecords() {
        return null;
    }

    public MedicalRecord addMedicalRecord(final MedicalRecord medicalRecord) {
        return null;
    }

    public MedicalRecord updateMedicalRecord(final MedicalRecord medicalRecord) {
        return null;
    }

    public Integer deleteMedicalRecord(final String firstname, final String lastname) {
        return null;
    }
}
