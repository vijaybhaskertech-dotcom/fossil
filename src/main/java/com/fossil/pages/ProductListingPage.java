package com.fossil.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object representing Fossil Product Listing Page (PLP) / Search Results.
 */
public class ProductListingPage extends BasePage {

    @FindBy(xpath = "//div[contains(@class, 'product-tile') or contains(@class, 'product-grid-item') or contains(@class, 'product-item')]")
    private List<WebElement> productTiles;

    @FindBy(xpath = "(//div[contains(@class, 'product-tile')]//a[contains(@class, 'link') or contains(@class, 'title')])[1] | (//a[contains(@class, 'pdp-link')])[1]")
    private WebElement firstProductLink;

    @FindBy(xpath = "(//div[contains(@class, 'product-tile')]//span[contains(@class, 'value') or contains(@class, 'sales') or contains(@class, 'price')])[1]")
    private WebElement firstProductPrice;

    @FindBy(xpath = "(//div[contains(@class, 'product-tile')]//a[contains(@class, 'title') or contains(@class, 'pdp-link')])[1]")
    private WebElement firstProductTitle;

    @FindBy(xpath = "//span[contains(@class, 'search-result-count') or contains(@class, 'result-count')]")
    private WebElement searchResultCount;

    public ProductListingPage(WebDriver driver) {
        super(driver);
    }

    public int getProductCount() {
        try {
            return productTiles.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getFirstProductTitle() {
        return getElementText(firstProductTitle);
    }

    public String getFirstProductPriceText() {
        return getElementText(firstProductPrice);
    }

    public double getFirstProductPriceValue() {
        return parsePrice(getFirstProductPriceText());
    }

    public ProductDetailsPage selectFirstProduct() {
        scrollIntoView(firstProductLink);
        click(firstProductLink);
        return new ProductDetailsPage(driver);
    }

    public ProductDetailsPage selectProductByName(String partialName) {
        By dynamicProduct = By.xpath("//a[contains(@class, 'pdp-link') and contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + partialName.toLowerCase() + "')]");
        WebElement targetProduct = wait.until(d -> d.findElement(dynamicProduct));
        scrollIntoView(targetProduct);
        click(targetProduct);
        return new ProductDetailsPage(driver);
    }
}

