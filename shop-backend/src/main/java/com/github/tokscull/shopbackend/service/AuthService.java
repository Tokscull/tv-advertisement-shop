package com.github.tokscull.shopbackend.service;

import com.github.tokscull.shopbackend.exceptions.EntityAlreadyExistsException;
import com.github.tokscull.shopbackend.model.Role;
import com.github.tokscull.shopbackend.model.User;
import com.github.tokscull.shopbackend.model.dto.AuthenticationResponse;
import com.github.tokscull.shopbackend.model.dto.LoginRequest;
import com.github.tokscull.shopbackend.model.dto.RefreshTokenRequest;
import com.github.tokscull.shopbackend.model.dto.RegisterRequest;
import com.github.tokscull.shopbackend.repository.RoleRepository;
import com.github.tokscull.shopbackend.repository.UserRepository;
import com.github.tokscull.shopbackend.util.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;


    public void signup(RegisterRequest registerRequest) {
        if (userService.existsByUsername(registerRequest.getUsername())) {
            throw new EntityAlreadyExistsException("User already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(Set.of(roleRepository.findByName("ROLE_USER")));
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        Set<String> roles = userService.getUserByUsername(refreshTokenRequest.getUsername())
                .getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        String token = jwtProvider.generateTokenWithUserNameAndRoles(refreshTokenRequest.getUsername(), roles);
        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

}
