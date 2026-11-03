# 📋 Azure DevOps Work Item #442: User Story & Complete Test Cases Suite

**Project:** AIDHIKA  
**Organization:** vijay0424  
**Work Item ID:** 442  
**Target Application:** Fossil India E-Commerce (https://www.fossil.in/)  
**Document Author:** QA Automation Engineer  

---

## 🌟 1. User Story Details (Work Item #442)

### 📌 Title:
> **US-442: E-Commerce Product Search, Price Verification, Add to Bag & Checkout Summary Calculation Workflow**

### 📝 User Story Description (Agile / Scrum Format):
```gherkin
As an Online Shopper on Fossil India (fossil.in),
I want to navigate the portal, search for specific watches, inspect verified product pricing, add items to my shopping bag, and review the checkout order summary calculations,
So that I can smoothly purchase my desired product with accurate pricing, transparent taxes/shipping, and reliable item tracking.
```

---

### ✅ Acceptance Criteria (AC):

- **AC-1 (Dynamic Navigation):**
  - **Given** the user triggers the test suite with a designated browser parameter (Chrome / Firefox / Edge),
  - **When** the browser session launches,
  - **Then** the application shall navigate to `https://www.fossil.in/` without bot blockades and render the header, brand logo, and navigation bar.

- **AC-2 (Account & Login Interface):**
  - **Given** the user is on the homepage,
  - **When** the user clicks on the Account/Login icon,
  - **Then** the login form/modal must display Email/Phone and Submit fields with proper validation feedback for incorrect credentials.

- **AC-3 (Product Search & Listing):**
  - **Given** the user is on the store homepage,
  - **When** the user enters a search keyword (e.g., *"Chronograph"*) and submits,
  - **Then** the Product Listing Page (PLP) must load relevant watch cards with visible, non-empty prices formatted in INR (₹).

- **AC-4 (Product Detail Page & Price Consistency):**
  - **Given** the user is browsing the search results,
  - **When** the user clicks on the first product card,
  - **Then** the Product Details Page (PDP) shall load with matching product title and a valid price (> 0).

- **AC-5 (Add to Bag / Cart):**
  - **Given** the user is on the PDP with an in-stock product,
  - **When** the user clicks the "Add to Bag" / "Add to Cart" button,
  - **Then** the item shall be added to the shopping bag, and the cart badge count must increment.

- **AC-6 (Shopping Bag & Item Count Verification):**
  - **Given** the product is added,
  - **When** the user navigates to the Shopping Bag (`/cart`),
  - **Then** the shopping bag page must list the added product and the line-item count must reflect the updated count (>= 1).

- **AC-7 (Checkout Summary Pricing Verification):**
  - **Given** the user is on the Shopping Bag page,
  - **When** inspecting the Order Summary section,
  - **Then** the final calculation must hold true:
    $$\text{Subtotal} + \text{Estimated Shipping} + \text{Estimated Tax} = \text{Grand Total}$$

- **AC-8 (Technical & Automation Compliance):**
  - The automation test suite must implement the **Page Object Model (POM)**, use **Explicit Waits** (no hardcoded sleeps), optimized relative locators, and generate **ExtentReports 5** HTML reports with failure screenshots.

---

## 🧪 2. Detailed Test Cases Suite (Matching Work Item #442)

| TC ID | Test Case Title | Priority | Test Type | Pre-conditions | Test Steps | Test Data | Expected Result |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_442_01** | Verify dynamic browser launch and homepage navigation | High (P1) | Smoke / Navigation | Browser installed, network active | 1. Initialize WebDriver dynamically.<br>2. Navigate to `https://www.fossil.in/`.<br>3. Verify current URL and Brand logo presence. | `browser=chrome`, `url=https://www.fossil.in/` | Browser launches, Fossil homepage renders, URL contains 'fossil', and brand logo is displayed. |
| **TC_442_02** | Verify Account Login navigation and form element visibility | High (P1) | Functional | Homepage is loaded | 1. Click Account/Sign in icon.<br>2. Verify email input field display.<br>3. Verify submit/login button display. | N/A | Login modal/page opens with interactive input fields. |
| **TC_442_03** | Verify invalid user credentials trigger validation feedback | Medium (P2) | Negative / Security | User on Login page | 1. Enter invalid email.<br>2. Enter dummy password.<br>3. Click Submit/Login button. | `email: test_invalid@test.com`<br>`password: WrongPass@999` | System shows error feedback; user is not authenticated. |
| **TC_442_04** | Verify Product Search with keyword returns matching results | High (P1) | Functional | Homepage is loaded | 1. Click search field.<br>2. Enter keyword 'Chronograph'.<br>3. Press Enter key.<br>4. Count product tiles. | `keyword = "Chronograph"` | PLP loads with search results; product count >= 1. |
| **TC_442_05** | Verify Product Listing Page displays products with valid prices | High (P1) | Functional | Search results loaded | 1. Inspect first product card.<br>2. Extract product price string.<br>3. Verify price is non-empty and > ₹0. | First product tile | Price text is visible, contains currency symbol, and numeric value > 0. |
| **TC_442_06** | Verify navigating from PLP to PDP maintains title and price | High (P1) | Functional / E2E | PLP is displayed | 1. Click on first product link.<br>2. Wait for PDP to load.<br>3. Verify PDP title is displayed.<br>4. Verify PDP price > 0. | First product link | PDP renders; product title and price match the selected item. |
| **TC_442_07** | Verify clicking "Add to Bag" adds product and updates badge | High (P1) | Functional / E2E | PDP is loaded | 1. Verify "Add to Bag" button is enabled.<br>2. Click "Add to Bag".<br>3. Observe cart badge counter / notification. | Selected watch item | Product added to bag; notification/mini-cart appears; badge count updates. |
| **TC_442_08** | Verify Shopping Bag page item count accuracy | High (P1) | Functional / E2E | Item added to bag | 1. Click Shopping Bag icon.<br>2. Navigate to `/cart`.<br>3. Count line-items in the cart. | Cart URL | Cart page displays at least 1 item matching the added product. |
| **TC_442_09** | Verify Order Summary pricing formula in Shopping Bag | High (P1) | Functional / E2E | User on Cart page | 1. Extract Subtotal.<br>2. Extract Shipping Cost.<br>3. Extract Tax (if applicable).<br>4. Extract Grand Total.<br>5. Validate: `Subtotal + Shipping + Tax == Grand Total`. | Cart Line-item prices | Mathematical calculation is verified with 100% accuracy (no pricing discrepancy). |
| **TC_442_10** | [Negative] Verify Search with non-existent keyword | Low (P3) | Negative | Homepage is loaded | 1. Enter random string.<br>2. Submit search.<br>3. Verify 'No results found' message. | `keyword = "xyz999nonsenseabc"` | Zero products displayed; friendly 'No matching products' message shown. |

---

## 🔗 3. Traceability to Automation Code

Every single Acceptance Criteria and Test Case is mapped directly to our automation codebase:

- **TC_442_01** ➔ `BaseTest.java` (`setUp()` with dynamic browser)
- **TC_442_02 & TC_442_03** ➔ `FossilLoginTest.java` (`testLoginPageDisplay`, `testInvalidLoginCredentials`)
- **TC_442_04 & TC_442_05** ➔ `FossilSearchProductTest.java` (`testSearchFunctionality`, `testNavigateToProductDetails`)
- **TC_442_06, 07, 08, 09** ➔ `FossilEndToEndTest.java` (`testCompleteECommerceProductPurchaseFlow`)

---

## 🐛 4. Linked Defect / Bug Report

### **[BUG-442-01]: No Action Occurs When User Clicks 'Remove' Button in Shopping Bag**
- **Parent User Story:** [User Story 442](https://dev.azure.com/vijay0424/AIDHIKA/_workitems/edit/442)
- **Work Item Type:** Bug
- **Severity:** 2 - High
- **Priority:** 1 - High
- **State:** Active / New
- **Steps to Reproduce:**
  1. Navigate to `https://www.fossil.in/`
  2. Search and add any watch to the Shopping Bag
  3. Navigate to `/cart`
  4. Click the "Remove" button on the line item
- **Expected Result:** Item is removed from the cart, line-item count decrements, order total recalculates.
- **Actual Result:** No action occurs upon clicking "Remove". Product remains in cart and pricing totals do not update.
- **Evidence Attached:** [`docs/evidence/BUG_442_Cart_Remove_Button_No_Action.webm`](file:///c:/Users/Admin/Desktop/Fossil_Ecommerce_Automation/docs/evidence/BUG_442_Cart_Remove_Button_No_Action.webm)
- **Automated Regression Test:** `FossilEndToEndTest.testCartItemRemoveDefectVerification()`


