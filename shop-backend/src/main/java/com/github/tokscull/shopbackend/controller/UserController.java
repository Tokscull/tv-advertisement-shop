package com.github.tokscull.shopbackend.controller;

import com.github.tokscull.shopbackend.exception.EntityNotFoundException;
import com.github.tokscull.shopbackend.model.User;
import com.github.tokscull.shopbackend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Get all users
     *
     * @return the ResponseEntity with status 200 (OK) and the List of User in body
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        log.info("Received request to get all users");
        return ResponseEntity.ok().body(userService.getUsers());
    }

    /**
     * Get user by id
     *
     * @param userId the User id which the requested entity should match
     * @return the ResponseEntity with status 200 (OK) and the User in body
     * @throws EntityNotFoundException when user with id not exist
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        log.info("Received request to get user by id: {}", userId);
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    /**
     * Get user by username
     *
     * @param username the User username which the requested entity should match
     * @return the ResponseEntity with status 200 (OK) and the User in body
     * @throws EntityNotFoundException when user with username not exist
     */
    @GetMapping(params = "username")
    public ResponseEntity<User> getUserByUsername(@RequestParam(value = "username") String username) {
        log.info("Received request to get user by username: {}", username);
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

}
