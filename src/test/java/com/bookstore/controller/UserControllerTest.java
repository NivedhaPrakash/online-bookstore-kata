package com.bookstore.controller;

import com.bookstore.config.SecurityConfig;
import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

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
        when(userService.register(any(User.class))).thenReturn(user);

        // When & Then: Register API called, expect 201 Created
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("nive16"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }
}
