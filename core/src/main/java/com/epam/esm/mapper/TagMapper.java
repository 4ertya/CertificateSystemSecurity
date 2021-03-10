package com.epam.esm.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.model.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;
@Component
@RequiredArgsConstructor
public class TagMapper {
    private final ModelMapper modelMapper;

    public Tag toModel(TagDto dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, Tag.class);
    }

    public TagDto toDTO(Tag model) {
        return Objects.isNull(model) ? null : modelMapper.map(model, TagDto.class);
    }
}
