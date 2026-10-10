package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import com.bookstore.entity.CartItem;
import com.bookstore.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CartControllerTest {

    @MockitoBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddBookToCart() throws Exception {
        //Given
        Cart cart = new Cart();
        cart.setCartId(100L);

        Book book =  new Book(
                1L,
                "Kalki Purana (Part-8)",
                "Kalki",
                new BigDecimal("200.00"),
                10
        );

        CartItem savedCartItem = new CartItem(1L, cart, book, 2);

        when(cartService.addBookToCart(100L,1L,2)).thenReturn(savedCartItem);

        String requestBody = """
                {
                "bookId": 1,
                "quantity": 2
                }
                """;

        //When and Then
        mockMvc.perform(post("/api/carts/{cartId}/items",100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cartItemId").value(1L))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.book.bookId").value(1L))
                .andExpect(jsonPath("$.book.title").value("Kalki Purana (Part-8)"));

    }

    @Test
    void testGetAllCartItems() throws Exception {
        //Given: Cart contains two cart items
        Long cartId = 100L;
        Cart cart = new Cart();
        cart.setCartId(cartId);

        Book firstBook =  new Book(
                1L,
                "Kalki Purana (Part-8)",
                "Kalki",
                new BigDecimal("200.00"),
                10
        );
        Book secondBook =  new Book(
                2L,
                "Kalki Purana (Part-9)",
                "Kalki",
                new BigDecimal("230.00"),
                8
        );

        CartItem firstCartItem = new CartItem(1L, cart, firstBook, 2);
        CartItem secondCartItem = new CartItem(2L, cart, secondBook, 3);
        List<CartItem> cartItemList = Arrays.asList(firstCartItem,secondCartItem);
        when(cartService.getAllCartItems(cartId)).thenReturn(cartItemList);

        //When and Then
        mockMvc.perform(get("/api/carts/{cartId}/items",100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].cartItemId").value(1L))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].book.bookId").value(1L))
                .andExpect(jsonPath("$[0].book.title").value("Kalki Purana (Part-8)"))
                .andExpect(jsonPath("$[1].cartItemId").value(2L))
                .andExpect(jsonPath("$[1].quantity").value(3));

    }

    @Test
    void testUpdateCartItemQuantity() throws Exception {
        // Given
        Long cartItemId = 1L;
        Cart cart = new Cart();
        cart.setCartId(100L);

        Book book = new Book(
                1L,
                "Kalki Purana (Part-8)",
                "Kalki",
                new BigDecimal("200.00"),
                10
        );

        // Updated cart item with new quantity
        CartItem updatedCartItem = new CartItem(cartItemId, cart, book, 5);

        when(cartService.updateCartItemQuantity(cartItemId, 5)).thenReturn(updatedCartItem);

        String requestBody = """
            {
              "quantity": 5
            }
            """;

        // When & Then
        mockMvc.perform(put("/api/carts/items/{cartItemId}", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartItemId").value(1L))
                .andExpect(jsonPath("$.quantity").value(5))
                .andExpect(jsonPath("$.book.bookId").value(1L))
                .andExpect(jsonPath("$.book.title").value("Kalki Purana (Part-8)"));
    }

    @Test
    void testRemoveCartItem() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/carts/items/{cartItemId}", 1L))
                .andExpect(status().isNoContent());

        verify(cartService).removeCartItem(1L);
    }
}
