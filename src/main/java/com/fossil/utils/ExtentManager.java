package com.fossil.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

/**
 * Singleton manager for ExtentReports 5 initialization and report generation.
 */
public class ExtentManager {

    private static ExtentReports extent;

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = ConfigReader.getProperty("reportPath");
            if (reportPath == null || reportPath.trim().isEmpty()) {
                reportPath = "test-output/ExtentReport.html";
            }

            File reportFile = new File(reportPath);
            File parentDir = reportFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle(ConfigReader.getProperty("reportTitle"));
            sparkReporter.config().setReportName(ConfigReader.getProperty("reportName"));
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            extent.setSystemInfo("Application", "Fossil India E-Commerce");
            extent.setSystemInfo("Target URL", ConfigReader.getProperty("baseUrl"));
            extent.setSystemInfo("Operating System", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
        }
        return extent;
    }
}

