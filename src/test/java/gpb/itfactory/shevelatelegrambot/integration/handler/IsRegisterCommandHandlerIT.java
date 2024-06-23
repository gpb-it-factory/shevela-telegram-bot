package gpb.itfactory.shevelatelegrambot.integration.handler;

import com.github.tomakehurst.wiremock.WireMockServer;
import gpb.itfactory.shevelatelegrambot.bot.handler.IsRegisterCommandHandler;
import gpb.itfactory.shevelatelegrambot.integration.mocks.UserMock;
import gpb.itfactory.shevelatelegrambot.integration.WireMockConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ContextConfiguration(classes = { WireMockConfig.class })
class IsRegisterCommandHandlerIT {

    private final WireMockServer wireMockServer;
    private final IsRegisterCommandHandler isRegisterCommandHandler;
    private Chat chat;
    private Update update;

    @Autowired
    IsRegisterCommandHandlerIT(WireMockServer wireMockServer, IsRegisterCommandHandler isRegisterCommandHandler) {
        this.wireMockServer = wireMockServer;
        this.isRegisterCommandHandler = isRegisterCommandHandler;
    }

    @BeforeEach
    void setUp(){
        chat = new Chat(123L, "test");
        chat.setUserName("test");
        Message message = new Message();
        message.setChat(chat);
        message.setText("/register");
        User user = new User();
        user.setId(123456L);
        message.setFrom(user);
        update = new Update();
        update.setMessage(message);
    }

    @Test
    void getUserByTelegramIdIfUserIsPresent() {
        UserMock.setupGetUserByTelegramIdResponseIfUserIsPresent(wireMockServer);

        SendMessage actualResult = isRegisterCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo( "User is registered in the MiniBank");
    }

    @Test
    void getUserByTelegramIdIfUserIsNotPresent() {
        UserMock.setupGetUserByTelegramIdResponseIfUserIsNotPresent(wireMockServer);

        SendMessage actualResult = isRegisterCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo("User is not registered in the MiniBank");
    }

    @Test
    void getUserByTelegramIdIfServerError() {
        UserMock.setupGetUserByTelegramIdResponseFail(wireMockServer);

        SendMessage actualResult = isRegisterCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when verifying user registration >>");
    }

    @Test
    void getUserByTelegramIdIfNoConnection() {
        UserMock.setupGetUserByTelegramIdResponseIfNoConnection(wireMockServer);

        SendMessage actualResult = isRegisterCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Middle service unknown or connection error");
    }

    @Test
    void getUserByTelegramIdIfBackendServerNoConnection() {
        UserMock.setupGetUserByTelegramIdResponseIfBackendServerNoConnection(wireMockServer);

        SendMessage actualResult = isRegisterCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText())
                .isEqualTo("Error << Backend server unknown " +
                        "or connection error when registration verification >>");
    }

}
