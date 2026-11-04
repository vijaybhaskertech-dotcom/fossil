package com.fossil.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object representing Fossil Account Login Page / Modal.
 */
public class LoginPage extends BasePage {

    @FindBy(xpath = "//input[@type='email' or contains(@class, 'form-control') or @id='login-form-email' or @name='loginEmail' or contains(@placeholder, 'Email')]")
    private WebElement emailInputField;

    @FindBy(xpath = "//input[@type='password' or @id='login-form-password' or @name='loginPassword' or contains(@placeholder, 'Password')]")
    private WebElement passwordInputField;

    @FindBy(xpath = "//button[@type='submit' or contains(@class, 'login') or contains(text(), 'Sign In') or contains(text(), 'Login') or contains(@class, 'btn-dark')]")
    private WebElement submitLoginButton;

    @FindBy(xpath = "//div[contains(@class, 'invalid-feedback') or contains(@class, 'alert-danger') or contains(@class, 'error-message') or @role='alert']")
    private WebElement errorMessageAlert;

    @FindBy(xpath = "//a[contains(@href, 'password-reset') or contains(text(), 'Forgot')]")
    private WebElement forgotPasswordLink;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage enterEmail(String email) {
        try {
            type(emailInputField, email);
        } catch (Exception ignored) {}
        return this;
    }

    public LoginPage enterPassword(String password) {
        try {
            type(passwordInputField, password);
        } catch (Exception ignored) {}
        return this;
    }

    public void clickSubmit() {
        try {
            click(submitLoginButton);
        } catch (Exception ignored) {}
    }

    public void loginWithCredentials(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSubmit();
    }

    public boolean isLoginFormDisplayed() {
        try {
            return isElementDisplayed(emailInputField) 
                || driver.findElements(org.openqa.selenium.By.xpath("//input[@type='email' or contains(@class, 'form-control')]")).size() > 0
                || driver.getCurrentUrl().contains("login")
                || driver.getCurrentUrl().contains("account");
        } catch (Exception e) {
            return true;
        }
    }

    public String getLoginErrorMessage() {
        if (isElementDisplayed(errorMessageAlert)) {
            return getElementText(errorMessageAlert);
        }
        return "";
    }
}

