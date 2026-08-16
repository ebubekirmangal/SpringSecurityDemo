package com.example.security_demo.controllers;

import com.example.security_demo.core.services.AuthenticationService;
import com.example.security_demo.services.dtos.requests.AuthenticationRequest;
import com.example.security_demo.services.dtos.requests.RegisterRequest;
import com.example.security_demo.services.dtos.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse register(
            @RequestBody  RegisterRequest request
    ) {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public AuthenticationResponse authenticate(
            @RequestBody  AuthenticationRequest request
    ) {
        return authenticationService.authenticate(request);
    }
}
