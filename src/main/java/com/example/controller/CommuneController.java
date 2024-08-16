package com.example.controller;

import com.example.DTO.CommuneDTO;
import com.example.service.impl.CommuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commune")
public class CommuneController {
    @Autowired
    private CommuneService communeService;

    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData(@PathVariable String prefix, @RequestParam(required = false) String query) {
        return communeService.findData(prefix, query);
    }

    @PostMapping("/add")
    public ResponseEntity<?> insertData(@RequestBody CommuneDTO communeDTO) {
        return communeService.insertData(communeDTO);
    }

    @PutMapping("/update/{code}")
    public ResponseEntity<?> updateData(@PathVariable String code, @RequestBody CommuneDTO communeDTO) {
        return communeService.updateData(code, communeDTO);
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<?> deleteData(@PathVariable String code) {
        return communeService.deleteData(code);
    }
}
