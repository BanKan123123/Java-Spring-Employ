package com.example.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DistrictDTO {
    private String code;
    private String name;
    private Integer provinceId;
    private List<CommuneDTO> communes = new ArrayList<>();
}
