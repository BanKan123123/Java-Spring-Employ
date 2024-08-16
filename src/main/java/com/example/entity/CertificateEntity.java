package com.example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "certificate")
@Data
public class CertificateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "issue_date", nullable = false)
    private Date issueDate;

    @Column(name = "expiry_date", nullable = false)
    private Date expiryDate;


    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private ProvinceEntity province;
}
