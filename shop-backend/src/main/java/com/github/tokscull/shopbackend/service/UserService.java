package com.github.tokscull.shopbackend.service;

import com.github.tokscull.shopbackend.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {
    User getUserById(Long id);
    User getUserByUsername(String username);
    Boolean existsByUsername(String username);
    List<User> getUsers();
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
