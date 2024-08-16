package com.example.service.impl;

import com.example.DTO.EmployeeDTO;
import com.example.model.EmployeeModel;
import com.example.entity.EmployeeEntity;
import com.example.mapper.EmployeeMapper;
import com.example.repository.EmployeeRepository;
import com.example.service.IEmployeeService;
import com.example.utils.ExcelExporter;
import com.example.validate.AddressValidate;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Service
@Data
@NoArgsConstructor
public class EmployeeService implements IEmployeeService {

    private EmployeeRepository employeeRepository;

    private AddressValidate addressValidate;

    private static final Logger LOGGER = LogManager.getLogger(EmployeeService.class);

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, AddressValidate addressValidate) {
        this.employeeRepository = employeeRepository;
        this.addressValidate = addressValidate;
    }

    @Override
    public List<EmployeeModel> findAll() {
        List<EmployeeEntity> employeeEntityList = employeeRepository.findAll();
        List<EmployeeModel> employeeDTOList = new ArrayList<>();
        for (EmployeeEntity employeeEntity : employeeEntityList) {
            employeeDTOList.add(EmployeeMapper.mapToEmployeeModel(employeeEntity));
        }
        return employeeDTOList;
    }

    @Override
    public List<EmployeeModel> findByName(String name) {
        List<EmployeeEntity> employeeEntityList = employeeRepository.findByName(name);
        List<EmployeeModel> employeeDTOList = new ArrayList<>();
        for (EmployeeEntity employeeEntity : employeeEntityList) {
            employeeDTOList.add(EmployeeMapper.mapToEmployeeModel(employeeEntity));
        }
        return employeeDTOList;
    }

    @Override
    public EmployeeModel findOneByCode(String code) {
        EmployeeEntity employeeEntity = employeeRepository.findOneByCode(code);
        return EmployeeMapper.mapToEmployeeModel(employeeEntity);
    }

    @Override
    public Integer add(EmployeeDTO employeeDTO) {
        boolean isExists = employeeRepository.existsByCode(employeeDTO.getCode());
        boolean isValid = new AddressValidate().isValidate(employeeDTO.getAddress());
        if (!isExists && isValid) {
            EmployeeEntity employeeEntity = EmployeeMapper.mapToEmployeeEntity(employeeDTO);
            employeeRepository.save(employeeEntity);
            return 1;
        } else if (!isValid) {
            return 0;
        } else {
            return -1;
        }
    }

    public ResponseEntity<?> findData(String prefix, String query) {
        if ("find-all".equals(prefix) && query == null) {
            List<EmployeeModel> employeeModelList = findAll();
            if (employeeModelList != null && !employeeModelList.isEmpty()) {
                return new ResponseEntity<>(employeeModelList, HttpStatus.OK);
            }
            return new ResponseEntity<>("Employee list is empty", HttpStatus.OK);
        } else if ("search".equals(prefix) && query != null) {
            List<EmployeeModel> employeeModelList = findByName(query);
            if (employeeModelList != null && !employeeModelList.isEmpty()) {
                return new ResponseEntity<>(employeeModelList, HttpStatus.OK);
            }
            return new ResponseEntity<>("There are no employees named " + query, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not found api", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> insertData (EmployeeDTO employeeDTO) {
        Integer save = add(employeeDTO);
        if (save == -1) {
            return new ResponseEntity<>("Employee exists with code " + employeeDTO.getCode(), HttpStatus.CONFLICT);
        } else if(save == 0){
            return new ResponseEntity<>("Address information is incorrect", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Insert success", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> updateData (String code, EmployeeDTO employeeDTO) {
        try {
            EmployeeEntity existingEmployee = employeeRepository.findOneByCode(code);
            boolean isValid = new AddressValidate().isValidate(employeeDTO.getAddress());
            if(existingEmployee == null) {
                return new ResponseEntity<>("Employee not found with code: "+code, HttpStatus.NOT_FOUND);
            }

            if (!isValid) {
                return new ResponseEntity<>("Incorrect address information!", HttpStatus.BAD_REQUEST);
            }
            EmployeeEntity employeeEntity = EmployeeMapper.mapToEmployeeEntity(employeeDTO);
            existingEmployee.setName(employeeEntity.getName());
            existingEmployee.setPhone(employeeEntity.getPhone());
            existingEmployee.setEmail(employeeEntity.getEmail());
            existingEmployee.setAge(employeeEntity.getAge());

            EmployeeEntity updated = employeeRepository.save(existingEmployee);
            EmployeeModel updatedEmployee = EmployeeMapper.mapToEmployeeModel(updated);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        }catch (Exception e) {
            // In ra log để xem lỗi chi tiết
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>("Error updating employee", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (String code) {
        try {
            boolean isExists = employeeRepository.existsByCode(code);
            if (isExists) {
                employeeRepository.deleteByCode(code);
                if (!employeeRepository.existsByCode(code)) return new ResponseEntity<>("Delete success", HttpStatus.OK);
                else return new ResponseEntity<>("Delete fail", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Employee not found with code: "+code, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>("Error deleting employee", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void exportExcel (HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"users.xlsx\"");

        List<String> headerString = new ArrayList<>(Arrays.asList("STT", "Ma nhan vien", "Ten nhan vien", "Email", "So dien thoai", "Tuoi"));
        List<EmployeeModel> employeeModelList = new ArrayList<>();
        List<EmployeeEntity> employeeEntityList = employeeRepository.findAll();
        for (EmployeeEntity employeeEntity : employeeEntityList) {
            employeeModelList.add(EmployeeMapper.mapToEmployeeModel(employeeEntity));
        }
        List<Function<EmployeeModel, Object>> propertyGetters = Arrays.asList(
                EmployeeModel::getId,
                EmployeeModel::getCode,
                EmployeeModel::getName,
                EmployeeModel::getEmail,
                EmployeeModel::getPhone,
                EmployeeModel::getAge
        );

        new ExcelExporter().export(response, headerString, employeeModelList, propertyGetters);
    }
}
