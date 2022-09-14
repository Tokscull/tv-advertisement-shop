package com.github.tokscull.shopbackend.controller;

import com.github.tokscull.shopbackend.exceptions.EntityAlreadyExistsException;
import com.github.tokscull.shopbackend.exceptions.EntityNotFoundException;
import com.github.tokscull.shopbackend.model.dto.AuthenticationResponse;
import com.github.tokscull.shopbackend.model.dto.LoginRequest;
import com.github.tokscull.shopbackend.model.dto.RefreshTokenRequest;
import com.github.tokscull.shopbackend.model.dto.RegisterRequest;
import com.github.tokscull.shopbackend.service.AuthService;
import com.github.tokscull.shopbackend.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    @Autowired
    JwtEncoder encoder;

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    /**
     * Create new user
     *
     * @param registerRequest include required User information
     * @return the ResponseEntity with status 201 (CREATED) and the username in body
     * @throws EntityAlreadyExistsException when user with id already exist
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        log.info("Received request to signup new user: {}", registerRequest.getUsername());
        authService.signup(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerRequest.getUsername());
    }

    /**
     * Authenticate user
     *
     * @param loginRequest include required User information
     * @return the ResponseEntity with status 200 (OK) and the AuthenticationResponse in body
     * @throws org.springframework.security.authentication.BadCredentialsException when provided credentials don't match
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Received request to authenticate user: {}", loginRequest.getUsername());
        return ResponseEntity.ok().body(authService.login(loginRequest));
    }

    /**
     * Refresh user access, generate new tokens
     *
     * @param refreshTokenRequest include refreshToken and username
     * @return the ResponseEntity with status 200 (OK) and the AuthenticationResponse in body
     * @throws EntityNotFoundException when refreshToken not exist
     */
    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Received request to refresh token user: {}", refreshTokenRequest.getUsername());
        return authService.refreshToken(refreshTokenRequest);
    }

    /**
     * Logout user, remove user refreshToken
     *
     * @param refreshTokenRequest include refreshToken and username
     * @return the ResponseEntity with status 200 (OK) and body message
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Received request to logout user: {}", refreshTokenRequest.getUsername());
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }


}
