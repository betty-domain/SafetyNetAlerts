package com.safetynet.alerts.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.safetynet.alerts.model.mapper.FloodInfoDTOMapper;
import com.sun.scenario.effect.Flood;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StationFloodInfoDTO {
    private String address;

    @JsonProperty("homes")
    private List<FloodInfoDTO> floodInfoDTOList = new ArrayList<>();
}
