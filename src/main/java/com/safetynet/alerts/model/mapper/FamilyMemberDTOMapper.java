package com.safetynet.alerts.model.mapper;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.dto.FamilyMemberDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FamilyMemberDTOMapper {

    FamilyMemberDTO personToFamilyMemberDTO(Person person);

    List<FamilyMemberDTO> personListToFamilyMemberDTOList(List<Person> personList);
}
