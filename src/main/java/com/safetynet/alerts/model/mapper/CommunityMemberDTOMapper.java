package com.safetynet.alerts.model.mapper;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.CommunityMemberDTO;
import com.safetynet.alerts.utils.DateUtils;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommunityMemberDTOMapper {

    DateUtils dateUtils = new DateUtils();

    @Mappings({
            @Mapping(target="firstName", source="person.firstName"),
            @Mapping(target="lastName", source="person.lastName"),
            @Mapping(target="address", source="person.address"),
            @Mapping(target="phone", source="person.phone"),
            @Mapping(target="age", source="medicalRecord.birthDate", qualifiedByName="calculateAge")
    })
    CommunityMemberDTO personToCommunityMemberDTO(Person person, MedicalRecord medicalRecord);


    @Named("calculateAge")
    public static int getAge(LocalDate birthDate) {
        return dateUtils.getAge(birthDate);
    }
}
