package gpb.itfactory.shevelatelegrambot.integration.handler;

import com.github.tomakehurst.wiremock.WireMockServer;
import gpb.itfactory.shevelatelegrambot.bot.handler.CurrentBalanceCommandHandler;
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
public class CurrentBalanceCommandHandlerIT {

    private final WireMockServer wireMockServer;
    private final CurrentBalanceCommandHandler currentBalanceCommandHandler;
    private Update update;

    @Autowired
    public CurrentBalanceCommandHandlerIT(WireMockServer wireMockServer,
                                          CurrentBalanceCommandHandler currentBalanceCommandHandler) {
        this.wireMockServer = wireMockServer;
        this.currentBalanceCommandHandler = currentBalanceCommandHandler;
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
    void getUserAccountsSuccess() {
        AccountMock.setupGetUserAccountsResponseSuccess(wireMockServer);

        SendMessage actualResult = currentBalanceCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).startsWith("User has open account");
    }

    @Test
    void getUserAccountsIfResponseNoAccounts() {
        AccountMock.setupGetUserAccountsResponseIfNoAccount(wireMockServer);

        SendMessage actualResult = currentBalanceCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << User does not have account in the MiniBank >>");
    }

    @Test
    void getUserAccountsIfResponseUserNotPresent() {
        AccountMock.setupGetUserAccountsResponseIfUserNotPresent(wireMockServer);

        SendMessage actualResult = currentBalanceCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo("User is not registered in the MiniBank");
    }

    @Test
    void getUserAccountsIfGetUserAccountsServerError() {
        AccountMock.setupGetUserAccountsResponseIfServerError(wireMockServer);

        SendMessage actualResult = currentBalanceCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when account verification >>");
    }

    @Test
    void getUserAccountsIfNoConnection() {
        AccountMock.setupGetUserAccountsResponseIfNoConnection(wireMockServer);

        SendMessage actualResult = currentBalanceCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Middle service unknown or connection error");
    }

    @Test
    void getUserAccountIfGetUserAccountsRequestNoConnection() {
        AccountMock.setupGetUserAccountResponseIfGetAccountsResponseNoConnection(wireMockServer);

        SendMessage actualResult = currentBalanceCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or connection error when account verification >>");
    }

    @Test
    void getUserAccountIfGetUserRequestServerError() {
        AccountMock.setupGetUserAccountResponseIfGetUserByTelegramIdResponseFail(wireMockServer);

        SendMessage actualResult = currentBalanceCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when verifying user registration >>");
    }

    @Test
    void getUserAccountIfGetUserRequestNoConnection() {
        AccountMock.setupGetUserAccountResponseIfGetUserByTelegramIdRequestNoConnection(wireMockServer);

        SendMessage actualResult = currentBalanceCommandHandler.handle(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or connection error when registration verification >>");
    }

}
