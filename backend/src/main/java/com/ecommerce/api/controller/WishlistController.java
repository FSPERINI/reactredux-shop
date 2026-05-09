package com.ecommerce.api.controller;

import com.ecommerce.api.model.WishlistItem;
import com.ecommerce.api.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "Wishlist", description = "Wishlist / favorites operations")
@SecurityRequirement(name = "Bearer Token")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    @Operation(summary = "Get wishlist", description = "Get all items in the wishlist")
    public ResponseEntity<List<WishlistItem>> getWishlist(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(wishlistService.getWishlist(email));
    }

    @PostMapping("/{productId}")
    @Operation(summary = "Add to wishlist", description = "Add a product to the wishlist")
    public ResponseEntity<WishlistItem> addToWishlist(@PathVariable Long productId, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(wishlistService.addToWishlist(productId, email));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Remove from wishlist", description = "Remove a product from the wishlist")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long productId, Principal principal) {
        String email = principal.getName();
        wishlistService.removeFromWishlist(productId, email);
        return ResponseEntity.noContent().build();
    }
}
