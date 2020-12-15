package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.Media;
import java.util.List;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public void saveAllMedicalRecords(List<MedicalRecord> medicalRecordList)
    {
        if (medicalRecordList!=null) {
            medicalRecordRepository.saveAll(medicalRecordList);
        }
        else {
            //TODO à implémenter
        }
    }
}
