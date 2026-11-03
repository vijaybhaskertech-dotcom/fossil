package com.fossil.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object representing Fossil Product Details Page (PDP).
 * Handles product price extraction, size/option selection, and 'Add to Bag' actions.
 */
public class ProductDetailsPage extends BasePage {

    @FindBy(xpath = "//h1[contains(@class, 'product-name') or contains(@class, 'product-title')]")
    private WebElement productTitleHeader;

    @FindBy(xpath = "//div[contains(@class, 'prices')]//span[contains(@class, 'value') or contains(@class, 'price') or contains(@class, 'sales')] | //span[contains(@class, 'price-sales')]")
    private WebElement productPriceSpan;

    @FindBy(xpath = "//button[contains(@class, 'add-to-cart') or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'add to bag') or contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'add to cart')]")
    private WebElement addToBagButton;

    @FindBy(xpath = "//a[contains(@class, 'btn-primary') and (contains(text(), 'View Bag') or contains(text(), 'Checkout') or contains(@href, '/cart'))] | //a[contains(@class, 'minicart-link')]")
    private WebElement viewBagOrCheckoutModalButton;

    @FindBy(xpath = "//div[contains(@class, 'alert-success') or contains(@class, 'minicart') or contains(text(), 'Added to')]")
    private WebElement addedToCartNotification;

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getProductTitle() {
        return getElementText(productTitleHeader);
    }

    public String getProductPriceText() {
        return getElementText(productPriceSpan);
    }

    public double getProductPriceValue() {
        return parsePrice(getProductPriceText());
    }

    public ProductDetailsPage clickAddToCart() {
        scrollIntoView(addToBagButton);
        click(addToBagButton);
        return this;
    }

    public CartPage proceedToCart() {
        try {
            // First look for the immediate mini-cart 'View Bag' modal button
            click(viewBagOrCheckoutModalButton);
        } catch (Exception e) {
            // Alternatively, navigate directly via Header cart icon or direct URL
            driver.navigate().to("https://www.fossil.in/en-in/cart");
        }
        return new CartPage(driver);
    }

    public boolean isAddToCartButtonEnabled() {
        return waitForVisibility(addToBagButton).isEnabled();
    }
}

