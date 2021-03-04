package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.model.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.HateoasBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final HateoasBuilder hateoasBuilder;
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RepresentationModel getUsers(@RequestParam Map<String, String> params) {
        List<UserDto> users = userService.getUsers(params);
        return hateoasBuilder.addLinksForListOfUsers(users,params,users.size());
    }

    @GetMapping("/{id}")
    public RepresentationModel getUserById(@PathVariable("id") long id) {
        UserDto userDTO = userService.getUserById(id);
        return hateoasBuilder.addLinksForUser(userDTO);
    }

    @GetMapping("/{id}/orders")
    public RepresentationModel getUserOrders(@PathVariable("id") long id, @RequestParam Map<String, String> params) {
        List<OrderDto> orders = orderService.getOrdersByUserId(id,params);
        return hateoasBuilder.addLinksForListOfOrders(orders,params,orders.size());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeUser(@PathVariable long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public RepresentationModel changeRole(@PathVariable long id) {
        UserDto userDto = userService.changeRole(id);
        return hateoasBuilder.addLinksForUser(userDto);
    }
}
