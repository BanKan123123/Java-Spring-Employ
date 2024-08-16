package com.example.service.impl;

import com.example.DTO.CommuneDTO;
import com.example.entity.CommuneEntity;
import com.example.entity.DistrictEntity;
import com.example.mapper.CommuneMapper;
import com.example.model.CommuneModel;
import com.example.repository.CommuneRepository;
import com.example.repository.DistrictRepository;
import com.example.response.DistrictResponse;
import com.example.service.ICommuneService;
import jakarta.transaction.Transactional;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommuneService implements ICommuneService {

    private static final Logger LOGGER = LogManager.getLogger(DistrictService.class);

    private CommuneRepository communeRepository;
    private DistrictRepository districtRepository;

    @Autowired
    public CommuneService(CommuneRepository communeRepository, DistrictRepository districtRepository) {
        this.communeRepository = communeRepository;
        this.districtRepository = districtRepository;
    }

    @Override
    public List<CommuneModel> findAll() {

        List<CommuneEntity> communeEntities = communeRepository.findAll();
        List<CommuneModel> communeModels = new ArrayList<>();
        for (CommuneEntity communeEntity : communeEntities) {
            communeModels.add(CommuneMapper.mapToModel(communeEntity));
        }
        return communeModels;
    }

    @Override
    public List<CommuneModel> findByName(String name) {
        List<CommuneEntity> communeEntities = communeRepository.findByName(name);
        List<CommuneModel> communeModels = new ArrayList<>();
        for (CommuneEntity communeEntity : communeEntities) {
            communeModels.add(CommuneMapper.mapToModel(communeEntity));
        }
        return communeModels;
    }

    @Override
    public DistrictResponse findByDistrictId(Integer districtId) {
        DistrictEntity districtEntity = districtRepository.findOneById(districtId);
        List<CommuneEntity> communeEntities = communeRepository.findByDistrictId(districtId);
        DistrictResponse districtResponse = new DistrictResponse();
        districtResponse.setCode(districtEntity.getCode());
        districtResponse.setName(districtEntity.getName());

        for(CommuneEntity communeEntity : communeEntities) {
            CommuneDTO communeDTO = new CommuneDTO();
            communeDTO.setCode(communeEntity.getCode());
            communeDTO.setName(communeEntity.getName());
            communeDTO.setPopulation(communeEntity.getPopulation());
            communeDTO.setDistrictId(communeEntity.getDistrict().getId());
            districtResponse.getCommunes().add(communeDTO);
        }
        return districtResponse;
    }

    @Override
    public CommuneEntity findOneById(Integer id) {
        return communeRepository.findOneById(id);
    }

    @Override
    public Integer getCommuneId(String communeName) {
        return communeRepository.findOneByName(communeName).getId();
    }

    @Override
    public CommuneModel add(CommuneDTO communeDTO) {
        CommuneEntity communeEntity = CommuneMapper.mapToEntity(communeDTO);
        boolean isExists = communeRepository.existsByCode(communeDTO.getCode());
        if (!isExists) {
            CommuneEntity saveCommune = communeRepository.save(communeEntity);
            return CommuneMapper.mapToModel(saveCommune);
        }
        return null;
    }

    public ResponseEntity<?> findData (String prefix, String query) {
        if ("find-all".equals(prefix) && query == null) {
            List<CommuneModel> communeModels = findAll();
            if (communeModels != null && !communeModels.isEmpty()) {
                return new ResponseEntity<>(communeModels, HttpStatus.OK);
            }
            return new ResponseEntity<>("Commune list is empty", HttpStatus.OK);
        } else if ("search".equals(prefix) && query != null) {
            List<CommuneModel> communeModels = findByName(query);
            if (communeModels != null && !communeModels.isEmpty()) {
                return new ResponseEntity<>(communeModels, HttpStatus.OK);
            }
            return new ResponseEntity<>("Commune list is empty", HttpStatus.OK);
        } else if ("search-district-id".equals(prefix) && query != null) {
            try {
                Integer districtId = Integer.parseInt(query);
                DistrictResponse districtResponse = findByDistrictId(districtId);
                if (districtResponse != null) {
                    return new ResponseEntity<>(districtResponse, HttpStatus.OK);
                }
                return new ResponseEntity<>("Commune list is empty", HttpStatus.OK);
            }catch (NumberFormatException e) {
                LOGGER.error(e.getMessage());
                return new ResponseEntity<>(query+" not a number!", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Not found api", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> insertData(CommuneDTO communeDTO) {
        CommuneModel communeModel = add(communeDTO);
        if (communeModel == null) {
            return new ResponseEntity<>("Commune exists with code " + communeDTO.getCode(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(communeModel, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> updateData(String code, CommuneDTO communeDTO) {
        try {
            CommuneEntity communeEntity = CommuneMapper.mapToEntity(communeDTO);
            CommuneEntity existscommuneEntity = communeRepository.findOneByCode(code);
            if (existscommuneEntity == null) {
                return new ResponseEntity<>("Commune not found with code " +code, HttpStatus.NOT_FOUND);
            }
            existscommuneEntity.setName(communeEntity.getName());
            existscommuneEntity.setPopulation(communeEntity.getPopulation());
            existscommuneEntity.setDistrict(communeEntity.getDistrict());
            CommuneEntity update = communeRepository.save(existscommuneEntity);
            return new ResponseEntity<>(CommuneMapper.mapToModel(update), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>("Error updating commune", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData(String code) {
        try {
            boolean isExists = communeRepository.existsByCode(code);
            if (isExists) {
                communeRepository.deleteByCode(code);
                if (!communeRepository.existsByCode(code)) return new ResponseEntity<>("Delete success", HttpStatus.OK);
                return new ResponseEntity<>("Delete fail", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Commune not found with code: " + code, HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>("Error deleting commune", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
