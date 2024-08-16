package com.example.controller;

import com.example.DTO.ProvinceDTO;
import com.example.model.ProvinceModel;
import com.example.service.impl.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/province")
public class ProvinceController {

    @Autowired
    private ProvinceService provinceService;

    @GetMapping("/{prefix}")
    public ResponseEntity<?> getData (@PathVariable String prefix, @RequestParam(required = false) String query) {
        return provinceService.findData(prefix, query);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add (@RequestBody ProvinceModel provinceModel) {
        return provinceService.ínsertData(provinceModel);
    }

    @PostMapping("/add-province-with-districts")
    public ResponseEntity<?> addProvinceWithDistricts(@RequestBody ProvinceDTO provinceDTO) {
        return provinceService.addProvinceWithDistricts(provinceDTO);
    }

    @PutMapping("/update-province-with-districts")
    public ResponseEntity<?> updateProvinceWithDistricts(@RequestBody ProvinceDTO provinceDTO) {
        return provinceService.updateProvinceWithDistricts(provinceDTO);
    }

    @PutMapping("/update/{code}")
    public ResponseEntity<?> update (@PathVariable String code, @RequestBody ProvinceModel provinceModel) {
        return provinceService.updateData(code, provinceModel);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete (@PathVariable Integer id) {
        return provinceService.deleteProvince(id);
    }
}
