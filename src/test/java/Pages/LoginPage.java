package Pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {
    public static SelenideElement login_entry = $(By.name("login"));
    public static SelenideElement password_entry = $(By.name("password"));
    public static SelenideElement submit_btn = $("button[type='submit']");

    @Step("Открыть страницу логина")
    public static void openLoginForm() {
        open(Configuration.baseUrl);
    }
}
