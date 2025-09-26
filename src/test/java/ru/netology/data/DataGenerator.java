package ru.netology.data;


import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

// Основной класс
// Данный класс является утилитным
// В нем размещается приватный конструктор и статичные методы, которые отвечают за обработку данных
public class DataGenerator {

    // Создаем объект requestSpec.
    // Создаем класс-конструктор new RequestSpecBuilder(), который сохраняет настройки HTTP-запросов.
    // RequestSpecification — выступает конечным хранилищем этих настроек, которое затем используется при отправке запросов.
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost") // адрес сервера, к которому будем отправлять запросы.
            .setPort(9999) // номер порта сервера.
            .setAccept(ContentType.JSON) // тип принимаемых данных (JSON).
            .setContentType(ContentType.JSON) // тип отправляемых данных (JSON).
            .log(LogDetail.ALL) // настройка полного логирования каждого шага взаимодействия с сервером (полезно для дебага).
            .build(); // создаёт финальный объект спецификации, который позже будет использоваться при отправке запросов.

    // Конструктор DataGenerator объявлен приватным и пустым.
    // Чтобы нельзя было создать методы класса извне, этот способ обеспечивает безопасность.
    // У конструктора такое же название, как и у класса.
    // Данный конструктор нужен просто пустым для обеспечения безопасности данных.
    private DataGenerator() {
    }

    // Метод sendRequest - отправка запроса
    static void sendRequest(UserData user) {
        // Given - When - Then
        // Предусловия
        given()
                .spec(requestSpec) // используем предустановленные настройки запроса
                // Выполняемые действия
                .body(user) // добавляем тело запроса (наш объект пользователя)
                .when().log().all() // запускает выполнение запроса .post и выводит логи .log().all()
                .post("/api/system/users")// отправляем POST-запрос на соответствующий путь
                // Проверки
                .then().log().all() // Запускает проверку возвращенного результата, в данном случае код статуса
                .statusCode(200); // проверяем, что сервер ответил успешным кодом 200
    }

    private static Faker faker = new Faker(new Locale("en"));

    private static String login (){

        String name = faker.name().username();

        return name;
    }

    private static String password (){

        String password = faker.internet().password(12,14, true, true, true);

        return password;
    }

    // Внутренний класс
    // Класс для генерации пользователей, в классе мы пишем слово static.
    // Чтобы класс был независимым от внешнего класса
    public static class Registration {

        // Создаем приватный конструктор для предотвращения доступа к методам класса и изменения объектов класса
        private Registration() {
        }

        // Метод для генерации пользователя. getUser - получи пользователя.
        public static UserData getUser(String status) {

            // Вызывается метод login().
            // Метод генерирует случайный логин, который сохраняется в переменную login.
            String login = login();
            // Вызывается метод password().
            // Метод генерирует случайный пароль, который сохраняется в переменную password.
            String password = password();

            // Создаётся объект класса new UserData,
            // куда передаются ранее полученные значения (login, password), а также задаваемый параметр status.

            // UserData это класс, своего рода контейнер.
            // Выражение return new UserData (login, password, status) означает: создать
            // объект с характериристиками: (login, password, status)
            // Если не написать new, то объект не будет сохранен в контейнер
            // UserData выступает контейнером хранения пользователя, а getUser - методом который создает пользователя
            return new UserData (login, password, status);
        }

        // Метод для генерации зарегистрированных пользователей.
        // registredUser - зарегистрированный пользователь.
        public static UserData registredUser(String status) {

            var user = getUser(status);

            //Выполняется запрос на сервер через метод sendRequest(user) для регистрации пользователя.
            sendRequest(user);

            // Возвращаем зарегистрированного пользователя
            return user;
        }
    }

    // Аннотация @Value делает класс UserData неизменяемым (иммутабельным),
    // что означает невозможность изменения его свойств после создания объекта.
    //
    @Value
    public static class UserData {
        String login;
        String password;
        String status;
    }
}
