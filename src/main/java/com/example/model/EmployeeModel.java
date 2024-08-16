package com.example.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmployeeModel {

    private Integer id;

    @NotBlank(message = "Code is mandatory")
    @Pattern(regexp = "^[^\\s]{6,10}$", message = "Code must be between 6 and 10 characters long and contain no spaces")
    private String code;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone is mandatory")
    @Pattern(regexp = "^+(84|0)?[0-9]{10}$", message = "Phone number must contain only digits and be up to 10 characters long")
    private String phone;

    @Min(value = 0, message = "Age must be non-negative")
    private Integer age;
}
