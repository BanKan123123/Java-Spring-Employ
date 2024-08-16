package com.example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "commune")
@Data
public class CommuneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "population", nullable = false)
    private Integer population;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private DistrictEntity district;
}
