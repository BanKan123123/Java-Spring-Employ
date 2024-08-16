package com.example.repository;

import com.example.entity.ProvinceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<ProvinceEntity, Integer> {
    @Query(value = "select * from province where name like %:name%", nativeQuery = true)
    List<ProvinceEntity> findByName(@Param("name") String name);

    @Query(value = "select * from province where code = :code", nativeQuery = true)
    ProvinceEntity findOneByCode(@Param(("code")) String code);

    @Query(value = "select * from province where id = :id", nativeQuery = true)
    ProvinceEntity findOneById (@Param("id") Integer id);

    @Query(value = "select * from province where name = :name", nativeQuery = true)
    ProvinceEntity findOneByName(@Param("name") String name);

    boolean existsByCode(String code);

    boolean existsById(Integer id);

    void deleteById(Integer id);
}
