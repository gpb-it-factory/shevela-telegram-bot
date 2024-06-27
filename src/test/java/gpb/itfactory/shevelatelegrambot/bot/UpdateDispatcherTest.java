package gpb.itfactory.shevelatelegrambot.bot;

import gpb.itfactory.shevelatelegrambot.bot.handler.CommandHandler;
import gpb.itfactory.shevelatelegrambot.bot.handler.UnknownCommandHandler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@SpringBootTest
class UpdateDispatcherTest {

    private final List<CommandHandler> commandHandlers;
    private final UnknownCommandHandler unknownCommandHandler;

    Chat chat;
    Message message;
    Update update;

    @Autowired
    UpdateDispatcherTest(List<CommandHandler> commandHandlers,
                         UnknownCommandHandler unknownCommandHandler) {
        this.commandHandlers = commandHandlers;
        this.unknownCommandHandler = unknownCommandHandler;
    }

    @BeforeEach
    void init() {
        chat = new Chat(123L, "test");
        message = new Message();
        update = new Update();
        message.setChat(chat);

    }

    @Test
    void doDispatchIfPingCommand() {
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);
        message.setText("/ping");
        update.setMessage(message);
        SendMessage expectedResult = new SendMessage(String.valueOf(123L), "pong");

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void doDispatchIfStartCommand() {
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);
        message.setText("/start");
        update.setMessage(message);

        String expectedResult = "Вас приветствует Bank Assistant Bot!";

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).startsWith(expectedResult);
    }

    @Test
    void doDispatchIfHelpCommand() {
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);
        System.out.println(updateDispatcher);
        message.setText("/help");
        update.setMessage(message);

        String expectedResult = "/start - начало работы с ботом";

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult.getText()).startsWith(expectedResult);
    }


    @Test
    void doDispatchIfUnknownCommand() {
        UpdateDispatcher updateDispatcher = new UpdateDispatcher(commandHandlers, unknownCommandHandler);
        System.out.println(updateDispatcher);
        message.setText("/hello");
        update.setMessage(message);

        SendMessage expectedResult = new SendMessage(String.valueOf(123L),
                "You have entered unknown command. Please check and enter valid command");

        SendMessage actualResult = updateDispatcher.doDispatch(update);

        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }
}