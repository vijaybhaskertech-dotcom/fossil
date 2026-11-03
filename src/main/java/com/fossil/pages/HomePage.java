package com.fossil.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object representing Fossil India Homepage (https://www.fossil.in/).
 * Encapsulates search bar, top navigation, account links, and promotional modal handlers.
 */
public class HomePage extends BasePage {

    // Locators for search functionality
    @FindBy(xpath = "//input[@name='q' or @type='search' or contains(@class, 'search-field') or contains(@placeholder, 'Search')]")
    private WebElement searchInputField;

    @FindBy(xpath = "//button[@type='submit' and contains(@class, 'search')] | //button[@aria-label='Search'] | //button[contains(@class, 'search-icon')]")
    private WebElement searchSubmitButton;

    // Locators for Header / Navigation
    @FindBy(xpath = "//a[contains(@href, '/login') or contains(@href, 'account') or contains(@aria-label, 'Sign in') or contains(@class, 'user-account')]")
    private WebElement accountLoginLink;

    @FindBy(xpath = "//a[contains(@href, '/cart') or contains(@class, 'minicart-link') or contains(@aria-label, 'Shopping Bag') or contains(@class, 'bag')]")
    private WebElement shoppingBagIcon;

    @FindBy(xpath = "//span[contains(@class, 'minicart-quantity') or contains(@class, 'badge') or contains(@class, 'bag-count')]")
    private WebElement bagItemBadge;

    @FindBy(xpath = "//a[contains(@class, 'navbar-brand') or contains(@class, 'logo')]")
    private WebElement brandLogo;

    // Locators for popup modals
    private final By promotionalModalCloseBtn = By.xpath("//button[contains(@class, 'close') or @aria-label='Close' or contains(@class, 'dismiss')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage dismissPromotionalBanner() {
        dismissOverlayIfPresent(promotionalModalCloseBtn);
        return this;
    }

    public ProductListingPage searchFor(String keyword) {
        dismissPromotionalBanner();
        waitForVisibility(searchInputField);
        searchInputField.clear();
        searchInputField.sendKeys(keyword);
        searchInputField.sendKeys(Keys.ENTER);
        return new ProductListingPage(driver);
    }

    public LoginPage navigateToLogin() {
        dismissPromotionalBanner();
        click(accountLoginLink);
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
        return isElementDisplayed(brandLogo);
    }
}

