package gpb.itfactory.shevelatelegrambot.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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