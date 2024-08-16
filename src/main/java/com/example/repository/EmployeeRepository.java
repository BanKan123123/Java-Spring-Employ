package com.example.repository;

import com.example.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    @Query(value = "select * from employee where name like %:name%", nativeQuery = true)
    List<EmployeeEntity> findByName(@Param("name") String name);

    @Query(value = "select * from employee where code = :code", nativeQuery = true)
    EmployeeEntity findOneByCode(@Param(("code")) String code);

    boolean existsByCode(String code);

    void deleteByCode(String code);
}
