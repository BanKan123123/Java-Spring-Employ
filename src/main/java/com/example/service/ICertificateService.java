package com.example.service;

import com.example.DTO.CertificateDTO;
import com.example.model.CertificateModel;

import java.util.List;

public interface ICertificateService {
    List<CertificateModel> findALL();
    List<CertificateModel> findByName(String name);
    CertificateModel findById(Integer id);
    CertificateModel add (CertificateDTO certificateDTO);
}
