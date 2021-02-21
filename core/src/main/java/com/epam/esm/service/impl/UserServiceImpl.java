package com.epam.esm.service.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ExceptionCode;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.validation.PaginationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public final static String PAGE_PARAM="page";
    public final static String SIZE_PARAM="size";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PaginationValidator paginationValidator;

    @Override
    public List<UserDto> getUsers(Map<String, String> params) {
        paginationValidator.validatePaginationParams(params);
        long count = userRepository.getCount();
        paginationValidator.validatePageNumber(params,count);
        List<UserDto> userDTOS = new ArrayList<>();
        int limit = Integer.parseInt(params.get(SIZE_PARAM));
        int offset = (Integer.parseInt(params.get(PAGE_PARAM)) - 1) * limit;
        userRepository.getUsers(limit, offset).forEach(user -> userDTOS.add(userMapper.toDTO(user)));
        return userDTOS;
    }

    @Override
    public long getCount() {
        return userRepository.getCount();
    }

    @Override
    public UserDto getUserById(long id) {
        User user = userRepository.getUserById(id);
        if (user==null){
            throw new EntityNotFoundException(ExceptionCode.NON_EXISTING_USER.getErrorCode(), String.valueOf(id));
        }
        return userMapper.toDTO(user);
    }


}
