package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
    Optional<Person> findByFirstNameAndLastNameAllIgnoreCase(String firstname, String lastname);

    @Transactional
    void deletePersonByFirstNameAndLastNameAllIgnoreCase(String firstname, String lastname);
}
