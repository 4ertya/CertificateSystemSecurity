package com.epam.esm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDto extends RepresentationModel<UserDto> {
    private int id;
    private String name;
    private String surname;
    private String email;
}
