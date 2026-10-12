package com.bookstore.service;

import com.bookstore.entity.Cart;
import com.bookstore.entity.User;
import com.bookstore.repository.CartRepository;
import com.bookstore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @InjectMocks
    private RegisterService registerService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Test
    void shouldRegisterNewUser() {
        //Given: Unique username and password
        User user = new User();
        user.setUsername("nive16");
        user.setPassword("password123");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartId(100L);

        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(userRepository.save(user)).thenReturn(user);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //When: User registration is requested
        Cart savedCart = registerService.register(user);

        //Then: Password is encoded and user is saved
        assertNotEquals("password123", savedCart.getUser().getPassword());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
    }
}

