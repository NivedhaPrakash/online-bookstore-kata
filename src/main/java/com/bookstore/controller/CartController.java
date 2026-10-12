package com.bookstore.controller;

import com.bookstore.dto.AddCartItemRequest;
import com.bookstore.dto.UpdateQuantityRequest;
import com.bookstore.entity.CartItem;
import com.bookstore.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItem> addBookToCart(
            @PathVariable Long cartId,@Valid @RequestBody AddCartItemRequest request){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CartItem cartItem = cartService.addBookToCart(
                cartId, request.getBookId(), request.getQuantity(),username);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(cartItem);
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItem>> retrieveAllCartItems(@PathVariable Long cartId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<CartItem> cartItemList = cartService.getAllCartItems(cartId,username);
        return ResponseEntity.ok(cartItemList);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateQuantityRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CartItem updateCartItem = cartService.updateCartItemQuantity(cartItemId, request.getQuantity(),username);
        return ResponseEntity.ok(updateCartItem);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.removeCartItem(cartItemId,username);
        return ResponseEntity.noContent().build();
    }
}
