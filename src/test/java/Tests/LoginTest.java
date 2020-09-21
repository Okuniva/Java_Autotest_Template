package Tests;

import Pages.LoginPage;
import TestFixtures.BaseTestFixture;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

@Feature("Форма авторизации")
public class LoginTest extends BaseTestFixture {
    @Test
    @Description("Успешная авторизация")
    public void successfulLoginTest() {
        LoginPage.login_entry.setValue("admin@edstein.tech");
        LoginPage.password_entry.setValue("123456");
        LoginPage.submit_btn.click();
    }
}
