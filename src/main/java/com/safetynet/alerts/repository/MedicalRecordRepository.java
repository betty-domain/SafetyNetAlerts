package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends CrudRepository<MedicalRecord,Long> {

    List<MedicalRecord> findAllByLastNameAllIgnoreCase(String lastname);

    MedicalRecord findByLastNameAndFirstNameAllIgnoreCase(String lastname, String firstname);
}
