package gpb.itfactory.shevelatelegrambot.bot.handler;

import gpb.itfactory.shevelatelegrambot.dto.CreateAccountDto;
import gpb.itfactory.shevelatelegrambot.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/* Обработчик команды /createaccount */

@Slf4j
@Component
public class CreateAccountCommandHandler implements CommandHandler{

    private static final String COMMAND = "/createaccount";
    private final AccountService accountService;

    public CreateAccountCommandHandler(AccountService accountService) {
        this.accountService = accountService;
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
        String accountName = "Акционный";
        Long tgUserId = update.getMessage().getFrom().getId();
        CreateAccountDto createAccountDto = CreateAccountDto.builder().accountName(accountName).build();
        return accountService.createUserAccountV2(tgUserId, createAccountDto);
    }
}
