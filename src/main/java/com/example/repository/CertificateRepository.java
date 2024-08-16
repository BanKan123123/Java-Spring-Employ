package com.example.repository;

import com.example.entity.CertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CertificateRepository extends JpaRepository<CertificateEntity, Integer> {
    @Query(value = "select * from certificate where name like %:name%", nativeQuery = true)
    List<CertificateEntity> findByName(@Param("name") String name);

    @Query(value = "select * from certificate where id = :id", nativeQuery = true)
    CertificateEntity findOneById(@Param("id") Integer id);

    @Query(value = "select * from certificate where code = :code", nativeQuery = true)
    CertificateEntity findOneByCode(@Param("code") String code);

    boolean existsByCode(String code);

    void deleteByCode(String code);
}
