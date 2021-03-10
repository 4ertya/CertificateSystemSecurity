package com.epam.esm.service;

import com.epam.esm.dto.NewOrderDto;
import com.epam.esm.dto.OrderDto;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderDto addOrder(NewOrderDto newOrderDto);

    List<OrderDto> getOrders(Map<String, String> params);

    OrderDto getOrderById(long id);

    List<OrderDto> getOrdersByUserId(long id, Map<String, String> params);

    long getCount(Map<String, String> params);

    void deleteOrder(long id);
}
