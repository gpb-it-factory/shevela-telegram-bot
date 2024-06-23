package gpb.itfactory.shevelatelegrambot.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
class CreateAccountCommandHandlerTest {

    private final CreateAccountCommandHandler createAccountCommandHandler;

    @Autowired
    CreateAccountCommandHandlerTest(CreateAccountCommandHandler createAccountCommandHandler) {
        this.createAccountCommandHandler = createAccountCommandHandler;
    }

    @Test
    void getCommand() {
        String actualResult = createAccountCommandHandler.getCommand();

        Assertions.assertThat(actualResult).isEqualTo("/createaccount");
    }
}