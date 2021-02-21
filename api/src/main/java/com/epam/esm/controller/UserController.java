package com.epam.esm.controller;

import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import com.epam.esm.util.HateoasBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final HateoasBuilder hateoasBuilder;

    @GetMapping
    public RepresentationModel getUsers(@RequestParam Map<String, String> params) {
        List<UserDto> users = userService.getUsers(params);
        long usersCount = userService.getCount();
        return hateoasBuilder.addLinksForListOfUsers(users, params, usersCount);
    }

    @GetMapping("/{id}")
    public RepresentationModel getUserById(@PathVariable("id") long id) {
        UserDto userDTO = userService.getUserById(id);
        return hateoasBuilder.addLinksForUser(userDTO);
    }
}
