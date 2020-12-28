package com.safetynet.alerts.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.alerts.utils.Constants;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FireStationCommunityDTO {

    @JsonView(Views.Public.class)
    private List<CommunityMemberDTO> communityMemberDTOList = new ArrayList<>();
    @JsonView(Views.Public.class)
    private long adultsCount;
    @JsonView(Views.Public.class)
    private long childrenCount;

    public long getAdultsCount() {
        adultsCount = communityMemberDTOList.stream().filter(communityMemberDTO -> communityMemberDTO.getAge() > Constants.AGE_MAX_CHILDREN).count();
        return adultsCount;
    }

    public long getChildrenCount() {
        childrenCount = communityMemberDTOList.stream().filter(communityMemberDTO -> communityMemberDTO.getAge() <= Constants.AGE_MAX_CHILDREN && communityMemberDTO.getAge()>=0).count();
        return childrenCount;
    }
}
