package gpb.itfactory.shevelatelegrambot.integration.handler;

import com.github.tomakehurst.wiremock.WireMockServer;
import gpb.itfactory.shevelatelegrambot.bot.handler.IsRegisterCommandHandler;
import gpb.itfactory.shevelatelegrambot.integration.CreateUserMock;
import gpb.itfactory.shevelatelegrambot.integration.WireMockConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WireMockConfig.class })
public class IsRegisterCommandHandlerIT {

    private final WireMockServer wireMockServer;
    private final IsRegisterCommandHandler isRegisterCommandHandler;
    Chat chat;
    Message message;
    Update update;

    @Autowired
    IsRegisterCommandHandlerIT(WireMockServer wireMockServer, IsRegisterCommandHandler isRegisterCommandHandler) {
        this.wireMockServer = wireMockServer;
        this.isRegisterCommandHandler = isRegisterCommandHandler;
    }

    @BeforeEach
    void setUp(){
        chat = new Chat(123L, "test");
        chat.setUserName("test");
        message = new Message();
        message.setChat(chat);
        message.setText("/register");
        update = new Update();
        update.setMessage(message);
    }

    @Test
    void handleIfIsRegisterSuccess() throws IOException {
        CreateUserMock.setupFindUserByUsernameResponseSuccess(wireMockServer);

        SendMessage actualResult = isRegisterCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo( "User %s is registered".formatted(chat.getUserName()));
    }

    @Test
    void handleIfIsRegisterFail() throws IOException {
        CreateUserMock.setupFindUserByUsernameResponseFail(wireMockServer);

        SendMessage actualResult = isRegisterCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Error");
    }

}
