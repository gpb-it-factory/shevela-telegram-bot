package gpb.itfactory.shevelatelegrambot.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
@Component
public class UnknownCommandHandler implements CommandHandler{
    private static final String COMMAND = "";
    private static final String ANSWER = "You have entered unknown command. Please check and enter valid command";

    @Override
    public SendMessage handle(long chatId) {
        log.warn("Unknown command has been processed.");
        return new SendMessage(String.valueOf(chatId), ANSWER);
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }
}
