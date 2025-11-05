package com.lucky.bot.telegram.response.handler.command;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record CommandData(Message message, User user) {
}
