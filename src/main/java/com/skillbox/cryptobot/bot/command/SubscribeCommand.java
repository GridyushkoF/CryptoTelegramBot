package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.repositories.SubscriberRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
public class SubscribeCommand implements IBotCommand {
    private final SubscriberRepository subscriberRepository;
    private final CryptoCurrencyService currencyService;
    private final MessageSender messageSender = new MessageSender();
    private static final Pattern SUBSCRIBE_PATTERN = Pattern.compile("^/subscribe\\s+(\\d+)$");
    @Autowired
    public SubscribeCommand(SubscriberRepository subscriberRepository, CryptoCurrencyService currencyService) {
        this.subscriberRepository = subscriberRepository;
        this.currencyService = currencyService;
    }

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String userInput = message.getText();
        Matcher subscriptionMatcher = SUBSCRIBE_PATTERN.matcher(userInput);
        if(subscriptionMatcher.matches()) {
            String subscriptionPriceString = subscriptionMatcher.group(1);
            Double subscriptionPrice = Double.parseDouble(subscriptionPriceString);
            subscriberRepository
                    .findByTelegramId(message.getChatId())
                    .ifPresent(subscriber -> {
                        subscriber.setSubscriptionPrice(subscriptionPrice);
                        subscriberRepository.save(subscriber);
                    });
            GetPriceCommand command = new GetPriceCommand(currencyService);
            command.processMessage(absSender,message,arguments);
            messageSender.sendNewMessage(
                    "Новая подписка создана на стоимость " + userInput + " USD",
                    message.getChatId(),
                    absSender
            );
        } else {
            messageSender.sendNewMessage(
                    "Вы некорректно ввели команду, пожалуйста, попробуйте ещё раз!",
                    message.getChatId(),
                    absSender
            );
        }
    }
}