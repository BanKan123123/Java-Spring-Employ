package com.example.model;

import lombok.Data;

import java.util.List;

@Data
public class DistrictModel {
    private Integer id;
    private String code;
    private String name;
    private List<String> communeNames;
    private ProvinceModel provinceModel;
}
