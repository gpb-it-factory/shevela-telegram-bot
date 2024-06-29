package gpb.itfactory.shevelatelegrambot.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import gpb.itfactory.shevelatelegrambot.bot.UpdateDispatcher;
import gpb.itfactory.shevelatelegrambot.bot.handler.CommandHandler;
import gpb.itfactory.shevelatelegrambot.bot.handler.TransferCommandHandler;
import gpb.itfactory.shevelatelegrambot.bot.handler.UnknownCommandHandler;
import gpb.itfactory.shevelatelegrambot.integration.mocks.UserMock;
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
public class UpdateDispatcherIsRegisterCommandIT {

    private final List<CommandHandler> commandHandlers;
    private final UnknownCommandHandler unknownCommandHandler;
    private final TransferCommandHandler transferCommandHandler;

    private Chat chat;
    private Update update;
    private WireMockServer wireMockServer;

    @Autowired
    public UpdateDispatcherIsRegisterCommandIT(List<CommandHandler> commandHandlers,
                                                   UnknownCommandHandler unknownCommandHandler,
                                                   TransferCommandHandler transferCommandHandler) {
        this.commandHandlers = commandHandlers;
        this.unknownCommandHandler = unknownCommandHandler;
        this.transferCommandHandler = transferCommandHandler;
    }

    @BeforeEach
    void setUp(){
        chat = new Chat(123L, "test");
        chat.setUserName("test");
        Message message = new Message();
        message.setChat(chat);
        message.setText("/isregister");
        User user = new User();
        user.setId(123456L);
        message.setFrom(user);
        update = new Update();
        update.setMessage(message);
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
    }


    @Test
    void getUserByTelegramIdIfUserIsPresent() {
        UserMock.setupGetUserByTelegramIdResponseIfUserIsPresent(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo( "User is registered in the MiniBank");
    }

    @Test
    void getUserByTelegramIdIfUserIsNotPresent() {
        UserMock.setupGetUserByTelegramIdResponseIfUserIsNotPresent(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo("User is not registered in the MiniBank");
    }

    @Test
    void getUserByTelegramIdIfServerError() {
        UserMock.setupGetUserByTelegramIdResponseFail(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when verifying user registration >>");
    }

    @Test
    void getUserByTelegramIdIfNoConnection() {
        UserMock.setupGetUserByTelegramIdResponseIfNoConnection(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Middle service unknown or client error");
    }

    @Test
    void getUserByTelegramIdIfBackendServerNoConnection() {
        UserMock.setupGetUserByTelegramIdResponseIfBackendServerNoConnection(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText())
                .isEqualTo("Error << Backend server unknown " +
                        "or client error when registration verification >>");
    }

    @AfterEach
    void cleanUp(){
        wireMockServer.stop();
    }

}
