package TestFixtures;

import Helpers.Log;
import Helpers.SeleniumConfig;
import Helpers.TakeScreenExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;

public class OneUserTestFixture extends BaseTestFixture {
    protected WebDriver driver;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        Log.startLog("setUp");

        new TakeScreenExtension().InitPath(testInfo.getDisplayName());

        setMavenEnvironment();

        SeleniumConfig seleniumConfig = getProjectConfig(mavenEnvironment);
        driver = getWebDriver(seleniumConfig, false);

        driver.get(siteUrl);
    }

    @AfterEach
    public void cleanUp() {
        try {
            driver.quit();
        } catch (Exception e) {
            Log.error(e.toString());
        }

        Log.info("end cleanUp");
    }
}
