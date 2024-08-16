package com.example.model;


import lombok.Data;

import java.util.Date;

@Data
public class CertificateModel {
    private Integer id;
    private String code;
    private String name;
    private String type;
    private Date issueDate;;
    private Date expiryDate;
    private String provinceName;
}
