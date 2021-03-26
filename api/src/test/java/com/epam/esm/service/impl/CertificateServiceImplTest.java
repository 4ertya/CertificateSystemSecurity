package com.epam.esm.service.impl;

import com.epam.esm.exception.notfound.CertificateNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.validation.BasicValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

    @InjectMocks
    private CertificateServiceImpl certificatesService;
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private BasicValidator basicValidator;

    @Test
    void readUnsuccessful() {
        long expectedCertificateId = 1;

        when(certificateRepository.findById(expectedCertificateId)).thenReturn(Optional.empty());
        assertThrows(CertificateNotFoundException.class, () -> certificatesService.findCertificateById(expectedCertificateId));
        verify(basicValidator).validateIdIsPositive(expectedCertificateId);
    }
}