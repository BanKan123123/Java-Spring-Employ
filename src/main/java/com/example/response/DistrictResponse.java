package com.example.response;

import com.example.DTO.CommuneDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DistrictResponse {
    private String code;
    private String name;
    private List<CommuneDTO> communes = new ArrayList<>();
}
