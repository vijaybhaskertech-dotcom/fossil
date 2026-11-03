package com.fossil.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility for capturing full page and Base64 screenshots for failure diagnosis.
 */
public class ScreenshotUtils {

    public static String captureScreenshotAsBase64(WebDriver driver) {
        if (driver == null) {
            return null;
        }
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            System.err.println("Could not capture Base64 screenshot: " + e.getMessage());
            return null;
        }
    }

    public static String captureScreenshotAsFile(WebDriver driver, String screenshotName) {
        if (driver == null) {
            return null;
        }
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String destPath = "test-output/screenshots/" + screenshotName + "_" + timestamp + ".png";
            File destination = new File(destPath);
            FileUtils.copyFile(source, destination);
            return destination.getAbsolutePath();
        } catch (IOException e) {
            System.err.println("Failed to save screenshot file: " + e.getMessage());
            return null;
        }
    }
}

