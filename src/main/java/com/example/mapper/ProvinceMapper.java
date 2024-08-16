package com.example.mapper;

import com.example.entity.DistrictEntity;
import com.example.model.DistrictModel;
import com.example.model.ProvinceModel;
import com.example.entity.ProvinceEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProvinceMapper {
    public static ProvinceModel mapToProvinceModel (ProvinceEntity provinceEntity) {
        ProvinceModel provinceModel = new ProvinceModel();

        provinceModel.setId(provinceEntity.getId());
        provinceModel.setCode(provinceEntity.getCode());
        provinceModel.setName(provinceEntity.getName());

        Set<DistrictEntity> districtEntities = provinceEntity.getDistrictEntities();
        List<String> districtNames = new ArrayList<>();

        for(DistrictEntity districtEntity : districtEntities) {
            districtNames.add(districtEntity.getName());
        }

        provinceModel.setDistrictName(districtNames);

        return provinceModel;
    }

    public static ProvinceEntity mapToProvinceEntity (ProvinceModel provinceModel) {
        ProvinceEntity provinceEntity = new ProvinceEntity();

        provinceEntity.setId(provinceModel.getId());
        provinceEntity.setCode(provinceModel.getCode());
        provinceEntity.setName(provinceModel.getName());

        return provinceEntity;
    }
}
