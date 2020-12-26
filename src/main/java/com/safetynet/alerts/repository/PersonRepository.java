package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
    Optional<Person> findByFirstNameAndLastNameAllIgnoreCase(String firstname, String lastname);

    List<Person> findAllByLastNameAllIgnoreCase(String lastname);

    List<Person> findAllByAddressInOrderByAddress(List<String> addressList);

    @Transactional
    Integer deletePersonByFirstNameAndLastNameAllIgnoreCase(String firstname, String lastname);

    List<Person> findAllByCityIgnoreCase(String city);
}
