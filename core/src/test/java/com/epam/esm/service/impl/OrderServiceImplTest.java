package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.model.Order;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.specification.OrderSpecificationCreator;
import com.epam.esm.validation.BasicValidator;
import com.epam.esm.validation.PaginationValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private OrderRepository orderRepository;

    @Spy
    private OrderSpecificationCreator orderSpecificationCreator;
    @Spy
    private PaginationValidator paginationValidator;
    @Spy
    private BasicValidator basicValidator;

    @Spy
    private OrderMapper orderMapper = new OrderMapper(new ModelMapper());


    @Test
    void getOrders() {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setCost(new BigDecimal("23.00"));
        orders.add(order);
        orders.add(order);
        orders.add(order);
        when(orderRepository.getOrders(orderSpecificationCreator.generateQuery(
                new HashMap<>()), 10, 0)).thenReturn(orders);
        assertEquals(3, orderService.getOrders(new HashMap<>()).size());
    }

    @Test
    void getOrderById() {
        when(orderRepository.getOrderById(4)).thenReturn(null);
        assertThrows(EntityNotFoundException.class,
                () -> orderService.getOrderById(4));
    }
}