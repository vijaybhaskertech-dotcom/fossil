package com.fossil.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.fossil.base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG ITestListener implementation that hooks into test execution events,
 * logs milestones into ExtentReports, and captures screenshots on test failure.
 * Thread-safe for parallel runs using ThreadLocal.
 */
public class TestListener implements ITestListener {

    private static final ExtentReports extent = ExtentManager.getInstance();
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return testThread.get();
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("========== Starting Suite: " + context.getName() + " ==========");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("========== Suite Finished: " + context.getName() + " ==========");
        if (extent != null) {
            extent.flush();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description == null || description.isEmpty()) {
            description = testName;
        }

        ExtentTest test = extent.createTest(testName, description);
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        testThread.set(test);
        test.log(Status.INFO, "Executing Test: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (testThread.get() != null) {
            testThread.get().log(Status.PASS, MarkupHelper.createLabel(result.getMethod().getMethodName() + " PASSED", ExtentColor.GREEN));
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (testThread.get() != null) {
            testThread.get().log(Status.FAIL, MarkupHelper.createLabel(result.getMethod().getMethodName() + " FAILED", ExtentColor.RED));
            testThread.get().log(Status.FAIL, result.getThrowable());

            Object currentClass = result.getInstance();
            if (currentClass instanceof BaseTest) {
                WebDriver driver = ((BaseTest) currentClass).getDriver();
                if (driver != null) {
                    String base64Screenshot = ScreenshotUtils.captureScreenshotAsBase64(driver);
                    if (base64Screenshot != null) {
                        testThread.get().fail("Failure Screenshot Captured:",
                                MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                    }
                }
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (testThread.get() != null) {
            testThread.get().log(Status.SKIP, MarkupHelper.createLabel(result.getMethod().getMethodName() + " SKIPPED", ExtentColor.ORANGE));
            if (result.getThrowable() != null) {
                testThread.get().log(Status.SKIP, result.getThrowable());
            }
        }
    }
}

