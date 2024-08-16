package com.example.service.impl;

import com.example.DTO.CommuneDTO;
import com.example.DTO.DistrictDTO;
import com.example.entity.CommuneEntity;
import com.example.entity.DistrictEntity;
import com.example.entity.ProvinceEntity;
import com.example.mapper.CommuneMapper;
import com.example.mapper.DistrictMapper;
import com.example.model.DistrictModel;
import com.example.repository.CommuneRepository;
import com.example.repository.DistrictRepository;
import com.example.repository.ProvinceRepository;
import com.example.response.ProvinceResponse;
import com.example.service.IDistrictService;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class DistrictService implements IDistrictService {

    private DistrictRepository districtRepository;
    private ProvinceRepository provinceRepository;
    private CommuneRepository communeRepository;

    @Autowired
    public DistrictService(DistrictRepository districtRepository, ProvinceRepository provinceRepository, CommuneRepository communeRepository) {
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
        this.communeRepository = communeRepository;
    }

    private static final Logger LOGGER = LogManager.getLogger(DistrictService.class);

    @Override
    public List<DistrictModel> findAll() {
        List<DistrictEntity> districtEntities = districtRepository.findAll();
        List<DistrictModel> districtModels = new ArrayList<>();
        for(DistrictEntity districtEntity : districtEntities) {
            districtModels.add(DistrictMapper.mapToDistrictModel(districtEntity));
        }
        return districtModels;
    }

    @Override
    public List<DistrictModel> findByName(String name) {
        List<DistrictEntity> districtEntities = districtRepository.findByName(name);
        List<DistrictModel> districtModels = new ArrayList<>();
        for(DistrictEntity districtEntity : districtEntities) {
            districtModels.add(DistrictMapper.mapToDistrictModel(districtEntity));
        }
        return districtModels;
    }

    @Override
    public ProvinceResponse findByProvinceId(Integer id) {
        ProvinceResponse provinceResponse = new ProvinceResponse();
        ProvinceEntity provinceEntity = provinceRepository.findOneById(id);

        provinceResponse.setCode(provinceEntity.getCode());
        provinceResponse.setName(provinceEntity.getName());
        List<DistrictEntity> districtEntities = districtRepository.findByProvinceId(id);
        for(DistrictEntity districtEntity : districtEntities) {
            DistrictDTO districtDTO = new DistrictDTO();
            districtDTO.setCode(districtEntity.getCode());
            districtDTO.setName(districtEntity.getName());
            districtDTO.setProvinceId(districtEntity.getProvince().getId());
            List<CommuneEntity> communeEntities = communeRepository.findByDistrictId(districtEntity.getId());
            for (CommuneEntity communeEntity : communeEntities) {
                CommuneDTO communeDTO = new CommuneDTO();
                communeDTO.setCode(communeEntity.getCode());
                communeDTO.setName(communeEntity.getName());
                communeDTO.setPopulation(communeEntity.getPopulation());
                communeDTO.setDistrictId(communeEntity.getDistrict().getId());

                districtDTO.getCommunes().add(communeDTO);
            }
            provinceResponse.getDistricts().add(districtDTO);
        }
        return provinceResponse;
    }

    @Override
    public DistrictEntity findOneById(Integer id) {
        return districtRepository.findOneById(id);
    }

    @Override
    public Integer getDistrictId(String districtName) {
        return districtRepository.findOneByName(districtName).getId();
    }

    @Override
    public DistrictModel add(DistrictDTO districtDTO) {
        DistrictEntity districtEntity = DistrictMapper.mapToDistrictEntity(districtDTO);
        boolean isExists = districtRepository.existsByCode(districtEntity.getCode());
        if (!isExists) {
            DistrictEntity saveDistrict = districtRepository.save(districtEntity);
            return DistrictMapper.mapToDistrictModel(saveDistrict);
        }
        return null;
    }

    public ResponseEntity<?> findData (String prefix, String query) {
        if ("find-all".equals(prefix) && query == null) {
            List<DistrictModel> districtModels = findAll();
            if (districtModels != null && !districtModels.isEmpty()) {
                return new ResponseEntity<>(districtModels, HttpStatus.OK);
            }
            return new ResponseEntity<>("District list is empty", HttpStatus.OK);
        } else if ("search".equals(prefix) && query != null) {
            List<DistrictModel> districtModels = findByName(query);
            if (districtModels != null && !districtModels.isEmpty()) {
                return new ResponseEntity<>(districtModels, HttpStatus.OK);
            }
            return new ResponseEntity<>("District list is empty", HttpStatus.OK);
        } else if ("search-province-id".equals(prefix) && query != null) {
            try {
                Integer id = Integer.parseInt(query);
                ProvinceResponse provinceResponse = findByProvinceId(id);
                if (provinceResponse != null) {
                    return new ResponseEntity<>(provinceResponse, HttpStatus.OK);
                }
                return new ResponseEntity<>("There is no province with an id of " + id, HttpStatus.OK);
            } catch (NumberFormatException e) {
                LOGGER.error(e.getMessage());
                return new ResponseEntity<>(query+" not a number!", HttpStatus.BAD_REQUEST);
            }

        }else {
            return new ResponseEntity<>("Not found api", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> insertData (DistrictDTO districtDTO) {
        DistrictModel saveDistrict = add(districtDTO);
        if (saveDistrict == null) {
            return new ResponseEntity<>("District exists with code " + districtDTO.getCode(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(saveDistrict, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> updateData (String code, DistrictDTO districtDTO) {
        try {
            DistrictEntity districtEntity = DistrictMapper.mapToDistrictEntity(districtDTO);
            DistrictEntity existsDistrict = districtRepository.findOneByCode(districtEntity.getCode());

            if (existsDistrict == null) {
                return new ResponseEntity<>("District not found with code: " + code, HttpStatus.NOT_FOUND);
            }

            existsDistrict.setName(districtEntity.getName());
            existsDistrict.setProvince(districtEntity.getProvince());

            DistrictEntity update = districtRepository.save(existsDistrict);
            DistrictModel districtModel = DistrictMapper.mapToDistrictModel(update);

            return new ResponseEntity<>(districtModel, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>("Error updating district", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (Integer id) {
        Integer delete = deleteDistrict(id);
        if(delete == -1) {
            return new ResponseEntity<>("Error deleting district", HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (delete == 0) {
            return new ResponseEntity<>("District not found with id: "+ id, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>("Delete success", HttpStatus.OK);
        }
    }

    @Transactional
    public Integer deleteDistrict(Integer id) {
        try {
            boolean isExists = districtRepository.existsById(id);
            if (isExists) {
                communeRepository.deleteByDistrictId(id);
                districtRepository.deleteById(id);
                if (!districtRepository.existsById(id)) return 1;
                else return -1;
            }
            return 0;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public Integer deleteDistrictByProvinceId (Integer id) {
        try {
            List<DistrictEntity> districtEntities = districtRepository.findByProvinceId(id);
            if (districtEntities != null) {
                for (DistrictEntity districtEntity : districtEntities) {
                    Integer districtId = districtEntity.getId();
                    communeRepository.deleteByDistrictId(districtId);
                    districtRepository.deleteById(districtId);
                }
                return 1;
            }
            return 0;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return -1;
        }
    }

    @Transactional
    public ResponseEntity<?> addDistrictWithCommune(DistrictDTO districtDTO){
        try {
            boolean isExists = districtRepository.existsByCode(districtDTO.getCode());
            if(isExists) {
                return new ResponseEntity<>("Already exists the district whose code is " + districtDTO.getCode(), HttpStatus.CONFLICT);
            }
            DistrictEntity districtEntity = new DistrictEntity();
            districtEntity.setCode(districtDTO.getCode());
            districtEntity.setName(districtDTO.getName());
            ProvinceEntity provinceEntity = provinceRepository.findOneById(districtDTO.getProvinceId());
            districtEntity.setProvince(provinceEntity);

            DistrictEntity saveDistrict = districtRepository.save(districtEntity);

            for (CommuneDTO communeDTO: districtDTO.getCommunes()) {
                boolean isExistsCode = communeRepository.existsByCode(communeDTO.getCode());
                boolean isExistsName = communeRepository.existsByName(communeDTO.getName());

                if (!isExistsCode && !isExistsName) {
                    CommuneEntity communeEntity = new CommuneEntity();
                    communeEntity.setCode(communeDTO.getCode());
                    communeEntity.setName(communeDTO.getName());
                    communeEntity.setPopulation(communeDTO.getPopulation());
                    communeEntity.setDistrict(saveDistrict);
                    communeRepository.save(communeEntity);
                }
            }
            return new ResponseEntity<>("District and communes added successfully", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>("Error adding district with communes", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional
    public ResponseEntity<?> updateDistrictWithCommunes (DistrictDTO districtDTO) {
        try {
            DistrictEntity districtEntity = districtRepository.findOneByCode(districtDTO.getCode());
            if (districtEntity != null) {
                districtEntity.setName(districtDTO.getName());
                ProvinceEntity provinceEntity = provinceRepository.findOneById(districtDTO.getProvinceId());
                districtEntity.setProvince(provinceEntity);
                districtRepository.save(districtEntity);
                for (CommuneDTO communeDTO : districtDTO.getCommunes()) {
                    CommuneEntity communeEntity = communeRepository.findOneByCode(communeDTO.getCode());
                    if(communeEntity != null) {
                        communeEntity.setName(communeDTO.getName());
                        communeEntity.setPopulation(communeDTO.getPopulation());
                        communeRepository.save(communeEntity);
                    } else {
                        CommuneEntity newCommune = CommuneMapper.mapToEntity(communeDTO);
                        communeRepository.save(newCommune);
                    }
                }
                return new ResponseEntity<>("District and communes updated successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>("Not found district with code is " + districtDTO.getCode(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>("Error updating district with communes", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
