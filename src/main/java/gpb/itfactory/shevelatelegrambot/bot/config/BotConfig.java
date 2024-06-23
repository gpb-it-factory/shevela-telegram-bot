package gpb.itfactory.shevelatelegrambot.bot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/* Конфигурационный класс для хранения botUsername & botToken */
@ConditionalOnProperty(value="botConfig", havingValue="production")
@Configuration
@ConfigurationProperties(prefix = "bot")
@Data
public class BotConfig {

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;
}



