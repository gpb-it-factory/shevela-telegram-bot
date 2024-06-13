package gpb.itfactory.shevelatelegrambot.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PingCommandHandlerTest {
    private final PingCommandHandler pingCommandHandler;
    Chat chat;
    Message message;
    Update update;

    @Autowired
    PingCommandHandlerTest(PingCommandHandler pingCommandHandler) {
        this.pingCommandHandler = pingCommandHandler;
    }

    @BeforeEach
    void init () {
        chat = new Chat(123L, "test");
        message = new Message();
        update = new Update();
        message.setChat(chat);
        update.setMessage(message);
    }

    @Test
    void handle() {
        SendMessage expectedResult = new SendMessage(String.valueOf(123L), "pong");

        SendMessage actualResult = pingCommandHandler.handle(update);

        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void getCommand() {
        String actualResult = pingCommandHandler.getCommand();

        Assertions.assertThat(actualResult).isEqualTo("/ping");
    }
}