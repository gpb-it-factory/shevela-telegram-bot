package gpb.itfactory.shevelatelegrambot.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
class IsRegisterCommandHandlerTest {

    private final IsRegisterCommandHandler isRegisterCommandHandler;

    @Autowired
    IsRegisterCommandHandlerTest(IsRegisterCommandHandler isRegisterCommandHandler) {
        this.isRegisterCommandHandler = isRegisterCommandHandler;
    }

    @Test
    void getCommand() {
        String actualResult = isRegisterCommandHandler.getCommand();

        Assertions.assertThat(actualResult).isEqualTo("/isregister");
    }
}