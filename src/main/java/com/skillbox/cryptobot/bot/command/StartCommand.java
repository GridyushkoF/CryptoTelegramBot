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

import java.util.Optional;


/**
 * Обработка команды начала работы с ботом
 */
@Service
@Slf4j
public class StartCommand implements IBotCommand {

    private final SubscriberRepository subscriberRepository;
    private final MessageSender messageSender = new MessageSender();
    @Autowired
    public StartCommand(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запускает бота";
    }


    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        messageSender.sendNewMessage("""
                Привет! Данный бот помогает отслеживать стоимость биткоина.
                Поддерживаемые команды:
                 /get_price - получить стоимость биткоина
                """, message.getChatId(),absSender);
        Optional<Subscriber> subscriberOptional = subscriberRepository.findByTelegramId(message.getChatId());
        if (subscriberOptional.isEmpty()) {
            subscriberRepository.save(new Subscriber(
                    message.getChatId()));
        } else {
            messageSender.sendNewMessage("Вы уже зарегистрировались!",message.getChatId(),absSender);
        }
    }
}