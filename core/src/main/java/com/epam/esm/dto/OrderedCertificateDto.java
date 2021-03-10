package com.epam.esm.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Data
public class OrderedCertificateDto extends RepresentationModel<OrderedCertificateDto> {
    private Long certificateId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;

}
