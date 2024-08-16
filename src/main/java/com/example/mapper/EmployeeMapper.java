package com.example.mapper;

import com.example.DTO.EmployeeDTO;
import com.example.model.EmployeeModel;
import com.example.entity.EmployeeEntity;
import com.example.service.impl.CommuneService;
import com.example.service.impl.DistrictService;
import com.example.service.impl.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    private static ProvinceService provinceService;
    private static DistrictService districtService;
    private static CommuneService communeService;

    public EmployeeMapper () {}

    @Autowired
    public EmployeeMapper (ProvinceService provinceService, DistrictService districtService, CommuneService communeService) {
        EmployeeMapper.provinceService = provinceService;
        EmployeeMapper.districtService = districtService;
        EmployeeMapper.communeService = communeService;
    }

    public static EmployeeModel mapToEmployeeModel (EmployeeEntity employeeEntity) {
        EmployeeModel employeeModel = new EmployeeModel();
        employeeModel.setId(employeeEntity.getId());
        employeeModel.setCode(employeeEntity.getCode());
        employeeModel.setName(employeeEntity.getName());
        employeeModel.setEmail(employeeEntity.getEmail());
        employeeModel.setPhone(employeeEntity.getPhone());
        employeeModel.setAge(employeeEntity.getAge());
        return employeeModel;
    }

    public static EmployeeEntity mapToEmployeeEntity (EmployeeModel employeeModel) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setCode(employeeModel.getCode());
        employeeEntity.setName(employeeModel.getName());
        employeeEntity.setEmail(employeeModel.getEmail());
        employeeEntity.setPhone(employeeModel.getPhone());
        employeeEntity.setAge(employeeModel.getAge());
        return employeeEntity;
    }

    public static EmployeeEntity mapToEmployeeEntity (EmployeeDTO employeeDTO) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setCode(employeeDTO.getCode());
        employeeEntity.setName(employeeDTO.getName());
        employeeEntity.setEmail(employeeDTO.getEmail());
        employeeEntity.setPhone(employeeDTO.getPhone());
        employeeEntity.setAge(employeeDTO.getAge());
        employeeEntity.setProvinceId(provinceService.getProvinceId(employeeDTO.getAddress().getProvinceName()));
        employeeEntity.setDistrictId(districtService.getDistrictId(employeeDTO.getAddress().getDistrictName()));
        employeeEntity.setCommuneId(communeService.getCommuneId(employeeDTO.getAddress().getCommuneName()));
        return employeeEntity;
    }
}
