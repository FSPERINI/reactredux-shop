package com.ecommerce.api.dto;

import com.ecommerce.api.model.Promotion;

import java.time.LocalDateTime;
import java.util.List;

public class PromotionEvent {

    private String eventType;
    private Promotion promotion;
    private List<Long> affectedProductIds;
    private LocalDateTime timestamp;

    public PromotionEvent() {}

    public PromotionEvent(String eventType, Promotion promotion, List<Long> affectedProductIds) {
        this.eventType = eventType;
        this.promotion = promotion;
        this.affectedProductIds = affectedProductIds;
        this.timestamp = LocalDateTime.now();
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Promotion getPromotion() { return promotion; }
    public void setPromotion(Promotion promotion) { this.promotion = promotion; }

    public List<Long> getAffectedProductIds() { return affectedProductIds; }
    public void setAffectedProductIds(List<Long> affectedProductIds) { this.affectedProductIds = affectedProductIds; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
