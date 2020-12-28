package com.safetynet.alerts.model.mapper;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.CommunityMemberDTO;
import com.safetynet.alerts.utils.DateUtils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;


@Mapper(componentModel = "spring")
public abstract class CommunityMemberDTOMapper {

    @Autowired
    DateUtils dateUtils;

    @Mappings({
            @Mapping(target = "firstName", source = "person.firstName"),
            @Mapping(target = "lastName", source = "person.lastName"),
            @Mapping(target = "address", source = "person.address"),
            @Mapping(target = "phone", source = "person.phone"),
            @Mapping(target = "age", source = "medicalRecord.birthDate", qualifiedByName = "calculateAge")
    })
    public abstract CommunityMemberDTO personToCommunityMemberDTO(Person person, MedicalRecord medicalRecord);

    @Named("calculateAge")
    public int getAge(LocalDate birthDate) {
        return dateUtils.getAge(birthDate);
    }
}
