package com.ecommerce.api.controller;

import com.ecommerce.api.model.Order;
import com.ecommerce.api.model.User;
import com.ecommerce.api.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@Tag(name = "Account", description = "User account operations")
@SecurityRequirement(name = "Bearer Token")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/profile")
    @Operation(summary = "Get user profile", description = "Get the authenticated user's profile information")
    public ResponseEntity<User> getProfile(Principal principal) {
        return ResponseEntity.ok(accountService.getProfile(principal.getName()));
    }

    @GetMapping("/orders")
    @Operation(summary = "Get user orders", description = "Get all orders placed by the authenticated user")
    public ResponseEntity<List<Order>> getOrders(Principal principal) {
        return ResponseEntity.ok(accountService.getOrders(principal.getName()));
    }
}
