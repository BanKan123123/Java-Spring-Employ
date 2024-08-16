package com.example.service;

import com.example.entity.ProvinceEntity;
import com.example.model.ProvinceModel;

import java.util.List;

public interface IProvinceService {
    List<ProvinceModel> findAll();

    List<ProvinceModel> findByName(String name);

    ProvinceModel findOneByCode(String code);

    ProvinceEntity findOneById(Integer id);

    Integer getProvinceId (String provinceName);

    ProvinceModel add (ProvinceModel provinceModel);
}
