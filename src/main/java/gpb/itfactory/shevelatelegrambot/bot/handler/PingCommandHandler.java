package gpb.itfactory.shevelatelegrambot.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/* Обработчик команды /ping */

@Slf4j
@Component
public class PingCommandHandler implements CommandHandler{

    private static final String COMMAND = "/ping";
    private static final String ANSWER = "pong";

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
