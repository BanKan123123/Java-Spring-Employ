package com.example.model;

import lombok.Data;

@Data
public class CommuneModel {
    private String code;
    private String name;
    private Integer population;
    private DistrictModel districtModel;
}
