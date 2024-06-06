package gpb.itfactory.shevelatelegrambot.bot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TelegramBotTest {

    @Mock
    private UpdateDispatcher dispatcher;
    Chat chat;
    Message message;
    Update update;

    @InjectMocks
    private TelegramBot telegramBot;

    @BeforeEach
    void init () {
        chat = new Chat(123L, "test");
        message = new Message();
        update = new Update();
        message.setChat(chat);
    }

    @Test
    void buildResponseMessageIfReceiveCommand() {
        message.setText("/test");
        update.setMessage(message);
        SendMessage expectedResult = new SendMessage(String.valueOf(123L), "test");
        Mockito.doReturn(expectedResult).when(dispatcher).doDispatch(update);

        SendMessage actualResult = telegramBot.buildResponseMessage(update);

        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void buildResponseMessageIfReceiveNotCommand() {
        message.setText("test");
        update.setMessage(message);
        SendMessage expectedResult = new SendMessage(String.valueOf(123L), "You have entered unknown message. Please check and enter valid command.");

        SendMessage actualResult = telegramBot.buildResponseMessage(update);

        Assertions.assertThat(actualResult).isEqualTo(expectedResult);

    }
}