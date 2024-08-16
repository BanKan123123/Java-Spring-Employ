package com.example.DTO;

import lombok.Data;

@Data
public class CommuneDTO {
    private String code;
    private String name;
    private Integer population;
    private Integer districtId;
}
