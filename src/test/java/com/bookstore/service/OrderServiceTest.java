package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.exception.CartNotFoundException;
import com.bookstore.exception.EmptyCartException;
import com.bookstore.exception.InsufficientStockException;
import com.bookstore.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldCreateOrderFromCartItems(){
        // Given: Cart with two items
        Long cartId = 100L;
        Cart cart = new Cart();
        cart.setCartId(cartId);

        Book firstBook = new Book(
                1L,
                "Kalki Purana (Part-8)",
                "Kalki",
                new BigDecimal("200.00"),
                10
        );
        Book secondBook = new Book(
                2L,
                "Kalki Purana (Part-9)",
                "Kalki",
                new BigDecimal("200.00"),
                8
        );

        CartItem firstCartItem = new CartItem(1L, cart, firstBook, 2);
        CartItem secondCartItem = new CartItem(2L, cart, secondBook, 3);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(firstCartItem);
        cartItems.add(secondCartItem);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartCartId(cartId)).thenReturn(cartItems);

        Order savedOrder = new Order();
        savedOrder.setOrderId(10L);
        savedOrder.setTotalAmount(new BigDecimal("1000.00"));
        savedOrder.setStatus(OrderStatus.PLACED);
        savedOrder.setCreatedAt(LocalDateTime.now());

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderItemRepository.save(any(OrderItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When: Checkout is called
        Order checkoutOrder = orderService.checkout(cartId);

        //Then
        assertEquals(new BigDecimal("1000.00"), checkoutOrder.getTotalAmount());
        assertEquals(OrderStatus.PLACED, checkoutOrder.getStatus());
        assertEquals(10L, checkoutOrder.getOrderId());

        // Verify cart cleared
        verify(cartRepository).save(cart);

        // Verify stock reduced
        assertEquals(8, firstBook.getAvailableStock());
        assertEquals(5, secondBook.getAvailableStock());
        verify(bookRepository, times(2)).save(any(Book.class));
    }

    @Test
    void shouldThrowNotFoundException() {
        // Given: cart does not exist
        when(cartRepository.findById(100L)).thenReturn(Optional.empty());

        // When & Then
        CartNotFoundException ex = assertThrows(CartNotFoundException.class,
                () -> orderService.checkout(100L));

        assertEquals("Cart Not Found", ex.getMessage());
    }

    @Test
    void givenEmptyCart_whenCheckout_thenThrowEmptyCartException() {
        // Given: cart exists but has no items
        Cart cart = new Cart();
        cart.setCartId(100L);
        when(cartRepository.findById(100L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartCartId(100L)).thenReturn(List.of());

        // When & Then
        EmptyCartException ex = assertThrows(EmptyCartException.class,
                () -> orderService.checkout(100L));

        assertEquals("Cannot Checkout An Empty Cart", ex.getMessage());
    }

    @Test
    void givenInsufficientStock_whenCheckout_thenThrowInsufficientStockException() {
        // Given: cart exists with one item but stock is insufficient
        Cart cart = new Cart();
        cart.setCartId(100L);

        Book book = new Book(
                1L,
                "Kalki Purana (Part-8)",
                "Kalki",
                new BigDecimal("200.00"),
                1
        );
        CartItem cartItem = new CartItem(1L, cart, book, 5);

        when(cartRepository.findById(100L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartCartId(100L)).thenReturn(List.of(cartItem));

        // When & Then
        InsufficientStockException ex = assertThrows(InsufficientStockException.class,
                () -> orderService.checkout(100L));

        assertEquals("Insufficient Stock For Book : Kalki Purana (Part-8)", ex.getMessage());
    }

}
