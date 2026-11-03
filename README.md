# ⌚ Fossil India E-Commerce Test Automation Suite

A production-grade, enterprise-ready Automated Testing Framework built with **Java 17/21**, **Selenium WebDriver**, **TestNG**, and **ExtentReports 5**, adhering to the **Page Object Model (POM)** design pattern.

---

## 🎯 Requirements Coverage Matrix

| Core Requirement | Implementation Details | Target Classes |
| :--- | :--- | :--- |
| **1. Dynamic Navigation** | Browser is initialized dynamically (Chrome, Firefox, Edge) with stealth anti-bot parameters, maximized window, and navigated to `https://www.fossil.in/`. | `BaseTest.java`, `config.properties` |
| **2. Product Flow** | Searches or locates specific watch (e.g. *Chronograph*), verifies product listing prices, navigates to PDP, asserts price > 0, and clicks "Add to Bag". | `HomePage.java`, `ProductListingPage.java`, `ProductDetailsPage.java` |
| **3. Cart Verification** | Navigates to Shopping Bag, asserts item count updates accurately, and verifies checkout summary pricing formula (`Subtotal + Shipping + Tax == Grand Total`). | `CartPage.java`, `FossilEndToEndTest.java` |
| **Page Object Model (POM)** | Strict decoupling of UI locators (`@FindBy private WebElement`) from test assertions and business interactions. | `com.fossil.pages.*` |
| **Synchronization** | **Zero** `Thread.sleep()`. Robust `WebDriverWait` with `ExpectedConditions` (`visibilityOf`, `elementToBeClickable`). | `BasePage.java`, `BaseTest.java` |
| **Assertions** | Balanced use of Hard Assertions (`Assert.assertEquals`) for critical guards and Soft Assertions (`SoftAssert`) for full checkout calculations. | `FossilEndToEndTest.java` |
| **Reporting & Screenshots** | Automated **ExtentReports 5** HTML reports with Base64 failure screenshots embedded via TestNG `TestListener`. | `TestListener.java`, `ExtentManager.java` |

---

## 📁 Project Directory Layout

```
Fossil_Ecommerce_Automation/
├── pom.xml                               # Maven project dependencies & plugins
├── testng.xml                            # TestNG execution suite with listeners
├── README.md                             # Project documentation
├── AZURE_DEVOPS_BUG_REPORT.md            # Comprehensive Bug Report for US-442
├── AZURE_DEVOPS_USER_STORY_AND_TEST_CASES.md # User Story #442 & 10 Test Cases
├── AZURE_DEVOPS_TEST_CASES.csv           # Azure DevOps bulk-import CSV (Story + Tests + Bug)
├── docs/
│   └── evidence/
│       └── BUG_442_Cart_Remove_Button_No_Action.webm # Bug video screen recording proof
├── src/
│   ├── main/
│   │   └── java/com/fossil/
│   │       ├── base/
│   │       │   └── BaseTest.java         # Driver setup, ThreadLocal, teardown
│   │       ├── pages/
│   │       │   ├── BasePage.java         # Root page, explicit waits, price parser
│   │       │   ├── HomePage.java         # Search, header, overlays
│   │       │   ├── LoginPage.java        # Email/password form, validations
│   │       │   ├── ProductListingPage.java # Search results & price extractors
│   │       │   ├── ProductDetailsPage.java # PDP prices, Add to Bag
│   │       │   └── CartPage.java         # Shopping bag counts & total verification
│   │       └── utils/
│   │           ├── ConfigReader.java     # config.properties reader
│   │           ├── ExtentManager.java    # ExtentReports 5 singleton instance
│   │           ├── ScreenshotUtils.java  # Base64 & file screenshot utility
│   │           └── TestListener.java     # TestNG listener for logging & screenshots
│   └── test/
│       ├── java/com/fossil/tests/
│       │   ├── FossilEndToEndTest.java   # Complete core E2E purchase flow
│       │   ├── FossilLoginTest.java      # Authentication & form tests
│       │   └── FossilSearchProductTest.java # Search & product listing tests
│       └── resources/
│           ├── config.properties         # Browser, URLs, and timeout settings
│           └── testng.xml                # TestNG suite runner
```

---

## 🚀 How to Run the Tests

### 1. Run Complete Test Suite via Maven
Open PowerShell or Command Prompt in this folder and run:
```bash
mvn clean test
```

### 2. Run with Cross-Browser Selection
```bash
# Run on Chrome
mvn test -Dbrowser=chrome

# Run on Edge
mvn test -Dbrowser=edge

# Run on Firefox
mvn test -Dbrowser=firefox
```

### 3. Run in Headless Mode (CI/CD)
```bash
mvn test -Dheadless=true
```

---

## 📊 Viewing HTML Test Reports

After test execution, the interactive HTML Extent Report is automatically generated at:
```
test-output/ExtentReport.html
```
Open this file in Google Chrome or any browser to view test status, step logs, execution timelines, and failure screenshots.

---

## 🐛 Defect Tracking & Bug Report

* **Azure DevOps User Story:** [User Story 442 (AIDHIKA)](https://dev.azure.com/vijay0424/AIDHIKA/_workitems/edit/442)
* **Defect ID:** `[BUG-442-01]`
* **Title:** No action occurs when clicking 'Remove' button on product in Shopping Bag / Cart
* **Severity / Priority:** High (P1)
* **Detailed Documentation:** [`AZURE_DEVOPS_BUG_REPORT.md`](AZURE_DEVOPS_BUG_REPORT.md)
* **Attached Video Evidence:** [`docs/evidence/BUG_442_Cart_Remove_Button_No_Action.webm`](docs/evidence/BUG_442_Cart_Remove_Button_No_Action.webm)
* **Automated Defect Verification:** Covered in `FossilEndToEndTest.testCartItemRemoveDefectVerification()`


