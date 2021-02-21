package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.model.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ModelMapper mapper;

    public Order toModel(OrderDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Order.class);
    }

    public OrderDto toDTO(Order model) {
        return Objects.isNull(model) ? null : mapper.map(model, OrderDto.class);
    }
}
