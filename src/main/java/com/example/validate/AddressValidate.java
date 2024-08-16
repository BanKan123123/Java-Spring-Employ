package com.example.validate;

import com.example.DTO.AddressDTO;
import com.example.entity.CommuneEntity;
import com.example.entity.DistrictEntity;
import com.example.entity.ProvinceEntity;
import com.example.mapper.DistrictMapper;
import com.example.mapper.ProvinceMapper;
import com.example.model.ProvinceModel;
import com.example.repository.CommuneRepository;
import com.example.repository.DistrictRepository;
import com.example.repository.ProvinceRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressValidate {
    private static final Logger LOGGER = LogManager.getLogger(AddressValidate.class);
    private static ProvinceRepository provinceRepository;
    private static DistrictRepository districtRepository;
    private static CommuneRepository communeRepository;

    public AddressValidate (){}

    @Autowired
    public AddressValidate(ProvinceRepository provinceRepository, DistrictRepository districtRepository, CommuneRepository communeRepository) {
        AddressValidate.provinceRepository = provinceRepository;
        AddressValidate.districtRepository = districtRepository;
        AddressValidate.communeRepository = communeRepository;
    }

    public boolean isValidate(AddressDTO addressDTO) {

        if (provinceRepository.findOneByName(addressDTO.getProvinceName()) == null) {
            LOGGER.error("Province not found: " + addressDTO.getProvinceName());
            return false;
        }

        ProvinceEntity provinceEntity = provinceRepository.findOneByName(addressDTO.getProvinceName());


        if (districtRepository.findOneByName(addressDTO.getDistrictName()) == null) {
            LOGGER.error("District not found: " + addressDTO.getDistrictName());
            return false;
        }

        DistrictEntity districtEntity = districtRepository.findOneByName(addressDTO.getDistrictName());

        if (!districtEntity.getProvince().getId().equals(provinceEntity.getId())) {
            LOGGER.error(districtEntity.getName() + " district is not in " + provinceEntity.getName() + " province.");
            return false;
        }

        if (communeRepository.findOneByName(addressDTO.getCommuneName()) == null) {
            LOGGER.error("Commune not found: " + addressDTO.getCommuneName());
            return false;
        }

        CommuneEntity communeEntity = communeRepository.findOneByName(addressDTO.getCommuneName());

        if (!communeEntity.getDistrict().getId().equals(districtEntity.getId())) {
            LOGGER.error(communeEntity.getName() + " commune is not in " + districtEntity.getName() + " district.");
            return false;
        }

        return true;
    }
}
