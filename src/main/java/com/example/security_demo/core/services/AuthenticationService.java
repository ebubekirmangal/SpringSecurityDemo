package com.example.security_demo.core.services;

import com.example.security_demo.entities.User;
import com.example.security_demo.entities.enums.Role;
import com.example.security_demo.repositories.UserRepository;
import com.example.security_demo.services.dtos.requests.AuthenticationRequest;
import com.example.security_demo.services.dtos.requests.RegisterRequest;
import com.example.security_demo.services.dtos.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthenticationResponse register(RegisterRequest request) {

        Optional<User> user =
                userRepository.findByEmail(request.getEmail());

        if (user.isPresent()) {
            throw new RuntimeException("Kullanıcı zaten kayıtlı");
        }

        User newUser = new User();

        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());

        newUser.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        newUser.setRole(Role.USER);

        userRepository.save(newUser);

        String jwt = jwtService.generateToken(newUser);

        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Kullanıcı bulunamadı")
                );

        String jwt = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

}
