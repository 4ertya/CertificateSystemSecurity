package com.epam.esm.controller;

import com.epam.esm.dto.AuthenticationRequestDto;
import com.epam.esm.dto.RegistrationUserDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity authenticate(@RequestBody AuthenticationRequestDto request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String token = jwtTokenProvider.createToken(request.getEmail());
        Map<String, String> result = new HashMap<>();
        result.put("email", request.getEmail());
        result.put("token", token);
        return ResponseEntity.ok(result);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public UserDto register(@RequestBody RegistrationUserDto newUserDto) {

        return userService.addUser(newUserDto);
    }

}
