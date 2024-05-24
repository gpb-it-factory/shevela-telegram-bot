package gpb.itfactory.shevelatelegrambot.bot;


import gpb.itfactory.shevelatelegrambot.bot.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/* Класс бота взаимодействует с Telegtam API в режиме Long Polling,
 * путем наследования от класса org.telegram.telegrambots.bots.TelegramLongPollingBot */

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private static final String UNKNOWN_MESSAGE = "You have entered unknown message. Please check and enter valid command.";
    private final BotConfig botConfig;
    private final UpdateDispatcher dispatcher;

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String command = update.getMessage().getText();
        if(update.hasMessage() && update.getMessage().hasText() && command.startsWith("/")) {
            log.info("Received command: " + command + " From chat № " + chatId);
            sendMessage(dispatcher.doDispatch(update));
            return;
        }
        log.warn("Received unknown message: " + command);
        sendMessage(new SendMessage(String.valueOf(chatId), UNKNOWN_MESSAGE));
    }

    private void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
            log.info("Send message: " + sendMessage.getText() + " To chat № " + sendMessage.getChatId());
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
