package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.mapper.CertificateMapper;
import com.epam.esm.model.Certificate;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.specification.CertificateSpecificationCreator;
import com.epam.esm.validation.BasicValidator;
import com.epam.esm.validation.EntityValidator;
import com.epam.esm.validation.PaginationValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CertificateServiceImplTest {

    @InjectMocks
    private CertificateServiceImpl certificatesService;
    @Mock
    private CertificateRepository certificateRepository;
    @Spy
    private BasicValidator basicValidator = new BasicValidator();
    @Spy
    private PaginationValidator paginationValidator = new PaginationValidator();
    @Mock
    CertificateSpecificationCreator certificateSpecificationCreator;
    @Spy
    CertificateMapper certificateMapper = new CertificateMapper(new ModelMapper());
    @Mock
    EntityValidator entityValidator;

    @Test
    void findCertificates() {
        List<Certificate> certificates = new ArrayList<>();
        Certificate certificate = new Certificate();
        certificates.add(certificate);
        certificates.add(certificate);
        certificates.add(certificate);
        when(certificateRepository.findAllCertificates(certificateSpecificationCreator.generateQueryCriteria
                (new HashMap<>()), 10, 0)).thenReturn(certificates);
        assertEquals(3, certificatesService.findCertificates(anyMap()).size());
    }

    @Test
    void findCertificateById() {
        Certificate certificateReturned = new Certificate();
        certificateReturned.setId(2L);
        certificateReturned.setPrice(new BigDecimal("12.00"));
        when(certificateRepository.findCertificateById(2)).thenReturn(certificateReturned);
        CertificateDto certificateDtoReturned = new CertificateDto();
        certificateDtoReturned.setId(2L);
        certificateDtoReturned.setPrice(new BigDecimal("12.00"));
        assertEquals(certificateDtoReturned, certificatesService.findCertificateById(2));
    }

    @Test
    void getCertificateByIdWithException() {
        when(certificateRepository.findCertificateById(24)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> certificatesService.findCertificateById(24));
    }
    @Test
    void createCertificate() {
        Certificate certificateReturned = new Certificate();
        certificateReturned.setId(24L);
        certificateReturned.setPrice(new BigDecimal("45.00"));
        when(certificateRepository.createCertificate(any())).thenReturn(certificateReturned);
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setPrice(new BigDecimal("45.00"));
        doNothing().when(entityValidator).validateCertificate(certificateDto);
        assertEquals(24, certificatesService.createCertificate(certificateDto).getId());
        assertNotNull(certificateDto.getCreateDate());
        assertNotNull(certificateDto.getLastUpdateDate());
    }

    @Test
    void updateCertificate() {
        Certificate certificate = new Certificate();
        certificate.setPrice(new BigDecimal("34.00"));
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setId(2L);
        certificateDto.setPrice(new BigDecimal("34.00"));
        when(certificateRepository.findCertificateById(2)).thenReturn(new Certificate());
        certificateDto = certificatesService.updateCertificate(certificateDto);
        assertNotNull(certificateDto.getLastUpdateDate());
    }

    @Test
    void deleteCertificate() {
        when(certificateRepository.findCertificateById(1)).thenReturn(new Certificate());
        doNothing().when(certificateRepository).deleteCertificate(new Certificate());
        certificatesService.deleteCertificate(1);
    }
}