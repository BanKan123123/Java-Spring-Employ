package com.example.service.impl;

import com.example.DTO.CommuneDTO;
import com.example.DTO.DistrictDTO;
import com.example.DTO.ProvinceDTO;
import com.example.entity.CommuneEntity;
import com.example.entity.DistrictEntity;
import com.example.mapper.DistrictMapper;
import com.example.model.ProvinceModel;
import com.example.entity.ProvinceEntity;
import com.example.mapper.ProvinceMapper;
import com.example.repository.DistrictRepository;
import com.example.repository.ProvinceRepository;
import com.example.service.IProvinceService;
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
public class ProvinceService implements IProvinceService {

    private ProvinceRepository provinceRepository;

    private DistrictRepository districtRepository;

    @Autowired
    private DistrictService districtService;

    private Logger LOGGER = LogManager.getLogger(ProvinceService.class);

    @Autowired
    public ProvinceService(ProvinceRepository provinceRepository, DistrictRepository districtRepository) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
    }

    @Override
    public List<ProvinceModel> findAll() {
        List<ProvinceEntity> provinceEntityList = provinceRepository.findAll();
        List<ProvinceModel> provinceModelList = new ArrayList<>();
        for (ProvinceEntity provinceEntity : provinceEntityList) {
            provinceModelList.add(ProvinceMapper.mapToProvinceModel(provinceEntity));
        }
        return provinceModelList;
    }

    @Override
    public List<ProvinceModel> findByName(String name) {
        List<ProvinceEntity> provinceEntityList = provinceRepository.findByName(name);
        List<ProvinceModel> provinceModelList = new ArrayList<>();
        for (ProvinceEntity provinceEntity : provinceEntityList) {
            provinceModelList.add(ProvinceMapper.mapToProvinceModel(provinceEntity));
        }
        return provinceModelList;
    }

    @Override
    public ProvinceModel findOneByCode(String code) {
        return ProvinceMapper.mapToProvinceModel(provinceRepository.findOneByCode(code));
    }

    @Override
    public ProvinceEntity findOneById(Integer id) {
        return provinceRepository.findOneById(id);
    }

    @Override
    public Integer getProvinceId(String provinceName) {
        return provinceRepository.findOneByName(provinceName).getId();
    }

    @Override
    public ProvinceModel add(ProvinceModel provinceModel) {
        ProvinceEntity provinceEntity = ProvinceMapper.mapToProvinceEntity(provinceModel);
        boolean isExists = provinceRepository.existsByCode(provinceModel.getCode());
        if (!isExists) {
            ProvinceEntity saveProvince = provinceRepository.save(provinceEntity);
            return ProvinceMapper.mapToProvinceModel(saveProvince);
        }
        return null;
    }

    public ResponseEntity<?> findData(String prefix, String query) {
        if ("find-all".equals(prefix) && query == null) {
            List<ProvinceModel> list = findAll();
            if (list != null && !list.isEmpty()) {
                return new ResponseEntity<>(list, HttpStatus.OK);
            }
            return new ResponseEntity<>("Province list is empty", HttpStatus.OK);
        } else if ("search".equals(prefix) && query != null) {
            List<ProvinceModel> list = findByName(query);
            if (list != null && !list.isEmpty()) {
                return new ResponseEntity<>(list, HttpStatus.OK);
            }
            return new ResponseEntity<>("here are no province named " + query, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not found api", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> ínsertData (ProvinceModel provinceModel) {
        ProvinceModel saveProvince = add(provinceModel);
        if (saveProvince == null) {
            return new ResponseEntity<>("Province exists with code " + provinceModel.getCode(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(saveProvince, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> addProvinceWithDistricts(ProvinceDTO provinceDTO) {
        try {
            boolean isExists = provinceRepository.existsByCode(provinceDTO.getCode());
            if (!isExists) {
                ProvinceEntity provinceEntity = new ProvinceEntity();
                provinceEntity.setCode(provinceDTO.getCode());
                provinceEntity.setName(provinceDTO.getName());
                ProvinceEntity savedProvince = provinceRepository.save(provinceEntity);

                List<DistrictDTO> districtDTOS = provinceDTO.getDistricts();
                if(districtDTOS != null) {
                    for (DistrictDTO districtDTO : districtDTOS) {
                        DistrictEntity districtEntity = new DistrictEntity();
                        districtEntity.setCode(districtDTO.getCode());
                        districtEntity.setName(districtDTO.getName());
                        districtEntity.setProvince(savedProvince);
                        DistrictEntity saveDistrict = districtRepository.save(districtEntity);

                        List<CommuneDTO> communeDTOS = districtDTO.getCommunes();
                        if (communeDTOS != null) {
                            for (CommuneDTO communeDTO : communeDTOS) {
                                CommuneEntity communeEntity = new CommuneEntity();
                                communeEntity.setCode(communeDTO.getCode());
                                communeEntity.setName(communeDTO.getName());
                                communeEntity.setPopulation(communeDTO.getPopulation());
                                communeEntity.setDistrict(saveDistrict);
                            }
                        }
                    }
                }
                return new ResponseEntity<>("Province and districts and communes added successfully", HttpStatus.OK);

            }
            return new ResponseEntity<>("Already exists the province whose code is " + provinceDTO.getCode(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>("Error adding province with districts and communes", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> updateProvinceWithDistricts(ProvinceDTO provinceDTO) {
        try {
            ProvinceEntity provinceEntity = provinceRepository.findOneByCode(provinceDTO.getCode());
            if (provinceEntity != null) {
                provinceEntity.setName(provinceDTO.getName());
                provinceRepository.save(provinceEntity);

                for(DistrictDTO districtDTO : provinceDTO.getDistricts()) {
                    DistrictEntity districtEntity = districtRepository.findOneByCode(districtDTO.getCode());
                    if (districtEntity != null) {
                        districtEntity.setName(districtDTO.getName());
                        districtRepository.save(districtEntity);
                    } else {
                        DistrictEntity newDistrict = DistrictMapper.mapToDistrictEntity(districtDTO);
                        districtRepository.save(newDistrict);
                    }
                }

                return new ResponseEntity<>("Province and districts updated successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>("Not found province with code is " + provinceDTO.getCode(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>("Error updating province with districts", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> updateData (String code, ProvinceModel provinceModel) {
        try {
            ProvinceEntity provinceEntity = ProvinceMapper.mapToProvinceEntity(provinceModel);
            ProvinceEntity existingProvince = provinceRepository.findOneByCode(code);

            if(existingProvince == null) {
                return new ResponseEntity<>("Province not found with code: "+code, HttpStatus.NOT_FOUND);
            }

            existingProvince.setName(provinceEntity.getName());

            ProvinceEntity updated = provinceRepository.save(existingProvince);
            ProvinceModel updateProvince = ProvinceMapper.mapToProvinceModel(updated);
            return new ResponseEntity<>(updateProvince, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>("Error updating province", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteProvince (Integer id) {
        try {
            boolean isExists = provinceRepository.existsById(id);
            if (isExists) {
                Integer deleteDistrict = districtService.deleteDistrictByProvinceId(id);
                if (deleteDistrict == 1) {
                    provinceRepository.deleteById(id);
                    if (!provinceRepository.existsById(id)) return new ResponseEntity<>("Delete Success", HttpStatus.OK);
                    return new ResponseEntity<>("Delete fail", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            return new ResponseEntity<>("Province not found with id: "+id, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>("Error deleting province", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
