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
class UnknownCommandHandlerTest {

    private final UnknownCommandHandler unknownCommandHandler;
    Chat chat;
    Message message;
    Update update;
    private static final String ANSWER = "You have entered unknown command. Please check and enter valid command";

    @Autowired
    UnknownCommandHandlerTest(UnknownCommandHandler unknownCommandHandler) {
        this.unknownCommandHandler = unknownCommandHandler;
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
        SendMessage expectedResult = new SendMessage(String.valueOf(123L), ANSWER);

        SendMessage actualResult = unknownCommandHandler.handle(update);

        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void getCommand() {
        String actualResult = unknownCommandHandler.getCommand();

        Assertions.assertThat(actualResult).isEqualTo("");
    }
}