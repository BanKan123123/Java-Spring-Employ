package com.example.controller;

import com.example.DTO.DistrictDTO;
import com.example.service.impl.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/district")
public class DistrictController {

    @Autowired
    private DistrictService districtService;

    @GetMapping("/{prefix}")
    public ResponseEntity<?> findData (@PathVariable String prefix, @RequestParam(required = false) String query) {
        return districtService.findData(prefix, query);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add (@RequestBody DistrictDTO districtDTO) {
        return districtService.insertData(districtDTO);
    }

    @PostMapping("/add-district-with-communes")
    public ResponseEntity<?> addDistrictWithCommunes(@RequestBody DistrictDTO districtDTO) {
        return districtService.addDistrictWithCommune(districtDTO);
    }

    @PutMapping("/update/{code}")
    public ResponseEntity<?> update (@PathVariable(name = "code") String code, @RequestBody DistrictDTO districtDTO) {
        return districtService.updateData(code, districtDTO);
    }

    @PutMapping("/update-district-with-communes")
    public ResponseEntity<?> updateDistrictWithCommunes(@RequestBody DistrictDTO districtDTO) {
        return districtService.updateDistrictWithCommunes(districtDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete (@PathVariable(name = "id") Integer id) {
        return districtService.deleteData(id);
    }
}
