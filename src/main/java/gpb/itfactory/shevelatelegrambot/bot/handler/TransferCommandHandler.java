package gpb.itfactory.shevelatelegrambot.bot.handler;


import gpb.itfactory.shevelatelegrambot.dto.TelegramTransferDto;
import gpb.itfactory.shevelatelegrambot.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class TransferCommandHandler implements CommandHandler{

    private static final String COMMAND = "/transfer";

    private final TransferService transferService;

    public TransferCommandHandler(TransferService transferService) {
        this.transferService = transferService;
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
        log.info("Build request on command: " + COMMAND);
        String regex = "/transfer\s[a-zA-Z0-9_]+\s([0-9]*[.])?[0-9]+";
        String command = update.getMessage().getText();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);
        if (matcher.matches()) {
            String[] s = command.split(" ");
            String recipientTelegramUser = s[1];
            float amount = Float.parseFloat(s[2]);
            if(update.getMessage().getChat().getUserName().equals(recipientTelegramUser)) {
                return "Invalid command format. Invalid field \"toTelegramUser\"";
            }
            TelegramTransferDto telegramTransferDto = getTelegramTransferDto(update, recipientTelegramUser, amount);
            return transferService.createTransferV2(telegramTransferDto);
        }
        return "Invalid command format. Valid command format is \"/transfer toTelegramUser amount\"";
    }

    private static TelegramTransferDto getTelegramTransferDto(Update update, String recipientTelegramUser, float amount) {
        return TelegramTransferDto.builder()
                .to(recipientTelegramUser)
                .from(update.getMessage().getChat().getUserName())
                .fromId(update.getMessage().getFrom().getId())
                .ammount(amount).build();
    }
}
