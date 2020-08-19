package Pages;

import Helpers.Log;
import Helpers.SleepExtension;
import Helpers.TakeScreenExtension;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected TakeScreenExtension ts;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 120);
        ts = new TakeScreenExtension();
        Log.info("(Page loaded) Object of Page " + this.getClass().getSimpleName() + " created");
    }

    protected WebElement Wait(By element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void openNewTab(String url) {
        String a = "window.open('" + url + "','_blank');";
        ((JavascriptExecutor) driver).executeScript(a);

        Set<String> tab_handles = driver.getWindowHandles();
        int number_of_tabs = tab_handles.size();
        int new_tab_index = number_of_tabs - 1;
        driver.switchTo().window(tab_handles.toArray()[new_tab_index].toString());
    }

    protected void waitToHide(By element) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(element));
    }

    protected void tap(By element) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click()", Wait(element));
    }

    protected void tap(WebElement element) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].click()", element);
    }

    protected void actionTap(WebElement element) {
        Actions builder = new Actions(driver);
        builder.moveToElement(element)
                .click(element);
        Action mouseoverAndClick = builder.build();
        mouseoverAndClick.perform();
    }

    protected void enter(By element, String text) {
        tap(element);
        driver.findElement(element).sendKeys(text);
    }

    protected void jsEnter(By element, String text) {
        tap(element);
        WebElement activeElement = driver.switchTo().activeElement();
        activeElement.sendKeys(Keys.TAB);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement el = driver.findElement(element);
        js.executeScript("arguments[0].value = arguments[1];", el, text);
        SleepExtension.sleep(500);
        tap(element);
    }

    protected void clear(By element) {
        tap(element);
        driver.findElement(element).clear();
    }

    protected void selectClearAndEnter(By element, String text) {
        tap(element);
        driver.findElement(element).sendKeys(Keys.chord(Keys.CONTROL, "a"), text);
    }

    protected void uploadFile(String fileName) {
        SleepExtension.sleep(2000);
        WebElement input = driver.findElement(By.cssSelector("input[type='file']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", input);

        String projectPath = System.getProperty("user.dir");
        input.sendKeys(projectPath + "/recourse/" + fileName);
    }

    protected void scrollDown() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollBy(1850,850)", "");
        SleepExtension.sleep(2000);
    }

    protected void scrollDown(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        SleepExtension.sleep(2000);
    }
}
