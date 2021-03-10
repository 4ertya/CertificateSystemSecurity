package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.notfound.CertificateNotFoundException;
import com.epam.esm.exception.ExceptionCode;
import com.epam.esm.mapper.CertificateMapper;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.specification.CertificateSpecification;
import com.epam.esm.service.CertificateService;
import com.epam.esm.validation.BasicValidator;
import com.epam.esm.validation.EntityValidator;
import com.epam.esm.validation.PaginationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CertificateServiceImpl implements CertificateService {

    public final static String PAGE_PARAM = "page";
    public final static String SIZE_PARAM = "size";

    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final CertificateMapper certificateMapper;
    private final TagMapper tagMapper;
    private final PaginationValidator paginationValidator;
    private final BasicValidator basicValidator;
    private final EntityValidator entityValidator;


    @Override
    public List<CertificateDto> findCertificates(Map<String, String> params) {
        basicValidator.checkOrderByParams(params);
        basicValidator.checkTagNameParams(params);
        paginationValidator.validatePaginationParams(params);
        CertificateSpecification certificateSpecification = new CertificateSpecification(params);
        Sort sort = createSort(params);
        long totalElements = certificateRepository.count(certificateSpecification);
        paginationValidator.validatePageNumber(params, totalElements);
        int size = Integer.parseInt(params.get(SIZE_PARAM));
        int page = Integer.parseInt(params.get(PAGE_PARAM)) - 1;
        return certificateRepository.findAll(certificateSpecification, PageRequest.of(page, size, sort))
                .stream()
                .map(certificateMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public CertificateDto findCertificateById(long id) {
        basicValidator.validateIdIsPositive(id);
        Certificate certificate =
                certificateRepository.findById(id).orElseThrow(() -> new CertificateNotFoundException(ExceptionCode.NON_EXISTING_CERTIFICATE.getErrorCode(),
                        String.valueOf(id)));
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
        return certificateMapper.toDTO(certificateRepository.save(newCertificate));
    }

    @Override
    public CertificateDto updateCertificate(CertificateDto certificateDto) {
        basicValidator.validateIdIsPositive(certificateDto.getId());
        Certificate exist = certificateRepository.findById(certificateDto.getId())
                .orElseThrow(() -> new CertificateNotFoundException(ExceptionCode.NON_EXISTING_CERTIFICATE.getErrorCode(),
                        String.valueOf(certificateDto.getId())));
        prepareCertificateForUpdate(exist, certificateDto);
        return certificateMapper.toDTO(exist);
    }

    @Override
    public void deleteCertificate(long id) {
        basicValidator.validateIdIsPositive(id);
        Certificate certificate = certificateRepository.findById(id).orElseThrow(() -> new CertificateNotFoundException(ExceptionCode.NON_EXISTING_CERTIFICATE.getErrorCode(),
                String.valueOf(id)));
        certificateRepository.delete(certificate);
    }

    @Override
    public long getCount(Map<String, String> params) {
        CertificateSpecification certificateSpecification = new CertificateSpecification(params);
        return certificateRepository.count(certificateSpecification);
    }

    private Set<Tag> prepareTags(Set<TagDto> tags) {
        Set<Tag> prepared = new HashSet<>();
        tags.forEach(tagDto -> {
            entityValidator.validateTag(tagDto);
            Tag tag = tagRepository.findTagByName(tagDto.getName()).orElse(null);
            if (tag == null) {
                tagDto.setId(null);
                tag = tagRepository.save(tagMapper.toModel(tagDto));
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

    private Sort createSort(Map<String, String> params) {
        List<Sort.Order> orders = new ArrayList<>();
        params.keySet().stream().filter(key -> key.equals("orderBy")).forEach(key -> {
            if (params.get(key).startsWith("-")) {
                orders.add(new Sort.Order(Sort.Direction.DESC, params.get(key).replace("-", "")));
            } else {
                orders.add(new Sort.Order(Sort.Direction.ASC, params.get(key)));
            }
        });
        if (!orders.isEmpty()) {
            return Sort.by(orders);
        }
        return Sort.by("id").ascending();
    }

}