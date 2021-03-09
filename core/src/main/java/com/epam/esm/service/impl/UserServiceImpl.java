package com.epam.esm.service.impl;

import com.epam.esm.dto.RegistrationUserDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.ExceptionCode;
import com.epam.esm.exception.alreadyexist.UserAlreadyExistException;
import com.epam.esm.exception.notfound.UserNotFoundException;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.security.JwtUser;
import com.epam.esm.service.UserService;
import com.epam.esm.validation.BasicValidator;
import com.epam.esm.validation.EntityValidator;
import com.epam.esm.validation.PaginationValidator;
import com.epam.esm.validation.SecurityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public final static String PAGE_PARAM = "page";
    public final static String SIZE_PARAM = "size";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PaginationValidator paginationValidator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final BasicValidator basicValidator;
    private final SecurityValidator securityValidator;
    private final EntityValidator entityValidator;

    @Override
    public List<UserDto> getUsers(Map<String, String> params) {
        paginationValidator.validatePaginationParams(params);
        long count = userRepository.count();
        paginationValidator.validatePageNumber(params, count);
        List<UserDto> userDTOS = new ArrayList<>();
        int size = Integer.parseInt(params.get(SIZE_PARAM));
        int page = Integer.parseInt(params.get(PAGE_PARAM));
        Pageable pageable = PageRequest.of(page - 1, size);
        userRepository.findAll(pageable).forEach(user -> userDTOS.add(userMapper.toDTO(user)));
        return userDTOS;
    }

    @Override
    public long getCount() {
        return userRepository.count();
    }

    @Override
    public void deleteUser(long id) {
        basicValidator.validateIdIsPositive(id);
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(ExceptionCode.NON_EXISTING_USER.getErrorCode(), String.valueOf(id))
        );
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserDto changeRole(long id) {
        basicValidator.validateIdIsPositive(id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(ExceptionCode.NON_EXISTING_USER.getErrorCode()
                , String.valueOf(id)));
        if (user.getRole() == Role.ROLE_ADMIN) {
            user.setRole(Role.ROLE_USER);
        } else {
            user.setRole(Role.ROLE_ADMIN);
        }
        return userMapper.toDTO(user);
    }


    @Override
    public UserDto getUserById(long id) {
        securityValidator.validateUserAccess(id);
        basicValidator.validateIdIsPositive(id);
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(ExceptionCode.NON_EXISTING_USER.getErrorCode(), String.valueOf(id))
        );
        return userMapper.toDTO(user);
    }

    @Override
    public UserDto addUser(RegistrationUserDto registerUserDto) {
        entityValidator.validateRegistrationUser(registerUserDto);
        Optional<User> user =
                userRepository.findByEmail(registerUserDto.getEmail());

        if (user.isPresent()) {
            throw new UserAlreadyExistException(ExceptionCode.USER_ALREADY_EXIST.getErrorCode(),
                    registerUserDto.getEmail());
        }
        User newUser = new User();
        newUser.setName(registerUserDto.getName());
        newUser.setSurname(registerUserDto.getSurname());
        newUser.setEmail(registerUserDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        newUser.setRole(Role.ROLE_USER);

        userRepository.save(newUser);
        return userMapper.toDTO(newUser);
    }

    @Override
    public Role getUserRole(long id){
        basicValidator.validateIdIsPositive(id);
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(ExceptionCode.NON_EXISTING_USER.getErrorCode(), String.valueOf(id))
        );
        return user.getRole();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException(ExceptionCode.NON_EXISTING_USER.getErrorCode(),
                                email));
        return new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRole())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Role userRole) {
        return Collections.singletonList(new SimpleGrantedAuthority(userRole.name()));
    }
}
