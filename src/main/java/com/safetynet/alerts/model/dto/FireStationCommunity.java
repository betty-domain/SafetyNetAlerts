package com.safetynet.alerts.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FireStationCommunity {

    private List<CommunityMemberDTO> communityMemberDTOList = new ArrayList<>();
    private long adultsCount;
    private long childsCount;

    public long getAdultsCount() {

        return communityMemberDTOList.stream().filter(communityMemberDTO -> communityMemberDTO.getAge() > 18).count();
    }

    public long getChildsCount() {
        return communityMemberDTOList.stream().filter(communityMemberDTO -> communityMemberDTO.getAge() <= 18).count();
    }
}
