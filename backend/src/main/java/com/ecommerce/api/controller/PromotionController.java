package com.ecommerce.api.controller;

import com.ecommerce.api.model.Promotion;
import com.ecommerce.api.service.PromotionService;
import com.ecommerce.api.service.SseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@Tag(name = "Promotions", description = "Promotion and flash sale operations")
public class PromotionController {

    private final PromotionService promotionService;
    private final SseService sseService;

    public PromotionController(PromotionService promotionService, SseService sseService) {
        this.promotionService = promotionService;
        this.sseService = sseService;
    }

    @GetMapping
    @Operation(summary = "List all promotions", description = "Get all promotions including inactive ones")
    public ResponseEntity<List<Promotion>> getAllPromotions() {
        return ResponseEntity.ok(promotionService.getAll());
    }

    @GetMapping("/active")
    @Operation(summary = "List active promotions", description = "Get only currently active promotions")
    public ResponseEntity<List<Promotion>> getActivePromotions() {
        return ResponseEntity.ok(promotionService.getActive());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get promotion by ID", description = "Get detailed information about a specific promotion")
    public ResponseEntity<Promotion> getPromotion(@PathVariable Long id) {
        return ResponseEntity.ok(promotionService.getById(id));
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Stream promotion events", description = "SSE endpoint for real-time promotion updates")
    public SseEmitter streamPromotions() {
        SseEmitter emitter = sseService.createEmitter();

        // Send initial state with all active promotions
        try {
            List<Promotion> activePromotions = promotionService.getActive();
            emitter.send(SseEmitter.event()
                    .name("initial_state")
                    .data(activePromotions));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }
}
