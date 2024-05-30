package gpb.itfactory.shevelatelegrambot.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/* Обработчик команды /start */

@Slf4j
@Component
public class StartCommandHandler implements CommandHandler{

    private static final String COMMAND = "/start";
    private static final String ANSWER = """
            Вас приветствует Bank Assistant Bot!
            
            Bank Assistant Bot предоставляет информацию об основных 
            финансовых продуктах банка, позволяет сделать заявки на их
            получение, выполнить расчет доходности или платежей по 
            вкладам, картам или кредитам, узнавать курсы валют и пр.
            
            Для получения необходимой информации введите соответствующую команду.
            
            С перечнем команд можно ознакомиться по команде /help.
            """;

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.getMessage().getChatId();
        log.info("Processed command: " + COMMAND);
        return new SendMessage(String.valueOf(chatId), ANSWER);
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }
}
