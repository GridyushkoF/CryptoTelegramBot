package com.skillbox.cryptobot.repositories;

import com.skillbox.cryptobot.model.Subscriber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface SubscriberRepository extends CrudRepository<Subscriber, UUID> {
    Optional<Subscriber> findByTelegramId(Long telegramId);
    List<Subscriber> findAllBySubscriptionPriceGreaterThan(double price);
}
