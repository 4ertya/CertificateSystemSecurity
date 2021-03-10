package com.epam.esm.dto;

import lombok.Data;

import java.util.List;

@Data
public class NewOrderDto {
    private Long userId;
    private List<Long> certificatesId;
}
