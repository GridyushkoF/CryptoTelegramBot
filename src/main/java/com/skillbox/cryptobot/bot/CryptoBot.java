package com.skillbox.cryptobot.bot;

import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repositories.SubscriberRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class CryptoBot extends TelegramLongPollingCommandBot {
    private final String botUsername;
    private final MessageSender messageSender = new MessageSender();
    private final SubscriberRepository subscriberRepository;
    private final CryptoCurrencyService cryptoCurrencyService;
    @Value("${time.TEN_MINUTES_IN_MILLIS}")
    private int TEN_MINUTES_IN_MILLIS;
    private long timeOfLastScheduledMessage = 0L;

    @Autowired
    public CryptoBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            List<IBotCommand> commandList,
            SubscriberRepository subscriberRepository, CryptoCurrencyService cryptoCurrencyService) {
        super(botToken);
        this.botUsername = botUsername;
        this.subscriberRepository = subscriberRepository;
        this.cryptoCurrencyService = cryptoCurrencyService;
        commandList.forEach(this::register);
        List<BotCommand> botCommandList = new ArrayList<>();
        commandList.forEach(iBotCommand -> {
            botCommandList.add(new BotCommand(iBotCommand.getCommandIdentifier(),iBotCommand.getDescription()));
        });
        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(),null));
        }catch (Exception e) {
            log.error("Ошибка в конструкторе",e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
    }
    @Scheduled(fixedRateString = "${time.TWO_MINUTES_IN_MILLIS}")
    public void checkCurrentPrice(){
        double currentBitcoinPrice = cryptoCurrencyService.getBitcoinPrice();
        List<Subscriber> subscribers = subscriberRepository.findAllBySubscriptionPriceGreaterThan(currentBitcoinPrice);
        if (!subscribers.isEmpty() && System.currentTimeMillis() - timeOfLastScheduledMessage >= TEN_MINUTES_IN_MILLIS) {
            timeOfLastScheduledMessage = System.currentTimeMillis();
            subscribers.forEach(subscriber -> {
                messageSender.sendNewMessage("Пора покупать: стоимость bitcoin'а уже " + currentBitcoinPrice,subscriber.getTelegramId(),this);
            });
        }
    }
}
