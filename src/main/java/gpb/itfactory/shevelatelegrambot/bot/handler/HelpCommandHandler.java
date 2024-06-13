package gpb.itfactory.shevelatelegrambot.bot.handler;

import gpb.itfactory.shevelatelegrambot.bot.util.HelpCommandAnswer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/* Обработчик команды /help */

@Slf4j
@Component
public class HelpCommandHandler implements CommandHandler{
    private static final String COMMAND = "/help";

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.getMessage().getChatId();
        log.info("Processed command: " + COMMAND);
        return new SendMessage(String.valueOf(chatId), HelpCommandAnswer.answer());
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }
}
