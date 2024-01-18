package com.skillbox.cryptobot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "subscribers")
@NoArgsConstructor
public @Data class Subscriber {
    public Subscriber(long telegramId, double subscriptionPrice) {
        this.telegramId = telegramId;
        this.subscriptionPrice = subscriptionPrice;
    }
    public Subscriber(long telegramId) {
        this.telegramId = telegramId;
        this.subscriptionPrice = null;
    }

    @Id
    @UuidGenerator
    private UUID uuid;
    private Long telegramId;
    private Double subscriptionPrice;
}
