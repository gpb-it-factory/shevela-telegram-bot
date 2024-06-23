package gpb.itfactory.shevelatelegrambot.integration.handler;

import com.github.tomakehurst.wiremock.WireMockServer;
import gpb.itfactory.shevelatelegrambot.bot.handler.CreateAccountCommandHandler;
import gpb.itfactory.shevelatelegrambot.integration.WireMockConfig;
import gpb.itfactory.shevelatelegrambot.integration.mocks.AccountMock;
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
public class CreateAccountCommandHandlerIT {

    private final WireMockServer wireMockServer;
    private final CreateAccountCommandHandler createAccountCommandHandler;
    private Update update;

    @Autowired
    public CreateAccountCommandHandlerIT(WireMockServer wireMockServer,
                                         CreateAccountCommandHandler createAccountCommandHandler) {
        this.wireMockServer = wireMockServer;
        this.createAccountCommandHandler = createAccountCommandHandler;
    }

    @BeforeEach
    void setUp(){
        Chat chat = new Chat(123L, "test");
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
    void createUserAccountSuccess() {
        AccountMock.setupCreateUserAccountResponseSuccess(wireMockServer);

        SendMessage actualResult = createAccountCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo("Account has been successfully created");
    }

    @Test
    void createUserAccountIfAccountIsPresent() {
        AccountMock.setupCreateUserAccountsV2ResponseIfAccountIsPresent(wireMockServer);

        SendMessage actualResult = createAccountCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Account is already open");
    }

    @Test
    void createUserAccountIfUserIsNotPresent() {
        AccountMock.setupCreateUserAccountsV2ResponseIfUserIsNotPresent(wireMockServer);

        SendMessage actualResult = createAccountCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo("User is not registered in the MiniBank");
    }

    @Test
    void createUserAccountFail() {
        AccountMock.setupCreateUserAccountResponseFail(wireMockServer);

        SendMessage actualResult = createAccountCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when create account >>");
    }

    @Test
    void createUserAccountNoConnection() {
        AccountMock.setupCreateUserAccountResponseNoConnection(wireMockServer);

        SendMessage actualResult = createAccountCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Middle service unknown or connection error");
    }


    @Test
    void createUserAccountIfBackendServerNoConnection() {
        AccountMock.setupCreateUserAccountResponseIfBackendServerNoConnection(wireMockServer);

        SendMessage actualResult = createAccountCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or connection error when create account >>");
    }

    @Test
    void createUserAccountIfGetUserRequestServerError() {
        AccountMock.setupCreateUserAccountResponseIfGetUserByTelegramIdResponseFail(wireMockServer);

        SendMessage actualResult = createAccountCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when verifying user registration >>");
    }

    @Test
    void createUserAccountIfGetUserRequestNoConnection() {
        AccountMock.setupCreateUserAccountResponseIfGetUserByTelegramIdRequestNoConnection(wireMockServer);

        SendMessage actualResult = createAccountCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or connection error when registration verification >>");
    }

    @Test
    void createUserAccountIfGetUserAccountsRequestServerError() {
        AccountMock.setupCreateUserAccountResponseIfGetAccountsResponseServerError(wireMockServer);

        SendMessage actualResult = createAccountCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when account verification >>");
    }

    @Test
    void createUserAccountIfGetUserAccountsRequestNoConnection() {
        AccountMock.setupCreateUserAccountResponseIfGetAccountsResponseNoConnection(wireMockServer);

        SendMessage actualResult = createAccountCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or connection error when account verification >>");
    }

}
