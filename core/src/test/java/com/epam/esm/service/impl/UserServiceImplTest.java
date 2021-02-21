package com.epam.esm.service.impl;

import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.validation.PaginationValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Spy
    private PaginationValidator paginationValidator;
    @Spy
    private UserMapper userMapper = new UserMapper(new ModelMapper());


    @Test
    void getUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        when(userRepository.getUsers(10, 0)).thenReturn(users);
        assertEquals(3, userService.getUsers(new HashMap<>()).size());
    }

    @Test
    void getUserById() {
        User user = new User();
        user.setId(1L);
        user.setName("Mitya");
        when(userRepository.getUserById(1)).thenReturn(user);
        assertEquals("Mitya", userService.getUserById(1).getName());
    }
}