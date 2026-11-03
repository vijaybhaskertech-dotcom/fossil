# 🐛 Azure DevOps Defect / Bug Report

**Target Project:** AIDHIKA  
**Organization:** vijay0424  
**Parent Work Item:** [User Story 442: US-442 E-Commerce Product Search, Price Verification, Add to Bag & Checkout Summary Calculation Workflow](https://dev.azure.com/vijay0424/AIDHIKA/_workitems/edit/442)  
**Target Application:** Fossil India E-Commerce (`https://www.fossil.in/`)  
**Logged By:** QA Automation Engineer  
**Date:** 03-Nov-2026  

---

## 📌 1. Bug Work Item Summary

| Field | Value |
| :--- | :--- |
| **Work Item Type** | **Bug** |
| **Work Item ID** | Linked to Parent **#442** |
| **Title** | `[BUG-442-01]: No action occurs when clicking 'Remove' button on product in Shopping Bag / Cart` |
| **Severity** | **2 - High** (Blocks user from modifying or managing cart items prior to checkout) |
| **Priority** | **1 - High** |
| **State** | **New / Active** |
| **Area Path** | `AIDHIKA\Fossil\Cart & Checkout` |
| **Iteration Path** | `AIDHIKA\Sprint 1` |
| **Found In Build** | Production Web (`https://www.fossil.in/cart`) |
| **Environment** | Chrome v131+, Edge v131+, Firefox Desktop (Resolution: 1920x1080) |

---

## 📝 2. Description & Steps to Reproduce

### 🔹 Summary
When a customer adds an item to their Shopping Bag on `fossil.in` and subsequently attempts to remove it by clicking the **"Remove"** button in the cart line-item container, the application fails to respond. No network request is initiated, no confirmation modal appears, and the item remains in the cart, preventing cart management.

### 🔹 Steps to Reproduce:
1. Navigate to the Fossil India e-commerce storefront: `https://www.fossil.in/`.
2. In the search bar, enter a keyword (e.g., *"Chronograph"*) and execute search.
3. Select any in-stock watch from the Product Listing Page (PLP) to navigate to the Product Details Page (PDP).
4. Click the **"Add to Bag"** button.
5. Click on the Shopping Bag icon in the header to open the Shopping Bag / Cart page (`https://www.fossil.in/cart`).
6. Locate the product item row and click the **"Remove"** button / icon.

---

## ⚖️ 3. Expected vs Actual Results

### ✅ Expected Result:
* The application should prompt a removal confirmation modal or immediately remove the line-item from the DOM.
* The cart badge count in the header should decrement (or show empty bag placeholder).
* The Order Summary (Subtotal, Tax, Shipping, Grand Total) should dynamically recalculate to ₹0.00.

### ❌ Actual Result:
* **No action happens when the user clicks the "Remove" button.**
* The button does not trigger any DOM update, UI loader, or API request.
* The product remains permanently in the shopping bag with unmodified pricing totals.

---

## 📹 4. Visual Evidence & Artifacts

| Artifact Name | File Path | Description |
| :--- | :--- | :--- |
| **Screen Recording (Video)** | [`docs/evidence/BUG_442_Cart_Remove_Button_No_Action.webm`](file:///c:/Users/Admin/Desktop/Fossil_Ecommerce_Automation/docs/evidence/BUG_442_Cart_Remove_Button_No_Action.webm) | Video capture demonstrating user repeatedly clicking the "Remove" button on the cart page with no system response. |

---

## 🔍 5. Root Cause Analysis (QA Perspective)
1. **Event Listener Disconnection:** During dynamic mini-cart / main-cart DOM re-rendering, the JavaScript `click` event listener attached to `.remove-product` / `.item-remove-btn` may not be bound or may have been detached.
2. **Unhandled Console Error:** An unhandled Promise rejection or null-pointer reference on cart item ID payload prevents the cart removal AJAX call from firing.
3. **Accessibility / Element Interception:** An overlay or transparent wrapper may be intercepting pointer events above the button element.

---

## 🤖 6. Automation Test Coverage
This defect has been mapped to our automated regression suite in:
* **Test Class:** [`FossilEndToEndTest.java`](file:///c:/Users/Admin/Desktop/Fossil_Ecommerce_Automation/src/test/java/com/fossil/tests/FossilEndToEndTest.java)
* **Test Method:** `testCartItemRemoveFunctionalityBugDefect()`
* **Page Object:** [`CartPage.java`](file:///c:/Users/Admin/Desktop/Fossil_Ecommerce_Automation/src/main/java/com/fossil/pages/CartPage.java) (`clickRemoveButton()`, `isItemRemoved()`)

