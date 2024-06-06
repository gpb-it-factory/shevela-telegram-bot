package gpb.itfactory.shevelatelegrambot.bot.util;

import java.util.stream.Stream;

public enum HelpCommandAnswer {
    START("/start - начало работы с ботом (первичная инструкция пользователю)"),
    HELP("/help - справка"),
    PING("/ping - тестовая команда (ответ <pong>)"),
    REGISTER("/register - регистрация нового пользователя"),
    ISREGISTER("/isregister - проверка регистрации пользователя");

    private String value;

    HelpCommandAnswer(String value) {
        this.value = value;
    }

    public static String answer() {
        String help = Stream.of(HelpCommandAnswer.values()).map(v -> v.value).toList().toString();
        return help.substring(1, help.length()-1).replace(',', '\n');
    }
}
