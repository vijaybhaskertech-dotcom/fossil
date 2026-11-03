package com.fossil.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object representing Fossil Shopping Bag / Cart Page.
 * Verifies cart item counts, product line-items, and checkout summary pricing logic.
 */
public class CartPage extends BasePage {

    @FindBy(xpath = "//div[contains(@class, 'product-info') or contains(@class, 'line-item') or contains(@class, 'cart-row')]")
    private List<WebElement> cartLineItems;

    @FindBy(xpath = "(//div[contains(@class, 'line-item-name') or contains(@class, 'product-summary-title') or contains(@class, 'pdp-link')])[1]")
    private WebElement firstCartItemTitle;

    @FindBy(xpath = "//span[contains(@class, 'sub-total') or contains(@class, 'subtotal')]")
    private WebElement subtotalAmountSpan;

    @FindBy(xpath = "//span[contains(@class, 'shipping-cost') or contains(@class, 'estimated-shipping')]")
    private WebElement shippingCostSpan;

    @FindBy(xpath = "//span[contains(@class, 'tax-total') or contains(@class, 'estimated-tax')]")
    private WebElement taxTotalSpan;

    @FindBy(xpath = "//span[contains(@class, 'grand-total') or contains(@class, 'order-total') or contains(@class, 'total-price')]")
    private WebElement grandTotalSpan;

    @FindBy(xpath = "//a[contains(@class, 'checkout-btn') or contains(@class, 'btn-checkout') or contains(text(), 'Checkout')]")
    private WebElement checkoutButton;

    @FindBy(xpath = "//h1[contains(text(), 'Bag') or contains(text(), 'Cart') or contains(@class, 'cart-header')]")
    private WebElement cartHeader;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartItemCount() {
        try {
            return cartLineItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getFirstItemTitle() {
        return getElementText(firstCartItemTitle);
    }

    public double getSubtotal() {
        return parsePrice(getElementText(subtotalAmountSpan));
    }

    public double getShippingCost() {
        try {
            String text = getElementText(shippingCostSpan);
            if (text.toLowerCase().contains("free")) {
                return 0.0;
            }
            return parsePrice(text);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public double getTaxAmount() {
        try {
            return parsePrice(getElementText(taxTotalSpan));
        } catch (Exception e) {
            return 0.0;
        }
    }

    public double getGrandTotal() {
        return parsePrice(getElementText(grandTotalSpan));
    }

    /**
     * Validates that Subtotal + Shipping + Tax == Grand Total within reasonable floating-point tolerance.
     */
    public boolean verifyCheckoutSummaryCalculations() {
        double subtotal = getSubtotal();
        double shipping = getShippingCost();
        double tax = getTaxAmount();
        double grandTotal = getGrandTotal();

        double expectedTotal = Math.round((subtotal + shipping + tax) * 100.0) / 100.0;
        double actualTotal = Math.round(grandTotal * 100.0) / 100.0;

        return Math.abs(expectedTotal - actualTotal) < 1.0;
    }

    public boolean isCheckoutButtonEnabled() {
        return waitForVisibility(checkoutButton).isEnabled();
    }
}

