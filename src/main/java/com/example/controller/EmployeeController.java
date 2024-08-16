package com.example.controller;

import com.example.DTO.EmployeeDTO;
import com.example.model.EmployeeModel;
import com.example.service.impl.EmployeeService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{prefix}")
    public ResponseEntity<?> getData(@PathVariable String prefix, @RequestParam(required = false) String query) {
         return employeeService.findData(prefix, query);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody EmployeeDTO employeeDTO, BindingResult result) {
        if(result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }
        return employeeService.insertData(employeeDTO);
    }

    @PutMapping("/update/{code}")
    public ResponseEntity<?> update (@Valid @RequestBody EmployeeDTO employeeDTO, @PathVariable String code, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }
        return employeeService.updateData(code, employeeDTO);
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<?> delete (@PathVariable String code) {
        return employeeService.deleteData(code);
    }

    @GetMapping("/export-excel")
    public void exportExcel (HttpServletResponse response) throws IOException {
        employeeService.exportExcel(response);
    }
}
