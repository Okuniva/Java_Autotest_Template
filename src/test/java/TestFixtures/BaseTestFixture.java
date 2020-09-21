package TestFixtures;

import Helpers.Log;
import Helpers.MyRemoteWebDriverClass;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;

import java.util.logging.Level;

import static Helpers.AllureHelpers.takeScreenshot;
import static Helpers.SelenoidVideo.attachAllureVideo;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class BaseTestFixture {
    @BeforeEach
    public void setUp() {
        Log.info("setUp");
        Configuration.browser = MyRemoteWebDriverClass.class.getName();
        Configuration.browserSize = "1920x1080";
        Configuration.startMaximized = true;
        System.out.println("RemoteWebDriver");

        Configuration.reportsFolder = "target/reports";
        Configuration.screenshots = false;
        //Configuration.fastSetValue = true;

        Configuration.baseUrl = System.getProperty("base.url");

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .enableLogs(LogType.BROWSER, Level.ALL)
                .enableLogs(LogType.PERFORMANCE, Level.ALL));

        Log.debug("Video name:" + getSessionId() + ".mp4");

        open(Configuration.baseUrl);
    }

    public static String getSessionId() {
        return ((RemoteWebDriver) getWebDriver()).getSessionId().toString();
    }

    @AfterEach
    public void cleanUp() {
        Log.info("cleanUp");
        String sessionId = getSessionId();
        takeScreenshot();
        closeWebDriver();

        if ("true".equals(System.getProperty("video.enabled"))) {
            sleep(5000);
            attachAllureVideo(sessionId);
        }
    }
}
