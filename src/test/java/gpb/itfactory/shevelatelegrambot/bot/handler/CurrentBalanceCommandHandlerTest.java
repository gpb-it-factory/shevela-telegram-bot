package gpb.itfactory.shevelatelegrambot.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CurrentBalanceCommandHandlerTest {

    private final CurrentBalanceCommandHandler currentBalanceCommandHandler;

    @Autowired
    CurrentBalanceCommandHandlerTest(CurrentBalanceCommandHandler currentBalanceCommandHandler) {
        this.currentBalanceCommandHandler = currentBalanceCommandHandler;
    }

    @Test
    void getCommand() {
        String actualResult = currentBalanceCommandHandler.getCommand();

        Assertions.assertThat(actualResult).isEqualTo("/currentbalance");
    }
}