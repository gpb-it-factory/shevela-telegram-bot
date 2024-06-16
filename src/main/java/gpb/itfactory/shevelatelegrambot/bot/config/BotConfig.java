package gpb.itfactory.shevelatelegrambot.bot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/* Конфигурационный класс для хранения botUsername & botToken */

@Component
@ConfigurationProperties(prefix = "bot")
@Data
public class BotConfig {

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;
}
