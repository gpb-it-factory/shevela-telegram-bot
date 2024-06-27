package gpb.itfactory.shevelatelegrambot.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import gpb.itfactory.shevelatelegrambot.bot.UpdateDispatcher;
import gpb.itfactory.shevelatelegrambot.bot.handler.CommandHandler;
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
public class UpdateDispatcherRegisterCommandIT {

    private final List<CommandHandler> commandHandlers;
    private final UnknownCommandHandler unknownCommandHandler;

    private Chat chat;
    private Update update;

    private WireMockServer wireMockServer;

    @Autowired
    public UpdateDispatcherRegisterCommandIT(List<CommandHandler> commandHandlers,
                                             UnknownCommandHandler unknownCommandHandler) {
        this.commandHandlers = commandHandlers;
        this.unknownCommandHandler = unknownCommandHandler;
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
        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
    }

    @Test
    void createUserResponseSuccess() {
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);
        UserMock.setupCreateUserResponseSuccess(wireMockServer);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "User %s has been successfully registered in the MiniBank".formatted(chat.getUserName()));
    }

    @Test
    void createUserIfUserIsPresent(){
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);
        UserMock.setupCreateUserResponseIfUserIsPresent(wireMockServer);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo("User already exists in the MiniBank");
    }

    @Test
    void createUserResponseFail() {
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);
        UserMock.setupCreateUserResponseFail(wireMockServer);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when create User >>");
    }

    @Test
    void createUserResponseIfNoConnection() {
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);
        UserMock.setupCreateUserResponseIfNoConnection(wireMockServer);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Middle service unknown or connection error");
    }

    @Test
    void createUserResponseIfBackendServerNoConnection() {
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);
        UserMock.setupCreateUserResponseIfBackendServerNoConnection(wireMockServer);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or connection error when create user >>");
    }

    @AfterEach
    void cleanUp(){
        wireMockServer.stop();
    }

}
