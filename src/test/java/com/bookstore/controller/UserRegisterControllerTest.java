package com.bookstore.controller;

import com.bookstore.config.SecurityConfig;
import com.bookstore.entity.Cart;
import com.bookstore.entity.User;
import com.bookstore.service.RegisterService;
import com.bookstore.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRegisterController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserRegisterControllerTest {

    @MockitoBean
    private RegisterService registerService;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void testShouldRegisterNewUser() throws Exception {
        // Given: Valid username and password
        String requestBody = """
            {
              "username": "nive16",
              "password": "password123"
            }
            """;

        User user = new User();
        user.setUsername("nive16");
        user.setPassword("password123");

        Cart userCart = new Cart();
        userCart.setUser(user);
        userCart.setCartId(100L);

        when(registerService.register(any(User.class))).thenReturn(userCart);

        // When & Then: Register API called, expect 201 Created
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("nive16"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }
}
