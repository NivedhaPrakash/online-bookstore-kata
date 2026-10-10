package com.bookstore.controller;

import com.bookstore.entity.Order;
import com.bookstore.entity.OrderStatus;
import com.bookstore.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
}
