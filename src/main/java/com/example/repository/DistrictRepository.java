package com.example.repository;

import com.example.entity.DistrictEntity;
import com.example.entity.ProvinceEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<DistrictEntity, Integer> {
    List<DistrictEntity> findByName(String name);
    boolean existsByCode(String code);

    boolean existsById(Integer id);

    @Query(value = "select * from district where code = :code", nativeQuery = true)
    DistrictEntity findOneByCode(@Param("code") String code);

    @Query(value = "select * from district where id = :id", nativeQuery = true)
    DistrictEntity findOneById(@Param("id") Integer id);

    @Query(value = "select * from district where province_id = :province_id", nativeQuery = true)
    List<DistrictEntity> findByProvinceId(@Param("province_id") Integer id);

    @Query(value = "select * from district where name = :name", nativeQuery = true)
    DistrictEntity findOneByName(@Param("name") String name);

    void deleteByCode(String code);

    void deleteById(Integer id);

    @Modifying
    @Transactional
    @Query(value = "Delete from district where province_id = :province_id", nativeQuery = true)
    void deleteByProvinceId(@Param("province_id") Integer provinceId);
}
