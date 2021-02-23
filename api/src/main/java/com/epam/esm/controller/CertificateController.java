package com.epam.esm.controller;


import com.epam.esm.dto.CertificateDto;
import com.epam.esm.model.User;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.HateoasBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;
    private final HateoasBuilder hateoasBuilder;

    @GetMapping
    @PreAuthorize("USER")
    public RepresentationModel findAllCertificates(@RequestParam Map<String, String> params) {
        List<CertificateDto> certificates = certificateService.findCertificates(params);
        long certificatesCount = certificateService.getCount(params);
        return hateoasBuilder.addLinksForListOfCertificates(certificates, params, certificatesCount);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RepresentationModel findCertificateById(@PathVariable long id) {
        CertificateDto certificate = certificateService.findCertificateById(id);
        return hateoasBuilder.addLinksForCertificate(certificate);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CertificateDto createCertificate(@RequestBody CertificateDto certificateDto) {
        CertificateDto certificate = certificateService.createCertificate(certificateDto);
        return hateoasBuilder.addLinksForCertificate(certificate);
    }

    @PatchMapping("/{id}")
    public CertificateDto updateCertificate(@PathVariable long id, @RequestBody CertificateDto certificateDto) {
        certificateDto.setId(id);
        CertificateDto certificate = certificateService.updateCertificate(certificateDto);
        return hateoasBuilder.addLinksForCertificate(certificate);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCertificate(@PathVariable long id) {
        certificateService.deleteCertificate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
