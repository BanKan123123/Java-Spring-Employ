package com.example.repository;

import com.example.entity.CommuneEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommuneRepository extends JpaRepository<CommuneEntity, Integer> {
    List<CommuneEntity> findByName(String name);
    @Query(value = "select * from commune where district_id = :id", nativeQuery = true)
    List<CommuneEntity> findByDistrictId(@Param("id") Integer id);

    @Query(value = "select * from commune where id = :id", nativeQuery = true)
    CommuneEntity findOneById(@Param("id") Integer id);

    @Query(value = "select * from commune where code = :code", nativeQuery = true)
    CommuneEntity findOneByCode(@Param("code") String code);

    @Query(value = "select * from commune where name = :name", nativeQuery = true)
    CommuneEntity findOneByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "delete from commune where district_id = :id", nativeQuery = true)
    void deleteByDistrictId(@Param("id") Integer id);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    void deleteByCode(String code);
}
