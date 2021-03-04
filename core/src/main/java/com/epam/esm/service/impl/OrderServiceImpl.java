package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.NewOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.ExceptionCode;
import com.epam.esm.exception.notfound.OrderNotFoundException;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.Order;
import com.epam.esm.model.OrderedCertificate;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.validation.BasicValidator;
import com.epam.esm.validation.EntityValidator;
import com.epam.esm.validation.PaginationValidator;
import com.epam.esm.validation.SecurityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    public final static String PAGE_PARAM = "page";
    public final static String SIZE_PARAM = "size";

    private final OrderRepository orderRepository;
    private final CertificateService certificateService;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final PaginationValidator paginationValidator;
    private final EntityValidator entityValidator;
    private final BasicValidator basicValidator;
    private final UserMapper userMapper;
    private final SecurityValidator securityValidator;


    @Override
    public OrderDto addOrder(NewOrderDto newOrderDto) {
        entityValidator.validateOrder(newOrderDto);
        securityValidator.validateUserAccess(newOrderDto.getUserId());
        Order newOrder = createNewOrder(newOrderDto);
        return orderMapper.toDTO(orderRepository.save(newOrder));
    }

    private Order createNewOrder(NewOrderDto newOrderDto) {
        Order order = new Order();
        BigDecimal cost = new BigDecimal(0);
        UserDto userDto = userService.getUserById(newOrderDto.getUserId());
        order.setUser(userMapper.toModel(userDto));
        List<Long> certificatesIdValues = newOrderDto.getCertificatesId();
        certificatesIdValues.stream()
                .map(certificateService::findCertificateById)
                .map(OrderServiceImpl::createOrderedCertificate)
                .forEach(orderedCertificate -> {
                    orderedCertificate.setOrder(order);
                    order.getCertificates().add(orderedCertificate);
                });
        for (OrderedCertificate orderedCertificate : order.getCertificates()) {
            cost = cost.add(orderedCertificate.getPrice());
        }
        order.setCost(cost);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    @Override
    public List<OrderDto> getOrders(Map<String, String> params) {
        paginationValidator.validatePaginationParams(params);
        basicValidator.validateOrderParams(params);
        List<OrderDto> orders = new ArrayList<>();
        int size = Integer.parseInt(params.get(SIZE_PARAM));
        int page = Integer.parseInt(params.get(PAGE_PARAM)) - 1;
        long elementsCount = orderRepository.count();
        paginationValidator.validatePageNumber(params, elementsCount);
        orderRepository.findAll(PageRequest.of(page, size))
                .forEach(order -> orders.add(orderMapper.toDTO(order)));
        return orders;
    }

    @Override
    public long getCount(Map<String, String> params) {
        return orderRepository.count();
    }

    @Override
    public void deleteOrder(long id) {
        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() -> new OrderNotFoundException(ExceptionCode.NON_EXISTING_ORDER.getErrorCode(),
                                String.valueOf(id)));
        orderRepository.delete(order);
    }


    @Override
    public OrderDto getOrderById(long id) {
        basicValidator.validateIdIsPositive(id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(ExceptionCode.NON_EXISTING_ORDER.getErrorCode(), String.valueOf(id)));

        return orderMapper.toDTO(order);
    }

    @Override
    public List<OrderDto> getOrdersByUserId(long id, Map<String, String> params) {
        securityValidator.validateUserAccess(id);
        paginationValidator.validatePaginationParams(params);
        int size = Integer.parseInt(params.get(SIZE_PARAM));
        int page = Integer.parseInt(params.get(PAGE_PARAM)) - 1;
        List<OrderDto> userOrders = new ArrayList<>();
        orderRepository.findOrderByUserId(id, PageRequest.of(page, size)).forEach(order -> userOrders.add(orderMapper.toDTO(order)));
        return userOrders;
    }


    private static OrderedCertificate createOrderedCertificate(CertificateDto certificate) {
        OrderedCertificate orderedCertificate = new OrderedCertificate();
        orderedCertificate.setName(certificate.getName());
        orderedCertificate.setDescription(certificate.getDescription());
        orderedCertificate.setPrice(certificate.getPrice());
        orderedCertificate.setDuration(certificate.getDuration());
        orderedCertificate.setCertificateId(certificate.getId());
        return orderedCertificate;
    }
}
