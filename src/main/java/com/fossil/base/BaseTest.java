package com.fossil.base;

import com.fossil.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.time.Duration;
import java.util.Collections;

/**
 * BaseTest manages the WebDriver lifecycle, parallel execution isolation via ThreadLocal,
 * cross-browser initialization, and reusable synchronization helpers.
 */
public class BaseTest {

    // ThreadLocal ensures thread-safety during parallel TestNG suite executions
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public WebDriver getDriver() {
        return driver.get();
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("") String browserParam) {
        String browserName = (browserParam != null && !browserParam.trim().isEmpty())
                ? browserParam
                : ConfigReader.getProperty("browser");

        boolean isHeadless = ConfigReader.getBooleanProperty("headless");
        WebDriver localDriver;

        switch (browserName.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (isHeadless) {
                    firefoxOptions.addArguments("-headless");
                }
                localDriver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (isHeadless) {
                    edgeOptions.addArguments("--headless=new");
                }
                localDriver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                // Point to user-level Chrome binary if running on Windows
                File userChrome = new File(System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe");
                if (userChrome.exists()) {
                    chromeOptions.setBinary(userChrome);
                }

                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--remote-allow-origins=*");
                // Anti-bot detection parameters for modern e-commerce websites
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");

                localDriver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.set(localDriver);

        // Configure timeouts through ConfigReader
        int implicitWait = ConfigReader.getIntProperty("implicitWait");
        int pageLoadTimeout = ConfigReader.getIntProperty("pageLoadTimeout");

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        getDriver().manage().window().maximize();

        // Navigate to Fossil India base URL
        String baseUrl = ConfigReader.getProperty("baseUrl");
        if (baseUrl != null && !baseUrl.isEmpty()) {
            getDriver().get(baseUrl);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (getDriver() != null) {
            try {
                getDriver().quit();
            } catch (Exception e) {
                System.err.println("Exception while quitting driver: " + e.getMessage());
            } finally {
                driver.remove();
            }
        }
    }

    // ========================================================
    // Synchronization & Explicit Wait Helpers
    // ========================================================

    public WebElement waitForVisibility(WebElement element) {
        int timeout = ConfigReader.getIntProperty("explicitWait");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForClickable(WebElement element) {
        int timeout = ConfigReader.getIntProperty("explicitWait");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void clickElement(WebElement element) {
        try {
            waitForClickable(element).click();
        } catch (Exception e) {
            // Fallback to Javascript click if element is intercepted by dynamic overlays
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].click();", element);
        }
    }

    public void enterText(WebElement element, String text) {
        WebElement el = waitForVisibility(element);
        el.clear();
        el.sendKeys(text);
    }
}

