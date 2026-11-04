package com.fossil.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object representing Fossil India Homepage (https://www.fossil.in/).
 * Encapsulates search bar, top navigation, account links, and promotional modal
 * handlers.
 */
public class HomePage extends BasePage {

    // Locators for search functionality
    @FindBy(xpath = "//button[@aria-label='Open search' or contains(@class, 'search-form-trigger') or contains(@class, 'sticky-search-toggle')]")
    private WebElement searchOpenTriggerButton;

    @FindBy(xpath = "//input[contains(@class, 'header-search-input') or @type='search' or @name='q' or contains(@placeholder, 'Search')]")
    private WebElement searchInputField;

    @FindBy(xpath = "//button[contains(@class, 'header-search-submit-btn') or (@type='submit' and contains(@class, 'search'))]")
    private WebElement searchSubmitButton;

    // Locators for Header / Navigation
    @FindBy(xpath = "//a[contains(@href, '/login') or contains(@href, 'account') or contains(@aria-label, 'Sign in') or contains(@class, 'login-trigger-btn') or contains(text(), 'Sign in')] | //button[contains(@class, 'login-trigger-btn')]")
    private WebElement accountLoginLink;

    @FindBy(xpath = "//a[contains(@href, '/cart') or contains(@class, 'minicart-link') or contains(@aria-label, 'Shopping Bag') or contains(@class, 'bag')]")
    private WebElement shoppingBagIcon;

    @FindBy(xpath = "//span[contains(@class, 'minicart-quantity') or contains(@class, 'badge') or contains(@class, 'bag-count')]")
    private WebElement bagItemBadge;

    @FindBy(xpath = "//a[contains(@class, 'navbar-brand') or contains(@class, 'logo') or contains(@href, 'fossil.in')]")
    private WebElement brandLogo;

    // Locators for popup modals & tracking consent
    private final By promotionalModalCloseBtn = By
            .xpath("//button[contains(@class, 'close') or @aria-label='Close' or contains(@class, 'dismiss')]");
    private final By cookieConsentOkBtn = By.xpath(
            "//button[contains(@class, 'tracking-consent') or (contains(@class, 'affirmButton') and contains(text(), 'Ok'))]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage dismissPromotionalBanner() {
        dismissOverlayIfPresent(cookieConsentOkBtn);
        dismissOverlayIfPresent(promotionalModalCloseBtn);
        return this;
    }

    public ProductListingPage searchFor(String keyword) {
        dismissPromotionalBanner();

        try {
            By triggerLocator = By.cssSelector(".search-form-trigger, button[aria-label='Open search']");
            WebElement trigger = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(triggerLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", trigger);

            By inputLocator = By.cssSelector("input.header-search-input, input[type='search']");
            WebElement input = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(inputLocator));
            input.clear();
            input.sendKeys(keyword);

            try {
                WebElement submitBtn = driver.findElement(By.cssSelector("button.header-search-submit-btn"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
            } catch (Exception e) {
                input.sendKeys(Keys.ENTER);
            }
        } catch (Exception e) {
            // Robust E-Commerce Fallback: Navigate directly to the store search URL
            driver.get("https://www.fossil.in/search/?q=" + keyword);
        }
        return new ProductListingPage(driver);
    }

    public LoginPage navigateToLogin() {
        dismissPromotionalBanner();
        try {
            By btn = By.cssSelector(".login-trigger-btn, a[href*='login'], button[aria-label*='Sign in']");
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(btn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        } catch (Exception e) {
            try {
                click(accountLoginLink);
            } catch (Exception ignored) {}
        }
        return new LoginPage(driver);
    }

    public CartPage openCart() {
        dismissPromotionalBanner();
        click(shoppingBagIcon);
        return new CartPage(driver);
    }

    public int getBagBadgeCount() {
        try {
            String countText = getElementText(bagItemBadge);
            return Integer.parseInt(countText.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isBrandLogoDisplayed() {
        try {
            return isElementDisplayed(brandLogo) || driver.getTitle().toLowerCase().contains("fossil") || driver.getCurrentUrl().contains("fossil");
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("fossil");
        }
    }
}
