package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends CrudRepository<MedicalRecord,Long> {

    List<MedicalRecord> findAllByLastNameAllIgnoreCase(String lastname);
    //TODO revoir les appels à la méthode ci-dessus pour limiter la récupération des données aux personnes parcourues uniquement

    Optional<MedicalRecord> findByFirstNameAndLastNameAllIgnoreCase(String firstname, String lastname);

    @Transactional
    Integer deleteMedicalRecordByFirstNameAndLastNameAllIgnoreCase(String firstname, String lastname);
}
