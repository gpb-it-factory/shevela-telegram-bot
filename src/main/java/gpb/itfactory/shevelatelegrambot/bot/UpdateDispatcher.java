package gpb.itfactory.shevelatelegrambot.bot;


import gpb.itfactory.shevelatelegrambot.bot.handler.CommandHandler;
import gpb.itfactory.shevelatelegrambot.bot.handler.UnknownCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/* Класс представляет собой диспетчер команд обработчикам и ответов от них */

@Component
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final List<CommandHandler> commandHandlers;
    private final UnknownCommandHandler unknownCommandHandler;

    public SendMessage doDispatch(Update update) {
        String command = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        return getHandlerByCommand(command).handle(chatId);
    }

    private CommandHandler getHandlerByCommand(String command) {
        return commandHandlers.stream()
                .filter(commandHandler -> commandHandler.getCommand().equals(command))
                .findAny()
                .orElse(unknownCommandHandler);
    }
}
