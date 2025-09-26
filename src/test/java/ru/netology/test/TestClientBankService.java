package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestClientBankService {

    // @BeforeEach - это аннотация в JUnit.
    // В этом разделе пишут методы, которые будут выполняться перед каждым последующим тестом.
    @BeforeEach
    void openPage() {
        open("http://localhost:9999");
    }

    @Test
    // Должен авторизировать клиента со статусом активен
    @DisplayName("Should authorize client with the active status" )

    // Метод shouldAuthorizeClientActiveStatus - должен авторизировать клиента
    void shouldAuthorizeClientActiveStatus() {

        // В качестве аргумента передаем статус "active"
        var activeUser = DataGenerator.Registration.registredUser("active");

        // .setValue(activeUser.getLogin()) - обрати внимание, переменную activeUser мы объявляем в строке выше.
        $("[data-test-id='login'] input").setValue(activeUser.getLogin());
        $("[data-test-id='password'] input").setValue(activeUser.getPassword());
        $("[data-test-id='action-login'] span.button__text").click();
        $("h2.heading.heading_size_l.heading_theme_alfa-on-white")
                .shouldHave(Condition.text("Личный кабинет"), Duration.ofSeconds(5))
                .shouldBe(Condition.visible);

    }

    @Test
    // Должен авторизировать клиента со статусом заблокирован
    @DisplayName("Should authorize client with the blocked status" )
    // Метод shouldAuthorizeClientBlocedStatus - должен авторизировать клиента
    void shouldAuthorizeClientBlocedStatus() {

        // В качестве аргумента передаем статус "blocked"
        var blockedUser = DataGenerator.Registration.registredUser("blocked");

        // .setValue(activeUser.getLogin()) - обрати внимание, переменную activeUser мы объявляем в строке выше.
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login'] span.button__text").click();
                $("[data-test-id='error-notification'] [class='notification__content']")
                .shouldHave(Condition.text("Пользователь заблокирован"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);

    }

    @Test
    // Должен авторизировать клиента со статусом заблокирован
    @DisplayName("Should authorize client with the blocked status" )
        // Метод shouldAuthorizeNotRegisteredClient - должен авторизировать не зарегистрированного клиента
    void shouldAuthorizeNotRegisteredClient() {

        // В качестве аргумента передаем статус "active"
        var activeUser = DataGenerator.Registration.getUser("active");


        $("[data-test-id='login'] input").setValue(activeUser.getLogin());
        $("[data-test-id='password'] input").setValue(activeUser.getPassword());
        $("[data-test-id='action-login'] span.button__text").click();
        $("[data-test-id='error-notification'] [class='notification__content']")
                .shouldHave(Condition.text("Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);

    }

}
