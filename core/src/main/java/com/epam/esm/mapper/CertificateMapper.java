package com.epam.esm.mapper;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.model.Certificate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CertificateMapper {
    private final ModelMapper modelMapper;

    public Certificate toModel(CertificateDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Certificate.class);
    }

    public CertificateDto toDTO(Certificate model) {
        return Objects.isNull(model) ? null : modelMapper.map(model, CertificateDto.class);
    }
}
