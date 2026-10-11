package com.bookstore.controller;

import com.bookstore.entity.Order;
import com.bookstore.entity.OrderStatus;
import com.bookstore.exception.*;
import com.bookstore.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void shouldCheckoutCartAndReturnOrder() throws Exception {
        // Given: a mocked order returned by service
        Order order = new Order();
        order.setOrderId(10L);
        order.setTotalAmount(new BigDecimal("1000.00"));
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());

        Mockito.when(orderService.checkout(anyLong())).thenReturn(order);

        // When & Then: perform POST request
        mockMvc.perform(post("/api/orders/checkout/{cartId}", 100L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(10L))
                .andExpect(jsonPath("$.totalAmount").value(1000.00))
                .andExpect(jsonPath("$.status").value("PLACED"));
    }

    @Test
    void shouldReturnBadRequestForEmptyCart() throws Exception {
        Mockito.when(orderService.checkout(anyLong()))
                .thenThrow(new EmptyCartException("Cannot checkout an empty cart"));

        mockMvc.perform(post("/api/orders/checkout/{cartId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Cannot checkout an empty cart"));
    }

    @Test
    void shouldReturnConflictForInsufficientStock() throws Exception {
        Mockito.when(orderService.checkout(anyLong()))
                .thenThrow(new InsufficientStockException("Insufficient Stock"));

        mockMvc.perform(post("/api/orders/checkout/{cartId}", 1L))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Insufficient Stock"));
    }

    @Test
    void shouldReturnForbidden() throws Exception {
        Mockito.when(orderService.checkout(anyLong()))
                .thenThrow(new ForbiddenException("Access denied"));

        mockMvc.perform(post("/api/orders/checkout/{cartId}", 1L))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

}
