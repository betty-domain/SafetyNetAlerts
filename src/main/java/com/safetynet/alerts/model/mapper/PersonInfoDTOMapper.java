package com.safetynet.alerts.model.mapper;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.CommunityMemberDTO;
import com.safetynet.alerts.model.dto.PersonInfoDTO;
import com.safetynet.alerts.utils.DateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface PersonInfoDTOMapper {

    DateUtils dateUtils = new DateUtils();

    @Mappings({
            @Mapping(target="lastname", source="person.lastName"),
            @Mapping(target="address", source="person.address"),
            @Mapping(target="mail", source="person.email"),
            @Mapping(target="age", source="medicalRecord.birthDate", qualifiedByName="calculateAge"),
            @Mapping(target="medicationList", source="medicalRecord.medications"),
            @Mapping(target="allergiesList", source="medicalRecord.allergies")
    })
    PersonInfoDTO personToPersonInfoDTO(Person person, MedicalRecord medicalRecord);


    @Named("calculateAge")
    public static int getAge(LocalDate birthDate) {
        return dateUtils.getAge(birthDate);
    }
}
