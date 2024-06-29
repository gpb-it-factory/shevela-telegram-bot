package gpb.itfactory.shevelatelegrambot.bot.config;

import gpb.itfactory.shevelatelegrambot.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/* Класс регистрации бота в TelegramBotsApi */

@Slf4j
@Component
@RequiredArgsConstructor
public class BotInitializer {
    private final TelegramBot telegramBot;

    @EventListener({ContextRefreshedEvent.class})
    private void init() throws RuntimeException {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
