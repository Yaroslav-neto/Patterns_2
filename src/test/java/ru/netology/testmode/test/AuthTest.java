package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;


class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    private void login(String login, String password) {
        $("[data-test-id='login'] input").setValue(login);
        $("[data-test-id='password'] input").setValue(password);
        $("button.button").click();
    }

    private void shouldSeeCabinet() {
        $("h2").should(Condition.exactText("Личный кабинет"));
    }

    private void verifyErrorMessage(String message, long seconds) {
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text(message), Duration.ofSeconds(seconds))
                .shouldBe(Condition.visible);
    }

    @Test
//    @DisplayName("Должен успешно пройти авторизацию зарегистрированным активным пользователем")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        login(registeredUser.getLogin(), registeredUser.getPassword());
        shouldSeeCabinet();
    }

    @Test
    @DisplayName("Получить ошибку при входе не зарегистрированного пользователя")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = DataGenerator.Registration.getUser("active");
        login(notRegisteredUser.getLogin(), notRegisteredUser.getPassword());
        verifyErrorMessage("Ошибка! Неверно указан логин или пароль", 18);
    }

    @Test
    @DisplayName("Получить ошибку при входе заблокированного пользователя")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
        login(blockedUser.getLogin(), blockedUser.getPassword());
        verifyErrorMessage("Ошибка! Пользователь заблокирован", 18);
    }

    @Test
    @DisplayName("Получить ошибку при вводе неправильного логина")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        String wrongLogin = DataGenerator.getRandomLogin();
        login(wrongLogin, registeredUser.getPassword());
        verifyErrorMessage("Ошибка! Неверно указан логин или пароль", 18);
    }

    @Test
    @DisplayName("Получить ошибку при вводе неправильного пароля")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        String wrongPassword = DataGenerator.getRandomPassword();
        login(registeredUser.getLogin(), wrongPassword);
        verifyErrorMessage("Ошибка! Неверно указан логин или пароль", 10);
    }
}