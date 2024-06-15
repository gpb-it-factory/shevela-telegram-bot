package gpb.itfactory.shevelatelegrambot.bot.handler;

import gpb.itfactory.shevelatelegrambot.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/* Обработчик команды /currentbalance */

@Slf4j
@Component
public class CurrentBalanceCommandHandler implements CommandHandler {

    private static final String COMMAND = "/currentbalance";
    private final AccountService accountService;

    public CurrentBalanceCommandHandler(AccountService accountService) {
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
        Long tgUserId = update.getMessage().getFrom().getId();
        return accountService.getUserAccountsV2(tgUserId);
    }
}
