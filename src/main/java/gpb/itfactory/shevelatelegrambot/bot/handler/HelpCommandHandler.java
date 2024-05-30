package gpb.itfactory.shevelatelegrambot.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/* Обработчик команды /help */

@Slf4j
@Component
public class HelpCommandHandler implements CommandHandler{
    private static final String COMMAND = "/help";
    private static final String ANSWER = """
            /start - начало работы с ботом, первичная инструкция пользователю
            /help - справка
            /ping - тестовая команда, ответ "pong"
            """;

    @Override
    public SendMessage handle(long chatId) {
        log.info("Processed command: " + COMMAND);
        return new SendMessage(String.valueOf(chatId), ANSWER);
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }
}
