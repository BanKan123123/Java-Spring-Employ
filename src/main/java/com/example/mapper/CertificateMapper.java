package com.example.mapper;

import com.example.DTO.CertificateDTO;
import com.example.entity.CertificateEntity;
import com.example.entity.ProvinceEntity;
import com.example.model.CertificateModel;
import com.example.service.impl.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CertificateMapper {

    private static ProvinceService provinceService;

    public CertificateMapper() {}

    @Autowired
    public CertificateMapper(ProvinceService provinceService) {
        CertificateMapper.provinceService = provinceService;
    }

    public static CertificateEntity mapToEntity (CertificateDTO certificateDTO) {
        CertificateEntity certificateEntity = new CertificateEntity();
        ProvinceEntity provinceEntity = provinceService.findOneById(certificateDTO.getProvinceId());
        certificateEntity.setCode(certificateDTO.getCode());
        certificateEntity.setName(certificateDTO.getName());
        certificateEntity.setType(certificateDTO.getType());
        certificateEntity.setIssueDate(certificateDTO.getIssueDate());
        certificateEntity.setExpiryDate(certificateDTO.getExpiryDate());
        certificateEntity.setProvince(provinceEntity);
        return certificateEntity;
    }

    public static CertificateModel mapToModel(CertificateEntity certificateEntity) {
        CertificateModel certificateModel = new CertificateModel();
        certificateModel.setId(certificateEntity.getId());
        certificateModel.setCode(certificateEntity.getCode());
        certificateModel.setName(certificateEntity.getName());
        certificateModel.setType(certificateEntity.getType());
        certificateModel.setIssueDate(certificateEntity.getIssueDate());
        certificateModel.setExpiryDate(certificateEntity.getExpiryDate());
        certificateModel.setProvinceName(certificateEntity.getProvince().getName());
        return certificateModel;
    }
}
