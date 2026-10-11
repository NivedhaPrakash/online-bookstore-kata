package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.exception.CartNotFoundException;
import com.bookstore.exception.EmptyCartException;
import com.bookstore.exception.InsufficientStockException;
import com.bookstore.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public Order checkout(Long cartId){

        Cart cart = cartRepository.findById(cartId)
                                  .orElseThrow(() -> new CartNotFoundException("Cart Not Found"));

        List<CartItem> cartItems = cartItemRepository.findByCartCartId(cart.getCartId());

        if (cartItems.isEmpty()) {
            throw new EmptyCartException("Cannot Checkout An Empty Cart");
        }

        for (CartItem item : cartItems) {
            if (item.getQuantity() > item.getBook().getAvailableStock()) {
                throw new InsufficientStockException("Insufficient Stock For Book : " + item.getBook().getTitle());
            }
        }

        BigDecimal totalAmount = cartItems.stream()
                .map(item -> item.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        for (CartItem item : cartItems) {

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setBookId(item.getBook().getBookId());
                orderItem.setBookTitle(item.getBook().getTitle());
                orderItem.setUnitPrice(item.getBook().getPrice());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSubTotal(item.getBook().getPrice()
                         .multiply(BigDecimal.valueOf(item.getQuantity())));
                orderItemRepository.save(orderItem);

                // Reduce available stock
                Book book = item.getBook();
                book.setAvailableStock(
                        book.getAvailableStock() - item.getQuantity()
                );
                bookRepository.save(book);
        }

        // Clear all items from cart
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return savedOrder;

    }
}
