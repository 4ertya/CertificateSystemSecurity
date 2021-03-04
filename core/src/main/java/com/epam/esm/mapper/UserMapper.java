package com.epam.esm.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper mapper;

    public UserDto toDTO(User model) {
        return Objects.isNull(model) ? null : mapper.map(model, UserDto.class);
    }
    public User toModel(UserDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, User.class);
    }
}
