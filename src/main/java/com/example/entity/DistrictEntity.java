package com.example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "district")
@Data
public class DistrictEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private ProvinceEntity province;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<CommuneEntity> communeEntities;
}
