package gpb.itfactory.shevelatelegrambot.bot.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StartCommandHandlerTest {

    private final StartCommandHandler startCommandHandler;
    Chat chat;
    Message message;
    Update update;

    private static final String ANSWER = """
            Вас приветствует Bank Assistant Bot!
            
            Bank Assistant Bot предоставляет информацию об основных 
            финансовых продуктах банка, позволяет сделать заявки на их
            получение, выполнить расчет доходности или платежей по 
            вкладам, картам или кредитам, узнавать курсы валют и пр.
            
            Для получения необходимой информации введите соответствующую команду.
            
            С перечнем команд можно ознакомиться по команде /help.
            """;

    @Autowired
    StartCommandHandlerTest(StartCommandHandler startCommandHandler) {
        this.startCommandHandler = startCommandHandler;
    }

    @BeforeEach
    void init () {
        chat = new Chat(123L, "test");
        message = new Message();
        update = new Update();
        message.setChat(chat);
        update.setMessage(message);
    }

    @Test
    void handle() {
        SendMessage expectedResult = new SendMessage(String.valueOf(123L), ANSWER);

        SendMessage actualResult = startCommandHandler.handle(update);

        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void getCommand() {
        String actualResult = startCommandHandler.getCommand();

        Assertions.assertThat(actualResult).isEqualTo("/start");
    }

}