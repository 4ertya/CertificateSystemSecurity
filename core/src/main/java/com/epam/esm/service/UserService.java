package com.epam.esm.service;

import com.epam.esm.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<UserDto> getUsers(Map<String, String> params);

    UserDto getUserById(long id);

    long getCount();
}
