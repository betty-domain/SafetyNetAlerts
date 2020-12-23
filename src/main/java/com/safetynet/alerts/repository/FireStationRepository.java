package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface FireStationRepository extends CrudRepository<FireStation,Long> {

    List<FireStation> findDistinctByStation(Integer station);

    Optional<FireStation> findFirstByAddressIgnoreCaseAndStation(String address, Integer station);

    List<FireStation> findAllByAddressIgnoreCase(String address);

    @Transactional
    Integer deleteByAddressIgnoreCase(String address);

    @Transactional
    Integer deleteByAddressIgnoreCaseAndStation(String address, Integer station);
}
