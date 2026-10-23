package com.lunaticcart.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lunaticcart.dto.LoginRequest;
import com.lunaticcart.entity.User;
import com.lunaticcart.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {

            throw new BadCredentialsException(
                    "Invalid email or password");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
