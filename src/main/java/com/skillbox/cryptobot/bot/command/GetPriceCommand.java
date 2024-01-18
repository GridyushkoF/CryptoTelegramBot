package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.MessageSender;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Обработка команды получения текущей стоимости валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class GetPriceCommand implements IBotCommand {

    private final CryptoCurrencyService service;
    private final MessageSender messageSender = new MessageSender();

    @Override
    public String getCommandIdentifier() {
        return "get_price";
    }

    @Override
    public String getDescription() {
        return "Возвращает цену биткоина в USD";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
            messageSender.sendNewMessage(
                    "Текущая цена биткоина " + TextUtil.toString(service.getBitcoinPrice()) + " USD",
                    message.getChatId(),
                    absSender
            );
    }
}