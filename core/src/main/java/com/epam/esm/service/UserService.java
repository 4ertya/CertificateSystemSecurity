package com.epam.esm.service;

import com.epam.esm.dto.RegistrationUserDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.model.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {

    List<UserDto> getUsers(Map<String, String> params);

    UserDto getUserById(long id);

    UserDto addUser(RegistrationUserDto registerUserDto);

    long getCount();

    void deleteUser(long id);

    Role getUserRole(long id);

    UserDto changeRole(long id);
}
