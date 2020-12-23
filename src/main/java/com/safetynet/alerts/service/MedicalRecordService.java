package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    private static final Logger logger = LogManager.getLogger(MedicalRecordService.class);

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    /**
     * Sauvergarde la liste des données médicales
     *
     * @param medicalRecordList liste des données médicales à sauvegarder
     */
    public boolean saveAllMedicalRecords(List<MedicalRecord> medicalRecordList) {
        if (medicalRecordList != null && !medicalRecordList.isEmpty()) {
            try {
                medicalRecordRepository.saveAll(medicalRecordList);
                return true;
            } catch (Exception exception) {
                logger.error("Erreur lors de l'enregistrement de la liste des données médicales " + exception.getMessage() + " , Stack Trace : " + exception.getStackTrace());
                //TODO voir comment faire suivre l'exception et arrêter le programme éventuellement ?
            }
        }
        return false;
    }

    /**
     * récupère la liste de toutes les données médicales
     * @return liste des données médicales
     */
    public Iterable<MedicalRecord> getAllMedicalRecords() {
        try {
            return medicalRecordRepository.findAll();
        } catch (Exception exception) {
            logger.error("Erreur lors de la récupération de la liste des données médicales : " + exception.getMessage() + " Stack Trace + " + exception.getStackTrace());
            return null;
        }
    }

    /**
     * ajoute une donnée médicale
     * @param medicalRecord donnée médicale à ajouter
     * @return donnée médicale si l'ajout s'est correctement passé, null sinon
     */
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        if (medicalRecord != null) {
            Optional<MedicalRecord> medicalRecordOptional = this.getMedicalRecordByFirstNameAndLastName(medicalRecord.getFirstName(),medicalRecord.getLastName());
            if (medicalRecordOptional.isPresent())
            {
                logger.error("Erreur lors de l'ajout d'une donnée médicale déjà existante");
                return null;
            }
            else {
                try {
                    medicalRecordRepository.save(medicalRecord);
                }
                catch (Exception exception) {
                    logger.error("Erreur lors de l'ajout d'une donnée médicale:" + exception.getMessage() + " StackTrace : " + exception.getStackTrace());
                    return null;
                }
            }
        }
        return medicalRecord;
    }

    /**
     * mise à jour d'une donnée médicale
     * @param medicalRecord donnée médicale à mettre à jour
     * @return donnée médicale mise à jour, null sinon
     */
    public MedicalRecord updateMedicalRecord(final MedicalRecord medicalRecord) {
        if (medicalRecord!=null) {
            Optional<MedicalRecord> medicalRecordOptional = this.getMedicalRecordByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());

            if (medicalRecordOptional.isPresent()) {
                MedicalRecord medicalRecordToUpdate = medicalRecordOptional.get();

                medicalRecordToUpdate.setMedications(medicalRecord.getMedications());
                medicalRecordToUpdate.setAllergies(medicalRecord.getAllergies());
                medicalRecordToUpdate.setBirthDate(medicalRecord.getBirthDate());

                try {
                    medicalRecordRepository.save(medicalRecordToUpdate);
                    return medicalRecordToUpdate;
                } catch (Exception exception) {
                    logger.error("Erreur lors de la mise à jour d'une donnée médicale : " + exception.getMessage() + " StackTrace : " + exception.getStackTrace());
                    return null;
                }
            } else {
                logger.error("Erreur lors de la mise à jour d'une donnée médicale : la personne n'existe pas");
                return null;
            }
        }
        else
        {
            logger.error("Erreur lors de la mise à jour d'une donnée médicale: object null envoyé");
            return null;
        }
    }

    /**
     * suppression d'une donnée médicale par le prénom et nom du patient
     * @param firstname prénom du patient
     * @param lastname nom du patient
     * @return nombre de lignes suppérimées, null si la suppression ne s'est pas bien déroulée
     */
    public Integer deleteMedicalRecord(final String firstname, final String lastname) {
        Optional<MedicalRecord> medicalRecordOptional = this.getMedicalRecordByFirstNameAndLastName(firstname,lastname);
        if (medicalRecordOptional.isPresent()) {
            try {
                return medicalRecordRepository.deleteMedicalRecordByFirstNameAndLastNameAllIgnoreCase(firstname, lastname);

            } catch (Exception exception) {
                logger.error("Erreur lors de la suppression d'une donnée médicale :" + exception.getMessage() + " StackTrace : " + exception.getStackTrace());
                return null;
            }
        }
        else
        {
            logger.error("Erreur lors de la suppression d'une donnée médicale : personne inexistante");
            return null;
        }
    }

    /**
     * récupère une donnée médicale fonction du prénom et nom du patient
     * @param firstname prénom du patient
     * @param lastname nom du patient
     * @return donnée médicale si elle a été trouvée
     */
    public Optional<MedicalRecord> getMedicalRecordByFirstNameAndLastName(String firstname, String lastname) {
        try {
            return medicalRecordRepository.findByFirstNameAndLastNameAllIgnoreCase(firstname, lastname);
        } catch (Exception exception) {
            logger.error("Erreur lors de la récupération d'une donnée médicale : " + exception.getMessage() + " Stack Trace + " + exception.getStackTrace());
            return null;
        }
    }
}
