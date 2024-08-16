package com.example.service;

import com.example.DTO.EmployeeDTO;
import com.example.model.EmployeeModel;

import java.util.List;

public interface IEmployeeService {
    List<EmployeeModel> findAll();

    List<EmployeeModel> findByName(String name);

    EmployeeModel findOneByCode(String code);

    Integer add (EmployeeDTO employeeDTO);
}
