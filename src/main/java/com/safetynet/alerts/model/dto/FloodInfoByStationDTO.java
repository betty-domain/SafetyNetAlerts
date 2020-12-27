package com.safetynet.alerts.model.dto;

import com.safetynet.alerts.model.mapper.FloodInfoDTOMapper;
import com.sun.scenario.effect.Flood;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FloodInfoByStationDTO {
    private String address;

    private List<FloodInfoDTO> floodInfoDTOList= new ArrayList<>();
}
