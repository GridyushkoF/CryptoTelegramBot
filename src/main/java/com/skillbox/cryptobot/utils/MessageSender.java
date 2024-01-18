package com.skillbox.cryptobot.utils;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public class MessageSender {
    public void sendNewMessage(String text, Long chatId, AbsSender sender) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId);
        try {
            answer.setText(text);
            sender.execute(answer);
        } catch (Exception e) {
            log.error("Ошибка возникла при отправке сообщения", e);
        }
    }
    public SendMessage prepareMessageToSend(String text, Long chatId) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId);
        try {
            answer.setText(text);
        } catch (Exception e) {
            log.error("Ошибка возникла при отправке сообщения", e);
        }
        return answer;
    }
}
