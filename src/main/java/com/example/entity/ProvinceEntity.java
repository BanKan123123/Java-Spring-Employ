package com.example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "province")
@Data
public class ProvinceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<DistrictEntity> districtEntities;

    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<CertificateEntity> certificateEntities;
}
