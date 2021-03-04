package com.epam.esm.controller;

import com.epam.esm.dto.NewOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.HateoasBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final HateoasBuilder hateoasBuilder;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@RequestBody NewOrderDto newOrderDto) {
        OrderDto orderDTO = orderService.addOrder(newOrderDto);
        return hateoasBuilder.addLinksForOrder(orderDTO);
    }


    @GetMapping
    public RepresentationModel getAllOrders(@RequestParam Map<String, String> params) {
        List<OrderDto> orders = orderService.getOrders(params);
        long ordersCount = orderService.getCount(params);
        return hateoasBuilder.addLinksForListOfOrders(orders, params, ordersCount);
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable("id") long id) {
        OrderDto orderDTO = orderService.getOrderById(id);
        return hateoasBuilder.addLinksForOrder(orderDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable long id){
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
