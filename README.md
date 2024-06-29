<h1 align="center">
  <a><img src="https://thumbs.dreamstime.com/b/%D0%B7%D0%B5%D0%BB%D0%B5%D0%BD%D1%8B%D0%B9-%D1%80%D0%BE%D0%B1%D0%BE%D1%82-%D0%B8%D0%BB%D0%BB%D1%8E%D1%81%D1%82%D1%80%D0%B0%D1%86%D0%B8%D1%8F-d-124238087.jpg" width="300"></a>
  <br>
  Bank assistant telegram-bot
  <br>
</h1>

<p align="center">
  <a href="#Функционал">Функционал</a> • • 
  <a href="#Запуск">Запуск</a> • • 
  <a href="#Быстрый-старт">Быстрый старт</a> • • 
  <a href="#Как-это-работает">Как это работает</a> • • 
  <a href="#Стэк">Стэк</a> • • 
  <a href="#Полезные-ссылки">Полезные ссылки</a> • • 
</p>

---

## Функционал

Bank assistant telegram-bot предоставляет информацию об основных финансовых продуктах банка, позволяет создавать заявки на их получение, выполнять расчет доходности или платежей по вкладам, картам или кредитам, узнавать курсы валют и пр.

---

## Запуск

* Скопировать проект с GitHub по [ссылке](https://github.com/gpb-it-factory/shevela-telegram-bot) (или _git clone git@github.com:gpb-it-factory/shevela-telegram-bot.git_)
* Добавить переменые среды BOT_NAME и BOT_TOKEN
* Из консоли перейти в директорию проекта и выполнить сборку проекта в jar-архив путем ввода команд _gradle build_ (либо открыть проект в IDE, например IntelliJ IDEA и выполнить запуск из нее)
* Для запуска проекта из консоли ввести команды _java -jar <полный путь к shevela-telegram-bot-0.0.1-SNAPSHOT.jar>_ (например C:\Users\\...\IdeaProjects\shevela-telegram-bot\build\libs\shevela-telegram-bot-0.0.1-SNAPSHOT.jar)

---

## Быстрый старт
* /start - начало работы с ботом, первичная инструкция пользователю
* /help - справка
* /register - первичное оформление пользователя в банке
* /isregister - проверка регистрации пользователя
* /createaccount - открытие счёта в Мини-банке, у клиента может быть только один счёт
* /currentbalance - получить текущий баланс открытого пользователем счёта
* /transfer toTelegramUser amount - перевод средств со счёта текущего пользователя на другой счёт по имени пользователя. toTelegramUser - пользователь, на счёт которого совершается перевод, amount - сумма перевода

---

## Как это работает

#### Приложение имеет следующую архитектуру:
* Frontend - telegram-бот, выступает как клиентское приложение, инициирует запросы пользователей
* Middle - java-сервис, принимает запросы от tg-бота, выполняет валидацию и бизнес-логику, маршрутизирует запросы
* Backend - автоматизированная банковская система (АБС), обрабатывает транзакции, хранит клиентские данные

```plantuml
actor User
User -> Telegram : Command

activate Telegram
Telegram -> JavaApplication : Http-request

activate JavaApplication
JavaApplication -> ABS : Http-request

activate ABS
ABS -> JavaApplication : Http-response
deactivate ABS

JavaApplication -> Telegram : Http-response
deactivate JavaApplication

Telegram -> User : Information
deactivate Telegram
```

---

## Стэк
* Java
* Gradle
* Spring Boot
* Hibernate
* PostgreSQL

---


## Полезные ссылки

* <https://core.telegram.org/bots>
* <https://handbook.tmat.me/ru/>
* <https://mvnrepository.com/artifact/org.telegram/telegrambots>