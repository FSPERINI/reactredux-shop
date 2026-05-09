package com.ecommerce.api.controller;

import com.ecommerce.api.dto.AddToCartRequest;
import com.ecommerce.api.dto.UpdateCartItemRequest;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "Shopping cart operations")
@SecurityRequirement(name = "Bearer Token")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @Operation(summary = "Get cart contents", description = "Get all items currently in the shopping cart")
    public ResponseEntity<List<CartItem>> getCart(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(cartService.getCartItems(email));
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to cart", description = "Add a product to the shopping cart")
    public ResponseEntity<CartItem> addToCart(@Valid @RequestBody AddToCartRequest request, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(cartService.addToCart(request.getProductId(), request.getQuantity(), email));
    }

    @PutMapping("/items/{productId}")
    @Operation(summary = "Update cart item quantity", description = "Update the quantity of a product in the cart")
    public ResponseEntity<CartItem> updateCartItem(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request,
            Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(cartService.updateCartItem(productId, request.getQuantity(), email));
    }

    @DeleteMapping("/items/{productId}")
    @Operation(summary = "Remove item from cart", description = "Remove a product from the shopping cart")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long productId, Principal principal) {
        String email = principal.getName();
        cartService.removeCartItem(productId, email);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Clear cart", description = "Remove all items from the shopping cart")
    public ResponseEntity<Void> clearCart(Principal principal) {
        String email = principal.getName();
        cartService.clearCart(email);
        return ResponseEntity.noContent().build();
    }
}
