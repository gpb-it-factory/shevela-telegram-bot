package gpb.itfactory.shevelatelegrambot.integration.handler;

import com.github.tomakehurst.wiremock.WireMockServer;
import gpb.itfactory.shevelatelegrambot.bot.handler.RegisterCommandHandler;
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
class RegisterCommandHandlerIT {

    private final WireMockServer wireMockServer;
    private final RegisterCommandHandler registerCommandHandler;
    private Chat chat;
    private Update update;

    @Autowired
    RegisterCommandHandlerIT(WireMockServer wireMockServer, RegisterCommandHandler registerCommandHandler) {
        this.wireMockServer = wireMockServer;
        this.registerCommandHandler = registerCommandHandler;
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
    void createUserResponseSuccess() {
        UserMock.setupCreateUserResponseSuccess(wireMockServer);

        SendMessage actualResult = registerCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "User %s has been successfully registered in the MiniBank".formatted(chat.getUserName()));
    }

    @Test
    void createUserIfUserIsPresent(){
        UserMock.setupCreateUserResponseIfUserIsPresent(wireMockServer);

        SendMessage actualResult = registerCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo("User already exists in the MiniBank");
    }

    @Test
    void createUserResponseFail() {
        UserMock.setupCreateUserResponseFail(wireMockServer);

        SendMessage actualResult = registerCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when create User >>");
    }

    @Test
    void createUserResponseIfNoConnection() {
        UserMock.setupCreateUserResponseIfNoConnection(wireMockServer);

        SendMessage actualResult = registerCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Middle service unknown or connection error");
    }

    @Test
    void createUserResponseIfBackendServerNoConnection() {
        UserMock.setupCreateUserResponseIfBackendServerNoConnection(wireMockServer);

        SendMessage actualResult = registerCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or connection error when create user >>");
    }
}
