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
public class UpdateDispatcherCreateAccountCommandIT {

    private final List<CommandHandler> commandHandlers;
    private final UnknownCommandHandler unknownCommandHandler;

    private Chat chat;
    private Update update;
    private WireMockServer wireMockServer;

    @Autowired
    public UpdateDispatcherCreateAccountCommandIT(List<CommandHandler> commandHandlers,
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
        message.setText("/createaccount");
        User user = new User();
        user.setId(123456L);
        message.setFrom(user);
        update = new Update();
        update.setMessage(message);
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
    }

    @Test
    void createUserAccountSuccess() {
        AccountMock.setupCreateUserAccountResponseSuccess(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo("Account has been successfully created");
    }

    @Test
    void createUserAccountIfAccountIsPresent() {
        AccountMock.setupCreateUserAccountsV2ResponseIfAccountIsPresent(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Account is already open");
    }

    @Test
    void createUserAccountIfUserIsNotPresent() {
        AccountMock.setupCreateUserAccountsV2ResponseIfUserIsNotPresent(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo("User is not registered in the MiniBank");
    }

    @Test
    void createUserAccountFail() {
        AccountMock.setupCreateUserAccountResponseFail(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when create account >>");
    }

    @Test
    void createUserAccountNoConnection() {
        AccountMock.setupCreateUserAccountResponseNoConnection(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Middle service unknown or connection error");
    }


    @Test
    void createUserAccountIfBackendServerNoConnection() {
        AccountMock.setupCreateUserAccountResponseIfBackendServerNoConnection(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or connection error when create account >>");
    }

    @Test
    void createUserAccountIfGetUserRequestServerError() {
        AccountMock.setupCreateUserAccountResponseIfGetUserByTelegramIdResponseFail(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when verifying user registration >>");
    }

    @Test
    void createUserAccountIfGetUserRequestNoConnection() {
        AccountMock.setupCreateUserAccountResponseIfGetUserByTelegramIdRequestNoConnection(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or connection error when registration verification >>");
    }

    @Test
    void createUserAccountIfGetUserAccountsRequestServerError() {
        AccountMock.setupCreateUserAccountResponseIfGetAccountsResponseServerError(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when account verification >>");
    }

    @Test
    void createUserAccountIfGetUserAccountsRequestNoConnection() {
        AccountMock.setupCreateUserAccountResponseIfGetAccountsResponseNoConnection(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or connection error when account verification >>");
    }

    @AfterEach
    void cleanUp(){
        wireMockServer.stop();
    }

}
