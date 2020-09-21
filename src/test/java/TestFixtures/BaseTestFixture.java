package TestFixtures;

import Helpers.DataConverter;
import Helpers.Log;
import Helpers.SeleniumConfig;
import org.apache.http.ParseException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public abstract class BaseTestFixture {
    protected static String mavenEnvironment;
    protected static String siteUrl;

    public SeleniumConfig getProjectConfig(String configName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(configName + ".json")));
            JSONObject config = new JSONObject(content);

            SeleniumConfig seleniumConfig = new SeleniumConfig();

            DataConverter dataConverter = new DataConverter();

            seleniumConfig.browserKey = dataConverter.ConvertStringToBrowserKeyEnum(config.getString("browserKey"));
            seleniumConfig.browserVersion = config.getString("browserVersion");
            seleniumConfig.selenoidServerUrl = config.getString("selenoidServerUrl");
            seleniumConfig.siteUrl = config.getString("siteUrl");
            seleniumConfig.enableProxy = config.getBoolean("enableProxy");
            seleniumConfig.enableVideoRecording = config.getBoolean("enableVideoRecording");

            return seleniumConfig;
        } catch (IOException | ParseException e) {
            Log.error(e.toString());
        }

        return null;
    }

    public WebDriver getWebDriver(SeleniumConfig seleniumConfig, boolean incognito) {
        siteUrl = seleniumConfig.siteUrl;

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(seleniumConfig.browserKey.name());
        capabilities.setVersion(seleniumConfig.browserVersion);

        // ToDo fix after proxy work on chrome correct
//        if (seleniumConfig.enableProxy) {
//            capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
//        }

        switch (seleniumConfig.browserKey) {
            case chrome:
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--use-fake-ui-for-media-stream");
                options.addArguments("--disable-user-media-security");
                options.addArguments("--allow-running-insecure-content");
                options.addArguments("--use-fake-device-for-media-stream");

                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

                options.addArguments("--disable-popup-blocking");
                options.addArguments("start-maximized");
                options.addArguments("--disable-infobars");
                options.addArguments("--disable-notifications");


                if (incognito) {
                    options.addArguments("--incognito");
                }

                if (seleniumConfig.enableProxy) {
                    options.addArguments("--ignore-ssl-errors=yes");
                    options.addArguments("--ignore-certificate-errors");
                }

                // disable chrome save password
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                options.setExperimentalOption("prefs", prefs);

                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                break;
            case opera:
                capabilities.setCapability("operaOptions", new HashMap<String, String>() {{
                    put("binary", "/usr/bin/opera");
                }});
                break;
        }

        // Selenoid capabilities
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableLog", false); // ToDo save selenoidLogs to allure
        capabilities.setCapability("enableVideo", seleniumConfig.enableVideoRecording);
        capabilities.setCapability("sessionTimeout", "15m");

        RemoteWebDriver driver = null;

        try {
            driver = new RemoteWebDriver(new URL(
                    seleniumConfig.selenoidServerUrl
            ), capabilities);
        } catch (MalformedURLException e) {
            Log.error(e.toString());
        }

        driver.setFileDetector(new LocalFileDetector());  // selenoid enable upload file

        // ToDo fix after proxy work on chrome correct
//        if (seleniumConfig.enableProxy) {
//            // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
//            proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
//        }

        //new TakeVideoExtension().printCurrentTestVideoName(seleniumConfig);

        return driver;
    }

    public void setMavenEnvironment() {
        mavenEnvironment = System.getProperty("env", "selenoidConfig");
    }
}
