package com.example.service.impl;

import com.example.DTO.CertificateDTO;
import com.example.entity.CertificateEntity;
import com.example.mapper.CertificateMapper;
import com.example.model.CertificateModel;
import com.example.repository.CertificateRepository;
import com.example.service.ICertificateService;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class CertificateService implements ICertificateService {

    private CertificateRepository certificateRepository;

    private static final Logger LOGGER = LogManager.getLogger(CertificateService.class);

    @Autowired
    public CertificateService(CertificateRepository certificateRepository){
        this.certificateRepository = certificateRepository;
    }

    @Override
    public List<CertificateModel> findALL() {
        List<CertificateEntity> certificateEntities = certificateRepository.findAll();
        List<CertificateModel> certificateModels = new ArrayList<>();
        for(CertificateEntity certificateEntity : certificateEntities) {
            certificateModels.add(CertificateMapper.mapToModel(certificateEntity));
        }
        return certificateModels;
    }

    @Override
    public List<CertificateModel> findByName(String name) {
        List<CertificateEntity> certificateEntities = certificateRepository.findByName(name);
        List<CertificateModel> certificateModels = new ArrayList<>();
        for(CertificateEntity certificateEntity : certificateEntities) {
            certificateModels.add(CertificateMapper.mapToModel(certificateEntity));
        }
        return certificateModels;
    }

    @Override
    public CertificateModel findById(Integer id) {
        CertificateEntity certificateEntity = certificateRepository.findOneById(id);
        return CertificateMapper.mapToModel(certificateEntity);
    }

    @Override
    public CertificateModel add(CertificateDTO certificateDTO) {
        CertificateEntity certificateEntity = CertificateMapper.mapToEntity(certificateDTO);
        boolean isExists = certificateRepository.existsByCode(certificateDTO.getCode());
        if(!isExists) {
            CertificateEntity saveCertificate = certificateRepository.save(certificateEntity);
            return CertificateMapper.mapToModel(saveCertificate);
        }
        return null;
    }

    public ResponseEntity<?> findData(String prefix, String query) {
        if("find-all".equals(prefix) && query == null) {
            List<CertificateModel> certificateModels = findALL();
            if(certificateModels != null && !certificateModels.isEmpty()) {
                return new ResponseEntity<>(certificateModels, HttpStatus.OK);
            }
            return new ResponseEntity<>("Certificate list is empty", HttpStatus.OK);
        } else if("search".equals(prefix) && query != null) {
            List<CertificateModel> certificateModels = findByName(query);
            if(certificateModels != null && !certificateModels.isEmpty()) {
                return new ResponseEntity<>(certificateModels, HttpStatus.OK);
            }
            return new ResponseEntity<>("There are no certificates named " + query, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not found API", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> insertData(CertificateDTO certificateDTO) {
        CertificateModel saveCertificate = add(certificateDTO);
        if (saveCertificate == null) {
            return new ResponseEntity<>("Certificate exists with code " + certificateDTO.getCode(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(saveCertificate, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> updateData (String code, CertificateDTO certificateDTO) {
        try {
            CertificateEntity certificateEntity = CertificateMapper.mapToEntity(certificateDTO);
            CertificateEntity existsCertificate = certificateRepository.findOneByCode(code);
            if (existsCertificate == null) {
                return new ResponseEntity<>("Certificate not found with code: " + code, HttpStatus.NOT_FOUND);
            }
            existsCertificate.setName(certificateEntity.getName());
            existsCertificate.setType(certificateEntity.getType());
            existsCertificate.setIssueDate(certificateEntity.getIssueDate());
            existsCertificate.setExpiryDate(certificateEntity.getExpiryDate());
            CertificateEntity updated = certificateRepository.save(existsCertificate);
            return new ResponseEntity<>(CertificateMapper.mapToModel(updated), HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return new ResponseEntity<>("Error updating certificate", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData(String code) {
        try {
            boolean isExists = certificateRepository.existsByCode(code);
            if (isExists) {
                certificateRepository.deleteByCode(code);
                CertificateEntity certificateEntity = certificateRepository.findOneByCode(code);
                if(certificateEntity != null) return new ResponseEntity<>("Delete fail", HttpStatus.INTERNAL_SERVER_ERROR);
                return new ResponseEntity<>("Delete success", HttpStatus.OK);
            }
            return new ResponseEntity<>("Certificate not found with code: "+code, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>("Error deleting certificate", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
