package com.example.response;

import com.example.DTO.DistrictDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProvinceResponse {
    private String code;
    private String name;
    private List<DistrictDTO> districts = new ArrayList<>();
}
