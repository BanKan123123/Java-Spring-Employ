package com.example.service;

import com.example.DTO.DistrictDTO;
import com.example.entity.DistrictEntity;
import com.example.model.DistrictModel;
import com.example.response.ProvinceResponse;

import java.util.List;

public interface IDistrictService {
    List<DistrictModel> findAll ();
    List<DistrictModel> findByName(String name);
    ProvinceResponse findByProvinceId(Integer id);
    DistrictEntity findOneById(Integer id);
    Integer getDistrictId(String districtName);
    DistrictModel add (DistrictDTO districtDTO);
}
