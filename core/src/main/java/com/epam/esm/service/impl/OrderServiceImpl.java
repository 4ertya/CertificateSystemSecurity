package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.NewOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ExceptionCode;
import com.epam.esm.mapper.OrderMapper;
import com.epam.esm.model.Order;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.specification.OrderSpecificationCreator;
import com.epam.esm.repository.specification.SearchSpecification;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.validation.BasicValidator;
import com.epam.esm.validation.EntityValidator;
import com.epam.esm.validation.PaginationValidator;
import lombok.RequiredArgsConstructor;
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
    public final static String USER_ID_PARAM = "userId";

    private final OrderRepository orderRepository;
    private final CertificateService certificateService;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final PaginationValidator paginationValidator;
    private final EntityValidator entityValidator;
    private final BasicValidator basicValidator;
    private final OrderSpecificationCreator orderSpecificationCreator;


    @Override
    public OrderDto addOrder(NewOrderDto newOrderDto) {
        entityValidator.validateOrder(newOrderDto);
        OrderDto orderDto = createNewOrder(newOrderDto);
        Order newOrder = orderMapper.toModel(orderDto);
        return orderMapper.toDTO(orderRepository.addOrder(newOrder));
    }

    private OrderDto createNewOrder(NewOrderDto newOrderDto) {
        OrderDto order = new OrderDto();
        BigDecimal cost = new BigDecimal(0);
        userService.getUserById(newOrderDto.getUserId());
        order.setUserId(newOrderDto.getUserId());
        List<Long> certificatesIdValues = newOrderDto.getCertificatesId();
        certificatesIdValues.forEach(certificateId -> {
            CertificateDto certificate = certificateService.findCertificateById(certificateId);
            order.getCertificates().add(certificate);
        });
        for (CertificateDto certificateDto : order.getCertificates()) {
            cost = cost.add(certificateDto.getPrice());
        }
        order.setCost(cost);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    @Override
    public List<OrderDto> getOrders(Map<String, String> params) {
        paginationValidator.validatePaginationParams(params);
        basicValidator.validateOrderParams(params);
        if (params.containsKey(USER_ID_PARAM)) {
            userService.getUserById(Long.parseLong(params.get(USER_ID_PARAM)));
        }
        List<SearchSpecification> specifications = orderSpecificationCreator.generateQuery(params);
        List<OrderDto> orders = new ArrayList<>();
        int limit = Integer.parseInt(params.get(SIZE_PARAM));
        int offset = (Integer.parseInt(params.get(PAGE_PARAM)) - 1) * limit;
        long elementsCount = orderRepository.getCount(specifications);
        paginationValidator.validatePageNumber(params, elementsCount);
        orderRepository.getOrders(specifications, limit, offset)
                .forEach(order -> orders.add(orderMapper.toDTO(order)));
        return orders;
    }

    @Override
    public long getCount(Map<String, String> params) {
        return orderRepository.getCount(orderSpecificationCreator.generateQuery(params));
    }


    @Override
    public OrderDto getOrderById(long id) {
        basicValidator.validateIdIsPositive(id);
        Order order = orderRepository.getOrderById(id);
        if (order == null) {
            throw new EntityNotFoundException(ExceptionCode.NON_EXISTING_ORDER.getErrorCode(), String.valueOf(id));
        }
        return orderMapper.toDTO(orderRepository.getOrderById(id));
    }
}
