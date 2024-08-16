package com.example.controller;

import com.example.DTO.CertificateDTO;
import com.example.service.impl.CertificateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("certificate")
public class CertificateController {
    @Autowired
    private CertificateService certificateService;

    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData(@PathVariable String prefix, @RequestParam(required = false) String query) {
        return certificateService.findData(prefix, query);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add (@Valid @RequestBody CertificateDTO certificateDTO, BindingResult result) {
        if(result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }
        return certificateService.insertData(certificateDTO);
    }

    @PutMapping("/update/{code}")
    public ResponseEntity<?> updateData(@Valid @RequestBody CertificateDTO certificateDTO, @PathVariable String code, BindingResult result) {
        if(result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }
        return certificateService.updateData(code, certificateDTO);
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<?> deleteData (@PathVariable String code) {
        return certificateService.deleteData(code);
    }
}
