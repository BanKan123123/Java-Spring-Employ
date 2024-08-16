package com.example.mapper;

import com.example.DTO.DistrictDTO;
import com.example.entity.CommuneEntity;
import com.example.entity.DistrictEntity;
import com.example.entity.ProvinceEntity;
import com.example.model.DistrictModel;
import com.example.service.impl.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DistrictMapper {

    private static ProvinceService provinceService;

    public DistrictMapper() {
    }

    @Autowired
    public DistrictMapper(ProvinceService provinceService) {
        DistrictMapper.provinceService = provinceService;
    }

    public static DistrictEntity mapToDistrictEntity (DistrictDTO districtDTO) {
        DistrictEntity districtEntity = new DistrictEntity();
        ProvinceEntity provinceEntity = provinceService.findOneById(districtDTO.getProvinceId());
        districtEntity.setProvince(provinceEntity);
        districtEntity.setCode(districtDTO.getCode());
        districtEntity.setName(districtDTO.getName());
        return districtEntity;
    }

    public static DistrictModel mapToDistrictModel (DistrictEntity districtEntity) {
        DistrictModel districtModel = new DistrictModel();
        districtModel.setId(districtEntity.getId());
        districtModel.setCode(districtEntity.getCode());
        districtModel.setName(districtEntity.getName());
        districtModel.setProvinceModel(ProvinceMapper.mapToProvinceModel(districtEntity.getProvince()));

        Set<CommuneEntity> communeEntities = districtEntity.getCommuneEntities();

        List<String> communeNames = new ArrayList<>();

        for(CommuneEntity communeEntity : communeEntities)  {
            communeNames.add(communeEntity.getName());
        }

        districtModel.setCommuneNames(communeNames);

        return districtModel;
    }
}
