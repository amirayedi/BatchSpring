package com.demo.service;


import com.demo.dto.RegisterRequest;
import com.demo.utils.JwtService;
import com.demo.entity.Enum.Role;
import com.demo.entity.User;
import com.demo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public RegisterRequest register(RegisterRequest request) {
        User user = createUserFromRequest(request);
        user.setRole(Role.ADMIN);
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setToken(jwtToken);
        return registerRequest;
    }

    public RegisterRequest authenticate(RegisterRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setToken(jwtToken);
        return registerRequest;
    }


    private User createUserFromRequest(RegisterRequest request) {
        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        return user;
    }
}
