package com.example.mapper;

import com.example.DTO.CommuneDTO;
import com.example.entity.CommuneEntity;
import com.example.entity.DistrictEntity;
import com.example.model.CommuneModel;
import com.example.service.impl.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommuneMapper {

    private static DistrictService districtService;

    public CommuneMapper(){}

    @Autowired
    public CommuneMapper(DistrictService districtService) {
        CommuneMapper.districtService = districtService;
    }

    public static CommuneEntity mapToEntity (CommuneDTO communeDTO) {
        CommuneEntity communeEntity = new CommuneEntity();
        DistrictEntity districtEntity = districtService.findOneById(communeDTO.getDistrictId());
        communeEntity.setCode(communeDTO.getCode());
        communeEntity.setName(communeDTO.getName());
        communeEntity.setPopulation(communeDTO.getPopulation());
        communeEntity.setDistrict(districtEntity);
        return communeEntity;
    }

    public static CommuneModel mapToModel (CommuneEntity communeEntity) {
        CommuneModel communeModel = new CommuneModel();
        communeModel.setCode(communeEntity.getCode());
        communeModel.setName(communeEntity.getName());
        communeModel.setPopulation(communeEntity.getPopulation());
        communeModel.setDistrictModel(DistrictMapper.mapToDistrictModel(communeEntity.getDistrict()));
        return communeModel;
    }
}
