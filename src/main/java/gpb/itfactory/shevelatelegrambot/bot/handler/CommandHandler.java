package gpb.itfactory.shevelatelegrambot.bot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface CommandHandler {
    SendMessage handle(long chatId);
    String getCommand();
}
