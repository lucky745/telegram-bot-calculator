package com.lucky.bot.telegram.response.handler.command;

import org.telegram.telegrambots.meta.api.objects.User;

public record CommandData(String command, User user) {
}
