package com.bookstore.controller;

import com.bookstore.dto.AddCartItemRequest;
import com.bookstore.dto.UpdateQuantityRequest;
import com.bookstore.entity.CartItem;
import com.bookstore.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        CartItem cartItem = cartService.addBookToCart(
                cartId, request.getBookId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(cartItem);
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItem>> retrieveAllCartItems(@PathVariable Long cartId){
        List<CartItem> cartItemList = cartService.getAllCartItems(cartId);
        return ResponseEntity.ok(cartItemList);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateQuantityRequest request) {

        CartItem updateCartItem = cartService.updateCartItemQuantity(cartItemId, request.getQuantity());
        return ResponseEntity.ok(updateCartItem);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
