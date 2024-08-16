package com.example.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDTO {

    @NotBlank(message = "Province name is mandatory")
    private String provinceName;

    @NotBlank(message = "District name is mandatory")
    private String districtName;

    @NotBlank(message = "Commune name is mandatory")
    private String communeName;
}
