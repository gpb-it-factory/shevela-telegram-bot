package gpb.itfactory.shevelatelegrambot.bot;


import gpb.itfactory.shevelatelegrambot.bot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/* Класс бота взаимодействует с Telegtam API в режиме Long Polling,
 * путем наследования от класса org.telegram.telegrambots.bots.TelegramLongPollingBot */

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final String UNKNOWN_MESSAGE = "You have entered unknown message. Please check and enter valid command.";
    private final BotConfig botConfig;
    private final UpdateDispatcher dispatcher;

    public TelegramBot(@Value("${bot.token}") String botToken, BotConfig botConfig, UpdateDispatcher dispatcher) {
        super(botToken);
        this.botConfig = botConfig;
        this.dispatcher = dispatcher;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            execute(buildResponseMessage(update));
            log.info("Send message: " + buildResponseMessage(update).getText()
                    + " To chat № " + buildResponseMessage(update).getChatId());
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public SendMessage buildResponseMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        String command = update.getMessage().getText();
        log.info("Received command: " + command + " From chat № " + chatId);
        if(update.hasMessage() && update.getMessage().hasText() && command.startsWith("/")) {
            return dispatcher.doDispatch(update);
        }
        log.warn("Received unknown message: " + command);
        return new SendMessage(String.valueOf(chatId), UNKNOWN_MESSAGE);
    }
}
