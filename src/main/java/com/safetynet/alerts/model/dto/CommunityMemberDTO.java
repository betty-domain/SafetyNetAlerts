package com.safetynet.alerts.model.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class CommunityMemberDTO {

    @JsonView(Views.Public.class)
    private String firstName;

    @JsonView(Views.Public.class)
    private String lastName;

    @JsonView(Views.Public.class)
    private String address;

    @JsonView(Views.Public.class)
    private String phone;

    @JsonView(Views.Private.class)
    private int age = Integer.MIN_VALUE;

}
