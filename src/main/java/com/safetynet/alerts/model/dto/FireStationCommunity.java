package com.safetynet.alerts.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FireStationCommunity {

    @JsonView(Views.Public.class)
    private List<CommunityMemberDTO> communityMemberDTOList = new ArrayList<>();
    @JsonView(Views.Public.class)
    private long adultsCount;
    @JsonView(Views.Public.class)
    private long childsCount;

    public long getAdultsCount() {

        return communityMemberDTOList.stream().filter(communityMemberDTO -> communityMemberDTO.getAge() > 18).count();
    }

    public long getChildsCount() {
        return communityMemberDTOList.stream().filter(communityMemberDTO -> communityMemberDTO.getAge() <= 18).count();
    }
}
