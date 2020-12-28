package com.safetynet.alerts.model.mapper;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.ChildAlertDTO;
import com.safetynet.alerts.utils.DateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Mapper(componentModel = "spring", uses = FamilyMemberDTOMapper.class)
public abstract class ChildAlertDTOMapper {

    @Mappings({
            @Mapping(target="lastName", source="person.lastName"),
            @Mapping(target="firstName", source="person.firstName"),
            @Mapping(target="age", source="medicalRecord.birthDate", qualifiedByName="calculateAge"),
    })
    public abstract ChildAlertDTO convertToChildAlertDTO(Person person, MedicalRecord medicalRecord);

    @Autowired
    DateUtils dateUtils;

    @Named("calculateAge")
    public int getAge(LocalDate birthDate) {
        return dateUtils.getAge(birthDate);
    }
}
