package gpb.itfactory.shevelatelegrambot.bot.handler;

import gpb.itfactory.shevelatelegrambot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/* Обработчик команды /ping */

@Slf4j
@Component
public class IsRegisterCommandHandler implements CommandHandler{

    @Autowired
    private UserService userService;

    private static final String COMMAND = "/isregister";
    private static final String ANSWER = "pong";

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.getMessage().getChatId();
        log.info("Processed command: " + COMMAND);
        return new SendMessage(String.valueOf(chatId), getAnswer(update));
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }

    private String getAnswer(Update update) {
        String username = update.getMessage().getChat().getUserName();
        return userService.findUserByUsername(username);
    }
}
