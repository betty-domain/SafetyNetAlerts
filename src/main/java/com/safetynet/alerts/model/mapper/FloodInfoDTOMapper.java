package com.safetynet.alerts.model.mapper;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

import com.safetynet.alerts.model.dto.FloodInfoDTO;
import com.safetynet.alerts.utils.DateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface FloodInfoDTOMapper {
    DateUtils dateUtils = new DateUtils();

    @Mappings({
            @Mapping(target="lastname", source="person.lastName"),
            @Mapping(target="phone", source="person.phone"),
            @Mapping(target="medicationList", source="medicalRecord.medications"),
            @Mapping(target="allergiesList", source="medicalRecord.allergies"),
            @Mapping(target="age", source="medicalRecord.birthDate", qualifiedByName="calculateAge")
    })
    FloodInfoDTO convertToFloodInfoDTO(Person person, MedicalRecord medicalRecord);


    @Named("calculateAge")
    public static int getAge(LocalDate birthDate) {
        return dateUtils.getAge(birthDate);
    }
}
