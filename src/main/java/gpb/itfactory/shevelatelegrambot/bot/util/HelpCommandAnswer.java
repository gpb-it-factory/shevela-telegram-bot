package gpb.itfactory.shevelatelegrambot.bot.util;

import java.util.stream.Stream;

public enum HelpCommandAnswer {
    START("/start - начало работы с ботом (первичная инструкция пользователю)"),
    HELP("/help - справка"),
    PING("/ping - тестовая команда (ответ <pong>)"),
    REGISTER("/register - регистрация нового пользователя"),
    ISREGISTER("/isregister - проверка регистрации пользователя"),
    CREATEACCOUNT("/createaccount - открытие счёта в нашем Мини-банке"),
    CURRENTBALANCE("/currentbalance - получить текущий баланс открытого пользователем счёта"),
    TRANSFER("/transfer toTelegramUser amount - перевод средств со счёта текущего пользователя на другой счёт по имени пользователя." +
            " toTelegramUser - пользователь, на счёт которого совершается перевод, amount - сумма перевода");

    private String value;

    HelpCommandAnswer(String value) {
        this.value = value;
    }

    public static String answer() {
        String help = Stream.of(HelpCommandAnswer.values()).map(v -> v.value).toList().toString();
        return help.substring(1, help.length()-1).replace(',', '\n');
    }
}
