package com.example.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class CertificateDTO {
    @NotBlank(message = "Code is mandatory")
    @Pattern(regexp = "^[^\\s]{6,10}$", message = "Code must be between 6 and 10 characters long and contain no spaces")
    private String code;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @NotNull(message = "Issue date cannot be null")
    @PastOrPresent(message = "Issue date must be in the past or present")
    private Date issueDate;

    @NotNull(message = "Expiry date cannot be null")
    @Future(message = "Expiry date must be in the future")
    private Date expiryDate;

    @Min(value = 0, message = "Province id must be non-negative")
    private Integer provinceId;

    public boolean isEffective() {
        return expiryDate.after(new Date());
    }
}
