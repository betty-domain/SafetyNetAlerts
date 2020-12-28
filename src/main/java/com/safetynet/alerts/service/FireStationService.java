package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.FireStationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FireStationService {

    private static final Logger logger = LogManager.getLogger(FireStationService.class);

    @Autowired
    private FireStationRepository fireStationRepository;

    /**
     * Sauvegarder la liste des stations de feu
     *
     * @param fireStationsList Liste à sauvegarder
     * @return true si la sauvegarde s'est bien passée, false en cas d'erreur
     */
    public boolean saveAllFireStations(List<FireStation> fireStationsList) {
        if (fireStationsList != null && !fireStationsList.isEmpty()) {
            try {
                fireStationRepository.saveAll(fireStationsList);
                return true;
            } catch (Exception exception) {
                logger.error("Erreur lors de l'enregistrement de la liste des personnes " + exception.getMessage() + " , Stack Trace : " + exception.getStackTrace());

            }
        }
        return false;
    }

    /**
     * Récupère la liste de toutes les stations de feu
     *
     * @return Liste des stations de feu existantes
     */
    public Iterable<FireStation> getAllFireStations() {
        try {
            return fireStationRepository.findAll();
        } catch (Exception exception) {
            logger.error("Erreur lors de la récupération de la liste des stations de feu : " + exception.getMessage() + " Stack Trace + " + exception.getStackTrace());
            return null;
        }
    }

    /**
     * Ajoute une station de feu si elle n'existe pas encore
     *
     * @param fireStation station de feu à ajouter
     * @return null en cas de problème lors de l'ajout, station de feu ajoutée sinon
     */
    public FireStation addFireStation(FireStation fireStation) {
        if (fireStation != null) {
            Optional<FireStation> existingFireStation = fireStationRepository.findFirstByAddressIgnoreCaseAndStation(fireStation.getAddress(), fireStation.getStation());
            if (existingFireStation.isPresent()) {
                logger.error("Erreur lors de l'ajout d'une station de feu déjà existante");
                return null;
            } else {
                try {
                    fireStation.setId(fireStationRepository.save(fireStation).getId());
                } catch (Exception exception) {
                    logger.error("Erreur lors de l'ajout d'une station de feu : " + exception.getMessage() + " Stack Trace : " + exception.getStackTrace());
                    return null;
                }
            }

        }
        return fireStation;
    }

    /**
     * Mise à jour du numéro d'une station de feu si elle existe déjà
     *
     * @param fireStation station de feu à mettre à jour
     * @return null en cas de problème lors de la mise à jour, station de feu mise à jour sinon
     */
    public FireStation updateFireStation(FireStation fireStation) {
        if (fireStation != null) {
            List<FireStation> existingFireStationList = fireStationRepository.findAllByAddressIgnoreCase(fireStation.getAddress());

            if (existingFireStationList.isEmpty()) {
                logger.error("Erreur lors de la mise à jour d'une station de feu inexistante");
                return null;

            } else {

                for(FireStation fireStationToUpdate : existingFireStationList)
                {
                    fireStationToUpdate.setStation(fireStation.getStation());
                    try {
                        fireStationRepository.save(fireStationToUpdate);
                    } catch (Exception exception) {
                        logger.error("Erreur lors de la mise à jour d'une station de feu :" + exception.getMessage() +
                                " Stack Trace : " + exception.getStackTrace());
                        return null;
                    }
                }
                return fireStation;
            }
        }
        return null;
    }

    /**
     * suppression d'une station de feu à partir de l'adresse
     *
     * @param address adresse de la station de feu à supprimer
     * @return nombre de lignes supprimées, null si problème lors de la suppression
     */
    public Integer deleteFireStationByAddress(String address) {
        if (address != null) {
            List<FireStation> existingFireStationList = fireStationRepository.findAllByAddressIgnoreCase(address);
            if (existingFireStationList.isEmpty()) {
                logger.error("Erreur lors de la suppression d'une station de feu inexistante");
                return null;
            } else {
                try {
                    return fireStationRepository.deleteByAddressIgnoreCase(address);
                } catch (Exception exception) {
                    logger.error("Erreur lors de la suppression d'une station de feu : " + exception.getMessage() +
                            " Stack Trace : " + exception.getStackTrace());
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * suppression d'une station de feu à partir de l'adresse et de la station
     *
     * @param address adresse de la station à supprimer
     * @param station numéro de la station à supprimer
     * @return nombre de lignes supprimées, null si problèmes lors de la suppression
     */
    public Integer deleteFireStationByAddressAndStation(String address, Integer station) {
        if (address != null && station != null) {
            Optional<FireStation> existingFireStationList = fireStationRepository.findFirstByAddressIgnoreCaseAndStation(address, station);
            if (existingFireStationList.isPresent()) {
                try {
                    return fireStationRepository.deleteByAddressIgnoreCaseAndStation(address, station);
                } catch (Exception exception) {
                    logger.error("Erreur lors de la suppression d'une station de feu : " + exception.getMessage() +
                            " Stack Trace : " + exception.getStackTrace());
                    return null;
                }
            } else {
                logger.error("Erreur lors de la suppression d'une station de feu inexistante : address : " +
                        address + " , station : " + station.toString());
                return null;
            }
        }
        return null;
    }
}
