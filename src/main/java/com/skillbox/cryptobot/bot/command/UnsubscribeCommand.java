package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repositories.SubscriberRepository;
import com.skillbox.cryptobot.utils.MessageSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

/**
 * Обработка команды отмены подписки на курс валюты
 */
@Service
@Slf4j
public class UnsubscribeCommand implements IBotCommand {
    private final SubscriberRepository subscriberRepository;
    private final MessageSender messageSender = new MessageSender();
    @Autowired
    public UnsubscribeCommand(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        Optional<Subscriber> subscriberOptional = subscriberRepository.findByTelegramId(message.getChatId());
        subscriberOptional.ifPresent(subscriber -> {
            subscriber.setSubscriptionPrice(null);
            subscriberRepository.save(subscriber);
        });
        messageSender.sendNewMessage("Подписка отменена",message.getChatId(),absSender);
    }
}