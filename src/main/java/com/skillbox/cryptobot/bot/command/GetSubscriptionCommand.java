package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repositories.SubscriberRepository;
import com.skillbox.cryptobot.utils.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@Slf4j
public class GetSubscriptionCommand implements IBotCommand {
    private final MessageSender messageSender = new MessageSender();
    private final SubscriberRepository subscriberRepository;
    @Autowired
    public GetSubscriptionCommand(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String newMessageText = "Активные подписки отсутствуют";
        Optional<Subscriber> subscriberOptional = subscriberRepository.findByTelegramId(message.getChatId());
        if (subscriberOptional.isPresent()) {
            Subscriber s = subscriberOptional.get();
            if (s.getSubscriptionPrice() != null) {
                newMessageText = MessageFormat.format("Вы подписаны на стоимость биткоина {0} USD",s.getSubscriptionPrice());
            }
        }
        messageSender.sendNewMessage(newMessageText,message.getChatId(),absSender);
    }
}