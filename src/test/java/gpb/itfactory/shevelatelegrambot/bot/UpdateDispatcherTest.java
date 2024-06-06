package gpb.itfactory.shevelatelegrambot.bot;

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
class UpdateDispatcherTest {

    private final UpdateDispatcher updateDispatcher;
    Chat chat;
    Message message;
    Update update;

    @Autowired
    UpdateDispatcherTest(UpdateDispatcher updateDispatcher) {
        this.updateDispatcher = updateDispatcher;
    }

    @BeforeEach
    void init () {
        chat = new Chat(123L, "test");
        message = new Message();
        update = new Update();
        message.setChat(chat);
    }

    @Test
    void doDispatchIfKnownCommand() {
        message.setText("/ping");
        update.setMessage(message);
        SendMessage expectedResult = new SendMessage(String.valueOf(123L), "pong");

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void doDispatchIfUnknownCommand() {
        message.setText("/hello");
        update.setMessage(message);
        SendMessage expectedResult = new SendMessage(String.valueOf(123L), "You have entered unknown command. Please check and enter valid command");

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }
}