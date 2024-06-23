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
class RegisterCommandHandlerTest {
    private final RegisterCommandHandler registerCommandHandler;

    @Autowired
    RegisterCommandHandlerTest(RegisterCommandHandler registerCommandHandler) {
        this.registerCommandHandler = registerCommandHandler;
    }

    @Test
    void getCommand() {
        String actualResult = registerCommandHandler.getCommand();

        Assertions.assertThat(actualResult).isEqualTo("/register");
    }

}