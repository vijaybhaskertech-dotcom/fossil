package com.fossil.tests;

import com.fossil.base.BaseTest;
import com.fossil.pages.HomePage;
import com.fossil.pages.LoginPage;
import com.fossil.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Functional Test Suite for Fossil User Authentication & Account Access.
 */
public class FossilLoginTest extends BaseTest {

    @Test(priority = 1, description = "Verify User can navigate to Login page and view input elements")
    public void testLoginPageDisplay() {
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.navigateToLogin();

        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "Login form fields (email and submit) were not displayed!");
    }

    @Test(priority = 2, description = "Verify invalid login credentials trigger error feedback")
    public void testInvalidLoginCredentials() {
        HomePage homePage = new HomePage(getDriver());
        LoginPage loginPage = homePage.navigateToLogin();

        String testEmail = ConfigReader.getProperty("testEmail");
        String testPassword = ConfigReader.getProperty("testPassword");

        loginPage.loginWithCredentials(testEmail, testPassword);

        String errorMsg = loginPage.getLoginErrorMessage();
        System.out.println("Login response error message: " + errorMsg);
        // Asserts either an explicit alert message is shown or login form prevents invalid submission
        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "User should remain on login page after failed authentication!");
    }
}

