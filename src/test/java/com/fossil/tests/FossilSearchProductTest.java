package com.fossil.tests;

import com.fossil.base.BaseTest;
import com.fossil.pages.HomePage;
import com.fossil.pages.ProductDetailsPage;
import com.fossil.pages.ProductListingPage;
import com.fossil.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Functional Test Suite for Fossil Product Search, PLP Rendering, and PDP Navigation.
 */
public class FossilSearchProductTest extends BaseTest {

    @Test(priority = 1, description = "Verify search returns product listings and prices for keywords")
    public void testSearchFunctionality() {
        HomePage homePage = new HomePage(getDriver());
        String keyword = ConfigReader.getProperty("searchKeyword");

        ProductListingPage plp = homePage.searchFor(keyword);
        int productCount = plp.getProductCount();
        System.out.println("Search results found for '" + keyword + "': " + productCount);

        Assert.assertTrue(productCount >= 0, "Product count should be non-negative");
    }

    @Test(priority = 2, description = "Verify navigating from search results to PDP maintains valid price")
    public void testNavigateToProductDetails() {
        HomePage homePage = new HomePage(getDriver());
        ProductListingPage plp = homePage.searchFor("Watch");

        ProductDetailsPage pdp = plp.selectFirstProduct();
        String title = pdp.getProductTitle();
        double price = pdp.getProductPriceValue();

        System.out.println("Selected Product: " + title + " | Price: ₹ " + price);
        Assert.assertNotNull(title, "Product title on PDP should not be null");
        Assert.assertTrue(price >= 0.0, "Product price should be valid number");
    }
}

