package gpb.itfactory.shevelatelegrambot.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import gpb.itfactory.shevelatelegrambot.bot.UpdateDispatcher;
import gpb.itfactory.shevelatelegrambot.bot.handler.CommandHandler;
import gpb.itfactory.shevelatelegrambot.bot.handler.UnknownCommandHandler;
import gpb.itfactory.shevelatelegrambot.integration.mocks.AccountMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@SpringBootTest
public class UpdateDispatcherCurrentBalanceCommandIT {

    private final List<CommandHandler> commandHandlers;
    private final UnknownCommandHandler unknownCommandHandler;

    private Chat chat;
    private Update update;
    private WireMockServer wireMockServer;

    @Autowired
    public UpdateDispatcherCurrentBalanceCommandIT(List<CommandHandler> commandHandlers,
                                                   UnknownCommandHandler unknownCommandHandler) {
        this.commandHandlers = commandHandlers;
        this.unknownCommandHandler = unknownCommandHandler;
    }

    @BeforeEach
    void setUp(){
        Chat chat = new Chat(123L, "test");
        chat.setUserName("test");
        Message message = new Message();
        message.setChat(chat);
        message.setText("/currentbalance");
        User user = new User();
        user.setId(123456L);
        message.setFrom(user);
        update = new Update();
        update.setMessage(message);
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
    }

    @Test
    void getUserAccountsSuccess() {
        AccountMock.setupGetUserAccountsResponseSuccess(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).startsWith("User has open account");
    }

    @Test
    void getUserAccountsIfResponseNoAccounts() {
        AccountMock.setupGetUserAccountsResponseIfNoAccount(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << User does not have account in the MiniBank >>");
    }

    @Test
    void getUserAccountsIfResponseUserNotPresent() {
        AccountMock.setupGetUserAccountsResponseIfUserNotPresent(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo("User is not registered in the MiniBank");
    }

    @Test
    void getUserAccountsIfGetUserAccountsServerError() {
        AccountMock.setupGetUserAccountsResponseIfServerError(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when account verification >>");
    }

    @Test
    void getUserAccountsIfNoConnection() {
        AccountMock.setupGetUserAccountsResponseIfNoConnection(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Middle service unknown or connection error");
    }

    @Test
    void getUserAccountIfGetUserAccountsRequestNoConnection() {
        AccountMock.setupGetUserAccountResponseIfGetAccountsResponseNoConnection(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or connection error when account verification >>");
    }

    @Test
    void getUserAccountIfGetUserRequestServerError() {
        AccountMock.setupGetUserAccountResponseIfGetUserByTelegramIdResponseFail(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when verifying user registration >>");
    }

    @Test
    void getUserAccountIfGetUserRequestNoConnection() {
        AccountMock.setupGetUserAccountResponseIfGetUserByTelegramIdRequestNoConnection(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or connection error when registration verification >>");
    }

    @AfterEach
    void cleanUp(){
        wireMockServer.stop();
    }

}
