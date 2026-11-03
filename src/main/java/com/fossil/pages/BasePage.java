package com.fossil.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Root class for all Page Objects in the Fossil E-Commerce automation suite.
 * Encapsulates common Selenium WebDriver actions, explicit waits,
 * dynamic popup dismissal, and price parsing utilities.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void click(WebElement element) {
        try {
            waitForClickable(element).click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            scrollIntoView(element);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    protected void type(WebElement element, String text) {
        WebElement el = waitForVisibility(element);
        el.clear();
        el.sendKeys(text);
    }

    protected String getElementText(WebElement element) {
        return waitForVisibility(element).getText().trim();
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            return waitForVisibility(element).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    /**
     * Closes common promotional or newsletter overlays if they appear dynamically.
     */
    public void dismissOverlayIfPresent(By overlayCloseBtnLocator) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement closeBtn = shortWait.until(ExpectedConditions.elementToBeClickable(overlayCloseBtnLocator));
            closeBtn.click();
        } catch (Exception ignored) {
            // Overlay did not appear; proceed normally
        }
    }

    /**
     * Parses currency strings (e.g., '₹ 12,495.00' or 'Rs. 9,995') into pure numeric double values.
     */
    public double parsePrice(String priceText) {
        if (priceText == null || priceText.trim().isEmpty()) {
            return 0.0;
        }
        String clean = priceText.replaceAll("[^0-9.]", "").trim();
        if (clean.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(clean);
    }

    public String getCurrentPageUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}

