package com.bookstore.controller;

import com.bookstore.dto.AddCartItemRequest;
import com.bookstore.dto.UpdateQuantityRequest;
import com.bookstore.entity.CartItem;
import com.bookstore.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@Tag(name = "Cart Management", description = "Operations for managing cart items")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add book to cart", description = "Adds a book to the specified cart for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<CartItem> addBookToCart(
            @PathVariable Long cartId,@Valid @RequestBody AddCartItemRequest request){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CartItem cartItem = cartService.addBookToCart(
                cartId, request.bookId(), request.quantity(),username);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(cartItem);
    }

    @GetMapping("/{cartId}/items")
    @Operation(summary = "Retrieve all cart items", description = "Fetches all items in the specified cart for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart items retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<List<CartItem>> retrieveAllCartItems(@PathVariable Long cartId){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<CartItem> cartItemList = cartService.getAllCartItems(cartId,username);
        return ResponseEntity.ok(cartItemList);
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update cart item quantity", description = "Updates the quantity of a specific cart item for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateQuantityRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CartItem updateCartItem = cartService.updateCartItemQuantity(cartItemId, request.quantity(),username);
        return ResponseEntity.ok(updateCartItem);
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Remove cart item", description = "Deletes a specific cart item from the authenticated user's cart")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cart item removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.removeCartItem(cartItemId,username);
        return ResponseEntity.noContent().build();
    }
}
