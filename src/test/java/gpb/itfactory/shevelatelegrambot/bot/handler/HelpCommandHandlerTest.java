package gpb.itfactory.shevelatelegrambot.bot.handler;

import gpb.itfactory.shevelatelegrambot.bot.util.HelpCommandAnswer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
class HelpCommandHandlerTest {

    private final HelpCommandHandler helpCommandHandler;
    Chat chat;
    Message message;
    Update update;

    @Autowired
    HelpCommandHandlerTest(HelpCommandHandler helpCommandHandler) {
        this.helpCommandHandler = helpCommandHandler;
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
        SendMessage expectedResult = new SendMessage(String.valueOf(123L), HelpCommandAnswer.answer());

        SendMessage actualResult = helpCommandHandler.handle(update);

        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void getCommand() {
        String actualResult = helpCommandHandler.getCommand();

        Assertions.assertThat(actualResult).isEqualTo("/help");
    }
}