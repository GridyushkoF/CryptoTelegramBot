package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.client.BinanceClient;
import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repositories.SubscriberRepository;
import com.skillbox.cryptobot.utils.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j

public class CryptoCurrencyService {
    private final AtomicReference<Double> price = new AtomicReference<>();
    private final BinanceClient client;


    @Autowired
    public CryptoCurrencyService(BinanceClient client) {
        this.client = client;
    }

    public double getBitcoinPrice()  {
        try {
            if (price.get() == null) {
                price.set(client.getBitcoinPrice());
            }
        }catch (Exception e) {
            log.error("Ошибка BinanceClient ",e);
        }
        return price.get();
    }

}
