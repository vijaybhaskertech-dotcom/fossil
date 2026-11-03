package com.fossil.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object representing Fossil Account Login Page / Modal.
 */
public class LoginPage extends BasePage {

    @FindBy(xpath = "//input[@id='login-form-email' or @name='loginEmail' or @type='email' or contains(@placeholder, 'Email')]")
    private WebElement emailInputField;

    @FindBy(xpath = "//input[@id='login-form-password' or @name='loginPassword' or @type='password' or contains(@placeholder, 'Password')]")
    private WebElement passwordInputField;

    @FindBy(xpath = "//button[@type='submit' and (contains(text(), 'Sign In') or contains(text(), 'Login') or contains(@class, 'btn-login'))]")
    private WebElement submitLoginButton;

    @FindBy(xpath = "//div[contains(@class, 'invalid-feedback') or contains(@class, 'alert-danger') or contains(@class, 'error-message') or @role='alert']")
    private WebElement errorMessageAlert;

    @FindBy(xpath = "//a[contains(@href, 'password-reset') or contains(text(), 'Forgot')]")
    private WebElement forgotPasswordLink;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage enterEmail(String email) {
        type(emailInputField, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordInputField, password);
        return this;
    }

    public void clickSubmit() {
        click(submitLoginButton);
    }

    public void loginWithCredentials(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSubmit();
    }

    public boolean isLoginFormDisplayed() {
        return isElementDisplayed(emailInputField) && isElementDisplayed(submitLoginButton);
    }

    public String getLoginErrorMessage() {
        if (isElementDisplayed(errorMessageAlert)) {
            return getElementText(errorMessageAlert);
        }
        return "";
    }
}

