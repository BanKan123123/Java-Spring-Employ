package com.example.model;

import lombok.Data;

import java.util.List;

@Data
public class ProvinceModel {
    private Integer id;
    private String code;
    private String name;
    private List<String> districtName;
}
