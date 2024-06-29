package gpb.itfactory.shevelatelegrambot.integration;


import com.github.tomakehurst.wiremock.WireMockServer;
import gpb.itfactory.shevelatelegrambot.bot.UpdateDispatcher;
import gpb.itfactory.shevelatelegrambot.bot.handler.CommandHandler;
import gpb.itfactory.shevelatelegrambot.bot.handler.TransferCommandHandler;
import gpb.itfactory.shevelatelegrambot.bot.handler.UnknownCommandHandler;
import gpb.itfactory.shevelatelegrambot.integration.mocks.TransferMock;
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
public class UpdateDispatcherTransferCommandIT {

    private final List<CommandHandler> commandHandlers;
    private final UnknownCommandHandler unknownCommandHandler;
    private final TransferCommandHandler transferCommandHandler;

    private Chat chat;
    private Update update;
    private WireMockServer wireMockServer;

    @Autowired
    public UpdateDispatcherTransferCommandIT(List<CommandHandler> commandHandlers, UnknownCommandHandler unknownCommandHandler, TransferCommandHandler transferCommandHandler) {
        this.commandHandlers = commandHandlers;
        this.unknownCommandHandler = unknownCommandHandler;
        this.transferCommandHandler = transferCommandHandler;
    }

    @BeforeEach
    void setUp(){
        Chat chat = new Chat(123L, "test");
        chat.setUserName("sender");
        Message message = new Message();
        message.setChat(chat);
        message.setText("/transfer recipient 1000");
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
        TransferMock.setupCreateTransferResponseSuccess(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo("Transfer has been successfully completed");
    }

    @Test
    void createTransferIfBalanceLessAmount() {
        TransferMock.setupCreateTransferV2ResponseIfBalanceLessAmount(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Error << User sender does not have enough money in the account >>");
    }

    @Test
    void createTransferIfUserSenderIsNotPresent() {
        TransferMock.setupCreateTransferV2ResponseIfUserSenderIsNotPresent(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo("Error << User sender is not registered in the MiniBank >>");
    }

    @Test
    void createTransferIfUserRecipientIsNotPresent() {
        TransferMock.setupCreateTransferV2ResponseIfUserRecipientIsNotPresent(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo("Error << User recipient is not registered in the MiniBank >>");
    }


    @Test
    void createTransferIfUserSenderAccountIsNotPresent() {
        TransferMock.setupCreateTransferIfUserSenderAccountIsNotPresent(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Error << User sender does not have account in the MiniBank >>");
    }

    @Test
    void createTransferFail() {
        TransferMock.setupCreateTransferResponseFail(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when create transfer >>");
    }

    @Test
    void createTransferIfBackendServerNoConnection() {
        TransferMock.setupCreateTransferResponseIfBackendServerNoConnection(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or client error when create transfer >>");
    }

    @Test
    void createTransferIfAccountToTransferIsNotPresent() {
        TransferMock.setupCreateTransferResponseIfAccountToTransferIsNotPresent(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Account for the transfer was not found >>");
    }

    @Test
    void createTransferNoConnection() {
        TransferMock.setupCreateTransferResponseNoConnection(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).startsWith("Middle service unknown or client error");
    }

    @Test
    void createTransferIfGetUserRequestServerError() {
        TransferMock.setupCreateTransferResponseIfGetUserByTelegramIdResponseFail(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when verifying user registration >>");
    }

    @Test
    void createTransferIfGetUserRequestNoConnection() {
        TransferMock.setupCreateTransferResponseIfGetUserByTelegramIdRequestNoConnection(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or client error when users registration verification >>");
    }

    @Test
    void createTransferIfGetUserAccountsRequestServerError() {
        TransferMock.setupCreateTransferResponseIfGetAccountsResponseServerError(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Internal Backend server error when sender account verification >>");
    }

    @Test
    void createTransferIfGetUserAccountsRequestNoConnection() {
        TransferMock.setupCreateTransferResponseIfGetAccountsResponseNoConnection(wireMockServer);
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler, transferCommandHandler);

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).isEqualTo(
                "Error << Backend server unknown or client error when sender account verification >>");
    }

    @AfterEach
    void cleanUp(){
        wireMockServer.stop();
    }

}
