package gpb.itfactory.shevelatelegrambot.bot.handler;

import gpb.itfactory.shevelatelegrambot.dto.UserDto;
import gpb.itfactory.shevelatelegrambot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/* Обработчик команды /register */

@Slf4j
@Component
public class RegisterCommandHandler implements CommandHandler{

    private static final String COMMAND = "/register";
    private final UserService userService;

    public RegisterCommandHandler(UserService userService) {
        this.userService = userService;
    }

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
        UserDto userDto = UserDto.builder().username(username).build();
        return userService.createUser(userDto);
    }
}
