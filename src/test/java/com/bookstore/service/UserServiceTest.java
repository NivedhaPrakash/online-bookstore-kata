package com.bookstore.service;

import com.bookstore.entity.User;
import com.bookstore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldRegisterNewUser() {
        //Given: Unique username and password
        User user = new User();
        user.setUsername("nive16");
        user.setPassword("password123");

        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(userRepository.save(user)).thenReturn(user);

        //When: User registration is requested
        User savedUser = userService.register(user);

        //Then: Password is encoded and user is saved
        assertNotEquals("password123", savedUser.getPassword());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
    }
}

