package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;

import java.util.List;
import java.util.Map;

public interface CertificateService {

    List<CertificateDto> findCertificates(Map<String, String> params);

    CertificateDto findCertificateById(long id);

    CertificateDto createCertificate(CertificateDto certificateDto);

    CertificateDto updateCertificate(CertificateDto certificateDTO);

    void deleteCertificate(long id);

    long getCount(Map<String, String> params);


}
