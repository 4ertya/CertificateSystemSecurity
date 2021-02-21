package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ExceptionCode;
import com.epam.esm.mapper.CertificateMapper;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.specification.CertificateSpecificationCreator;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.specification.Specification;
import com.epam.esm.service.CertificateService;
import com.epam.esm.validation.BasicValidator;
import com.epam.esm.validation.EntityValidator;
import com.epam.esm.validation.PaginationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CertificateServiceImpl implements CertificateService {

    public final static String PAGE_PARAM="page";
    public final static String SIZE_PARAM="size";

    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final CertificateMapper certificateMapper;
    private final TagMapper tagMapper;
    private final CertificateSpecificationCreator certificateSpecificationCreator;
    private final PaginationValidator paginationValidator;
    private final BasicValidator basicValidator;
    private final EntityValidator entityValidator;


    @Override
    public List<CertificateDto> findCertificates(Map<String, String> params) {
        basicValidator.checkOrderByParams(params);
        basicValidator.checkTagNameParams(params);
        paginationValidator.validatePaginationParams(params);
        List<Specification> specifications = certificateSpecificationCreator.generateQueryCriteria(params);
        long elementsCount = certificateRepository.getCount(specifications);
        paginationValidator.validatePageNumber(params, elementsCount);
        List<CertificateDto> certificates = new ArrayList<>();
        int limit = Integer.parseInt(params.get(SIZE_PARAM));
        int offset = (Integer.parseInt(params.get(PAGE_PARAM)) - 1) * limit;
        certificateRepository.findAllCertificates(specifications, limit, offset).forEach(giftCertificate ->
                certificates.add(certificateMapper.toDTO(giftCertificate)));
        return certificates;
    }

    @Override
    public CertificateDto findCertificateById(long id) {
        basicValidator.validateIdIsPositive(id);
        Certificate certificate = checkExistence(id);
        return certificateMapper.toDTO(certificate);
    }

    @Override
    public CertificateDto createCertificate(CertificateDto certificateDto) {
        certificateDto.setId(null);
        certificateDto.setCreateDate(LocalDateTime.now());
        certificateDto.setLastUpdateDate(LocalDateTime.now());
        entityValidator.validateCertificate(certificateDto);
        Certificate newCertificate = certificateMapper.toModel(certificateDto);
        newCertificate.setTags(prepareTags(certificateDto.getTags()));
        return certificateMapper.toDTO(certificateRepository.createCertificate(newCertificate));
    }

    @Override
    public CertificateDto updateCertificate(CertificateDto certificateDto) {
        basicValidator.validateIdIsPositive(certificateDto.getId());
        Certificate exist = checkExistence(certificateDto.getId());
        prepareCertificateForUpdate(exist, certificateDto);
        return certificateMapper.toDTO(exist);
    }

    @Override
    public void deleteCertificate(long id) {
        basicValidator.validateIdIsPositive(id);
        Certificate certificate = checkExistence(id);
        certificateRepository.deleteCertificate(certificate);
    }

    @Override
    public long getCount(Map<String, String> params) {
        return certificateRepository.getCount(certificateSpecificationCreator.generateQueryCriteria(params));
    }

    private Set<Tag> prepareTags(Set<TagDto> tags) {
        Set<Tag> prepared = new HashSet<>();
        tags.forEach(tagDto -> {
            entityValidator.validateTag(tagDto);
            Tag tag = tagRepository.findTagByName(tagDto.getName());
            if (tag == null) {
                tagDto.setId(null);
                tag = tagRepository.createTag(tagMapper.toModel(tagDto));
            }
            prepared.add(tag);
        });
        return prepared;
    }

    private void prepareCertificateForUpdate(Certificate certificate, CertificateDto certificateDto) {
        certificate.setLastUpdateDate(LocalDateTime.now());

        if (certificateDto.getName() != null) {
            basicValidator.validateStringField(certificateDto.getName(), "certificate name");
            certificate.setName(certificateDto.getName());
        }
        if (certificateDto.getDescription() != null) {
            basicValidator.validateStringField(certificateDto.getDescription(), "description");
            certificate.setDescription(certificateDto.getDescription());
        }
        if (certificateDto.getPrice() != null) {
            entityValidator.validatePrice(certificateDto.getPrice());
            certificate.setPrice(certificateDto.getPrice());
        }
        if (certificateDto.getDuration() != null) {
            entityValidator.validateDuration(certificateDto.getDuration());
            certificate.setDuration(certificateDto.getDuration());
        }
        if (certificateDto.getTags() != null) {
            certificate.setTags(prepareTags(certificateDto.getTags()));
        }
    }

    private Certificate checkExistence(long id) {
        Certificate certificate = certificateRepository.findCertificateById(id);
        if (certificate == null) {
            throw new EntityNotFoundException(
                    ExceptionCode.NON_EXISTING_CERTIFICATE.getErrorCode(),
                    String.valueOf(id));
        }
        return certificate;
    }

}