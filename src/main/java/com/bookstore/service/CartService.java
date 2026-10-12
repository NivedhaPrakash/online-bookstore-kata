package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import com.bookstore.entity.CartItem;
import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.CartNotFoundException;
import com.bookstore.exception.UnauthorizedException;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    private void validateCartOwnership(Cart cart, String username) {
        if (!cart.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("Cart does not belong to user");
        }
    }

    public CartItem addBookToCart(Long cartId, Long bookId, Integer quantity,String username){

        Cart cart = cartRepository.findById(cartId)
                                  .orElseThrow(() -> new CartNotFoundException("Cart Not Found"));

        Book book = bookRepository.findById(bookId)
                                  .orElseThrow(() -> new BookNotFoundException("Book Not Found"));

        validateCartOwnership(cart, username);

        Optional<CartItem> existingItem = cartItemRepository.findByCartCartIdAndBookBookId(cartId, bookId);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(quantity);
            return cartItemRepository.save(cartItem);
        }
    }

    public List<CartItem> getAllCartItems(Long cartId, String username){
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart Not Found"));

        validateCartOwnership(cart, username);

        List<CartItem> cartItemList = cartItemRepository.findByCartCartId(cart.getCartId());
        return cartItemList;
    }

    public CartItem updateCartItemQuantity(Long cartItemId, Integer quantity,String username){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartNotFoundException("Cart Item Not Found"));
        cartItem.setQuantity(quantity);
        validateCartOwnership(cartItem.getCart(), username);
        return  cartItemRepository.save(cartItem);
    }

    public void removeCartItem(Long cartItemId, String username){
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartNotFoundException("Cart Item Not Found"));

        validateCartOwnership(cartItem.getCart(), username);

        cartItemRepository.deleteById(cartItemId);
    }
}
