package com.studentwallet.service;

import com.studentwallet.model.User;
import com.studentwallet.model.UserByEmail;
import com.studentwallet.model.dto.AuthResponse;
import com.studentwallet.model.dto.LoginRequest;
import com.studentwallet.model.dto.RegisterRequest;
import com.studentwallet.repository.UserByEmailRepository;
import com.studentwallet.repository.UserRepository;
import com.studentwallet.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserByEmailRepository userByEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       UserByEmailRepository userByEmailRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.userByEmailRepository = userByEmailRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        if (userByEmailRepository.findById(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        String userId = UUID.randomUUID().toString();
        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setDisplayName(request.getDisplayName().trim());
        user.setCreatedAt(Instant.now());

        userRepository.save(user);
        userByEmailRepository.save(new UserByEmail(email, userId));

        String token = jwtService.generateToken(userId, email);
        return new AuthResponse(token, userId, user.getDisplayName(), email);
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        UserByEmail index = userByEmailRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        User user = userRepository.findById(index.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getUserId(), user.getEmail());
        return new AuthResponse(token, user.getUserId(), user.getDisplayName(), user.getEmail());
    }
}
