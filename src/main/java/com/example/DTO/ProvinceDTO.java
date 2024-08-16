package com.example.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ProvinceDTO {
    private String code;
    private String name;
    List<DistrictDTO> districts;
}
