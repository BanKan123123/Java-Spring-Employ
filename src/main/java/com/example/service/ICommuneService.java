package com.example.service;

import com.example.DTO.CommuneDTO;
import com.example.entity.CommuneEntity;
import com.example.model.CommuneModel;
import com.example.response.DistrictResponse;

import java.util.List;

public interface ICommuneService {
    List<CommuneModel> findAll();
    List<CommuneModel> findByName(String name);
    DistrictResponse findByDistrictId(Integer districtId);
    CommuneEntity findOneById(Integer id);
    Integer getCommuneId(String communeName);
    CommuneModel add (CommuneDTO communeDTO);
}
