package com.bookstore.service;

import com.bookstore.entity.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void testLoginSuccess() {

        User user = new User();
        user.setUsername("nive16");
        user.setPassword("encodedPassword");

        when(userRepository.existsByUsername("nive16")).thenReturn(true);
        when(userRepository.findByUsername("nive16")).thenReturn(user);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtTokenUtil.generateToken("nive16")).thenReturn("mock-jwt-token");

        String token = loginService.login("nive16", "password123");

        assertEquals("mock-jwt-token", token);
    }

}
