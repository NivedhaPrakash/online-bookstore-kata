package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.CartNotFoundException;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void shouldAddBookToCart(){
        //Given: Cart and book exists and quantity is valid
        Long cartId = 100L;
        Long bookId = 1L;
        Integer quantity = 2;

        Cart cart = new Cart();
        cart.setCartId(cartId);

        Book book =  new Book(
                bookId,
                "Kalki Purana (Part-8)",
                "Kalki",
                new BigDecimal("200.00"),
                10
        );

        CartItem savedCartItem = new CartItem(1L, cart, book, 2);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cartItemRepository.findByCartCartIdAndBookBookId(cartId, bookId))
                .thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(savedCartItem);

        //When: Book added to cart
        CartItem actualCartItem = cartService.addBookToCart(100L,1l,2);

        //Then: Cart item is created with book and quantity
        assertEquals(cart, actualCartItem.getCart());
        assertEquals(book, actualCartItem.getBook());
        assertEquals(quantity, actualCartItem.getQuantity());
    }

    @Test
    void shouldUpdateQuantityWhenBookAlreadyInCart() {
        // Given: Cart and book exist
        Long cartId = 100L;
        Long bookId = 1L;

        Cart cart = new Cart();
        cart.setCartId(cartId);

        Book book = new Book(
                bookId,
                "Kalki Purana (Part-8)",
                "Kalki",
                new BigDecimal("200.00"),
                10
        );

        CartItem existingCartItem = new CartItem(1L, cart, book, 2);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cartItemRepository.findByCartCartIdAndBookBookId(cartId, bookId))
                .thenReturn(Optional.of(existingCartItem));
        when(cartItemRepository.save(existingCartItem)).thenReturn(existingCartItem);

        // When: Add same book again with quantity 3
        CartItem updatedCartItem = cartService.addBookToCart(cartId, bookId, 3);

        // Then: Quantity is incremented (2 + 3 = 5)
        assertEquals(5, updatedCartItem.getQuantity());
        assertEquals(book, updatedCartItem.getBook());
        assertEquals(cart, updatedCartItem.getCart());

        // Verify save was called
        verify(cartItemRepository).save(existingCartItem);
    }


    @Test
    void shouldReturnAllCartItems(){
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

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartCartId(cartId)).thenReturn(cartItemList);

        //When: Cart items are retrieved
        List<CartItem> actualCartItems = cartService.getAllCartItems(cartId);

        //Then: Both Cart items are returned
        assertEquals(2, actualCartItems.size());
        assertEquals(cart, actualCartItems.get(0).getCart());
        assertEquals(firstBook, actualCartItems.get(0).getBook());
        assertEquals(firstCartItem.getQuantity(), actualCartItems.get(0).getQuantity());
        assertEquals(cart, actualCartItems.get(1).getCart());
        assertEquals(secondBook, actualCartItems.get(1).getBook());
        assertEquals(secondCartItem.getQuantity(), actualCartItems.get(1).getQuantity());

    }

    @Test
    void shouldUpdateCArtItemQuantity() {
        //Given: Cart Item exists with quantity 2
        Long cartId = 100L;

        Cart cart = new Cart();
        cart.setCartId(cartId);

        Book book =  new Book(
                1L,
                "Kalki Purana (Part-8)",
                "Kalki",
                new BigDecimal("200.00"),
                10
        );

        CartItem existingCartItem = new CartItem(1L, cart, book, 2);

        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(existingCartItem));
        when(cartItemRepository.save(existingCartItem)).thenReturn(existingCartItem);

        //When: Quantity is updated to 3
        CartItem updatedCartItem = cartService.updateCartItemQuantity(1L,4);

        //Then: Cart Item contains quantity 3
        assertEquals(4,updatedCartItem.getQuantity());
    }

    @Test
    void shouldRemoveCartItem(){
        //Given: Cart item with ID 1 exists
        Long cartId = 100L;

        Cart cart = new Cart();
        cart.setCartId(cartId);

        Book book =  new Book(
                1L,
                "Kalki Purana (Part-8)",
                "Kalki",
                new BigDecimal("200.00"),
                10
        );

        CartItem existingCartItem = new CartItem(1L, cart, book, 2);

        when(cartItemRepository.existsById(1L)).thenReturn(true);

        //When: Cart item is removed
        cartService.removeCartItem(1L);

        //Then: Repository deleted the cart item
        verify(cartItemRepository).deleteById(1L);

    }
    @Test
    void shouldThrowWhenBookNotFound() {
        // Given: cart exists but book does not
        Cart cart = new Cart();
        cart.setCartId(100L);
        when(cartRepository.findById(100L)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // When: trying to add book to cart
        BookNotFoundException ex = assertThrows(BookNotFoundException.class,
                () -> cartService.addBookToCart(100L, 1L, 2));

        // Then: exception is thrown
        assertEquals("Book Not Found", ex.getMessage());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingCartItem() {
        // Given: cart item does not exist
        when(cartItemRepository.findById(999L)).thenReturn(Optional.empty());

        // When: trying to update non-existing cart item
        CartNotFoundException ex = assertThrows(CartNotFoundException.class,
                () -> cartService.updateCartItemQuantity(999L, 3));

        // Then: exception is thrown
        assertEquals("Cart Item Not Found", ex.getMessage());
    }

    @Test
    void shouldThrowWhenRemovingNonExistingCartItem() {
        // Given: cart item does not exist
        when(cartItemRepository.existsById(999L)).thenReturn(false);

        // When: trying to remove non-existing cart item
        CartNotFoundException ex = assertThrows(CartNotFoundException.class,
                () -> cartService.removeCartItem(999L));

        // Then: exception is thrown
        assertEquals("Cart Item Not Found", ex.getMessage());
    }

}
