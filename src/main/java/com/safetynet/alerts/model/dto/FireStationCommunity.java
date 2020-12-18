package com.safetynet.alerts.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FireStationCommunity {

    private List<CommunityMember> communityMemberList = new ArrayList<>();
    private long adultsCount;
    private long childCount;

    public long getAdultsCount() {

        return communityMemberList.stream().filter(communityMember -> communityMember.getAge() > 18).count();
    }

    public long getChildCount() {
        return communityMemberList.stream().filter(communityMember -> communityMember.getAge() <= 18).count();
    }
}
