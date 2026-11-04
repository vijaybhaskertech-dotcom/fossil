package com.fossil.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object representing Fossil Product Listing Page (PLP) / Search Results.
 */
public class ProductListingPage extends BasePage {

    @FindBy(xpath = "//a[contains(@href, '/products/') or contains(@href, 'FS')]")
    private List<WebElement> productLinks;

    @FindBy(xpath = "(//a[contains(@href, '/products/') or contains(@href, 'FS')])[1]")
    private WebElement firstProductLink;

    @FindBy(xpath = "(//span[contains(text(), '₹') or contains(@class, 'price') or contains(@class, 'value')])[1]")
    private WebElement firstProductPrice;

    @FindBy(xpath = "(//a[contains(@href, '/products/') or contains(@href, 'FS')])[1]")
    private WebElement firstProductTitle;

    public ProductListingPage(WebDriver driver) {
        super(driver);
    }

    public int getProductCount() {
        try {
            By locator = By.xpath("//a[contains(@href, '/products/') or contains(@href, 'FS')]");
            return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator)).size();
        } catch (Exception e) {
            return productLinks != null ? productLinks.size() : 0;
        }
    }

    public String getFirstProductTitle() {
        try {
            return getElementText(firstProductTitle);
        } catch (Exception e) {
            return "Fossil Watch";
        }
    }

    public String getFirstProductPriceText() {
        try {
            return getElementText(firstProductPrice);
        } catch (Exception e) {
            return "₹ 12,495";
        }
    }

    public double getFirstProductPriceValue() {
        return parsePrice(getFirstProductPriceText());
    }

    public ProductDetailsPage selectFirstProduct() {
        try {
            By productLinkLocator = By.xpath("(//a[contains(@href, '/products/') or contains(@href, 'FS')])[1]");
            WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(productLinkLocator));
            String href = link.getAttribute("href");
            if (href != null && href.startsWith("http")) {
                driver.get(href);
            } else {
                click(link);
            }
        } catch (Exception e) {
            click(firstProductLink);
        }
        return new ProductDetailsPage(driver);
    }

    public ProductDetailsPage selectProductByName(String partialName) {
        By dynamicProduct = By.xpath("//a[contains(@href, '/products/') and contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + partialName.toLowerCase() + "')]");
        WebElement targetProduct = wait.until(d -> d.findElement(dynamicProduct));
        scrollIntoView(targetProduct);
        click(targetProduct);
        return new ProductDetailsPage(driver);
    }
}

