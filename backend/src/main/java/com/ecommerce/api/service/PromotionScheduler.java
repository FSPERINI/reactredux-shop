package com.ecommerce.api.service;

import com.ecommerce.api.dto.PromotionEvent;
import com.ecommerce.api.model.Promotion;
import com.ecommerce.api.repository.PromotionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromotionScheduler {

    private final PromotionRepository promotionRepository;
    private final PromotionService promotionService;
    private final SseService sseService;

    public PromotionScheduler(PromotionRepository promotionRepository,
                              PromotionService promotionService,
                              SseService sseService) {
        this.promotionRepository = promotionRepository;
        this.promotionService = promotionService;
        this.sseService = sseService;
    }

    @Scheduled(fixedRate = 30000)
    public void checkPromotions() {
        LocalDateTime now = LocalDateTime.now();

        // Activate promotions that should be active
        List<Promotion> toActivate = promotionRepository.findInactiveToActivate(now);
        for (Promotion promotion : toActivate) {
            promotion.setActive(true);
            promotionRepository.save(promotion);

            List<Long> affectedProductIds = promotionService.getAffectedProductIds(promotion);
            String eventType = promotion.isFlashSale() ? "flash_sale_started" : "promotion_started";
            PromotionEvent event = new PromotionEvent(eventType, promotion, affectedProductIds);
            sseService.sendEvent(eventType, event);
        }

        // Deactivate promotions that have expired
        List<Promotion> toDeactivate = promotionRepository.findActiveToDeactivate(now);
        for (Promotion promotion : toDeactivate) {
            promotion.setActive(false);
            promotionRepository.save(promotion);

            List<Long> affectedProductIds = promotionService.getAffectedProductIds(promotion);
            String eventType = promotion.isFlashSale() ? "flash_sale_ended" : "promotion_ended";
            PromotionEvent event = new PromotionEvent(eventType, promotion, affectedProductIds);
            sseService.sendEvent(eventType, event);
        }

        // Send heartbeat
        sseService.sendEvent("heartbeat", "ping");
    }
}
