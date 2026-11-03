package com.fossil.tests;

import com.fossil.base.BaseTest;
import com.fossil.pages.*;
import com.fossil.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * End-to-End E-Commerce Workflow Test matching all Core Functional Requirements:
 * 1. Navigation: Launch browser dynamically & navigate to e-commerce site.
 * 2. Product Flow: Account flow, search specific product, verify price, click 'Add to Cart'.
 * 3. Cart Verification: Navigate to cart, assert item count updates, verify checkout summary pricing.
 */
public class FossilEndToEndTest extends BaseTest {

    @Test(priority = 1, description = "Core E2E Flow: Navigation -> Search -> PDP Price Verification -> Add to Bag -> Cart Item & Summary Verification")
    public void testCompleteECommerceProductPurchaseFlow() {
        SoftAssert softAssert = new SoftAssert();

        // -------------------------------------------------------------
        // Step 1: Navigation Verification
        // -------------------------------------------------------------
        HomePage homePage = new HomePage(getDriver());
        String expectedUrl = ConfigReader.getProperty("baseUrl");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("fossil"), "Failed to navigate to Fossil E-Commerce site!");
        softAssert.assertTrue(homePage.isBrandLogoDisplayed(), "Fossil Brand Logo is not visible upon navigation!");

        // -------------------------------------------------------------
        // Step 2: Search or Locate a Specific Product
        // -------------------------------------------------------------
        String searchKeyword = ConfigReader.getProperty("searchKeyword");
        ProductListingPage plp = homePage.searchFor(searchKeyword);

        int productCount = plp.getProductCount();
        System.out.println("Located products on search PLP: " + productCount);
        softAssert.assertTrue(productCount >= 0, "Product list count is negative!");

        // Extract and record price on listing
        double plpPrice = plp.getFirstProductPriceValue();
        System.out.println("PLP First Product Price: ₹ " + plpPrice);

        // -------------------------------------------------------------
        // Step 3: Product Detail Flow & Price Verification
        // -------------------------------------------------------------
        ProductDetailsPage pdp = plp.selectFirstProduct();

        String pdpTitle = pdp.getProductTitle();
        double pdpPrice = pdp.getProductPriceValue();
        System.out.println("PDP Product Title: " + pdpTitle);
        System.out.println("PDP Product Price: ₹ " + pdpPrice);

        // Assert that product has a valid price displayed (> 0)
        Assert.assertTrue(pdpPrice > 0, "Product price must be greater than zero!");

        // Verify PDP price matches or relates to listing price
        if (plpPrice > 0) {
            softAssert.assertEquals(pdpPrice, plpPrice, "Price mismatch between search listing and product detail page!");
        }

        // Click 'Add to Bag' / 'Add to Cart'
        Assert.assertTrue(pdp.isAddToCartButtonEnabled(), "'Add to Bag' button should be active and clickable!");
        pdp.clickAddToCart();

        // -------------------------------------------------------------
        // Step 4: Cart Verification & Checkout Summary Calculation
        // -------------------------------------------------------------
        CartPage cartPage = pdp.proceedToCart();

        // Assert that shopping bag item count updated accurately
        int cartItemsCount = cartPage.getCartItemCount();
        System.out.println("Cart Items Count: " + cartItemsCount);
        softAssert.assertTrue(cartItemsCount >= 1, "Shopping cart count did not update after adding product!");

        // Assert checkout summary calculations (Subtotal + Shipping + Tax == Grand Total)
        double subtotal = cartPage.getSubtotal();
        double grandTotal = cartPage.getGrandTotal();
        System.out.println("Cart Subtotal: ₹ " + subtotal);
        System.out.println("Cart Grand Total: ₹ " + grandTotal);

        boolean isCalculationValid = cartPage.verifyCheckoutSummaryCalculations();
        softAssert.assertTrue(isCalculationValid, "Final checkout summary calculation is incorrect (Subtotal + Shipping + Tax != Total)!");

        // Assert checkout button readiness
        softAssert.assertTrue(cartPage.isCheckoutButtonEnabled(), "Checkout button is not enabled on Cart Page!");

        // Consolidate all soft assertion validations
        softAssert.assertAll();
    }

    @Test(priority = 2, description = "Defect Verification [BUG-442-01]: Verify Remove Button action on Shopping Bag item")
    public void testCartItemRemoveDefectVerification() {
        HomePage homePage = new HomePage(getDriver());
        String searchKeyword = ConfigReader.getProperty("searchKeyword");
        ProductListingPage plp = homePage.searchFor(searchKeyword);
        ProductDetailsPage pdp = plp.selectFirstProduct();
        pdp.clickAddToCart();
        CartPage cartPage = pdp.proceedToCart();

        int initialCount = cartPage.getCartItemCount();
        Assert.assertTrue(initialCount >= 1, "Cart should have at least 1 item before testing removal!");

        Assert.assertTrue(cartPage.isRemoveButtonPresent(), "Remove button should be visible on cart line item!");
        cartPage.clickRemoveItem();

        // Check if item is removed or if no action occurred (Defect BUG-442-01)
        int updatedCount = cartPage.getCartItemCount();
        System.out.println("Defect Check [BUG-442-01] -> Initial Count: " + initialCount + ", Post-Remove Click Count: " + updatedCount);

        // Assertion: Validates expected behavior vs actual defective behavior
        Assert.assertTrue(cartPage.isCartEmpty() || updatedCount < initialCount,
            "[DEFECT REPRODUCED - BUG-442-01]: Clicking 'Remove' button did not remove item or decrement cart count! Refer to docs/evidence/BUG_442_Cart_Remove_Button_No_Action.webm");
    }
}

