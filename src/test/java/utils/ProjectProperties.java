package utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ProjectProperties {

    private static final String ENV_BROWSER_OPTIONS = "BROWSER_OPTIONS";
    private static final String ENV_WEB_OPTIONS = "WEB_OPTIONS";
    public static Properties properties = init_properties();
    public static final String BROWSER_NAME = properties.getProperty("browser").trim();
    public static final boolean IS_HEADLESS = Boolean.parseBoolean(properties.getProperty("headless").trim());
    public static final double IS_SLOW = Double.parseDouble(properties.getProperty("slowMo").trim());
    public static final int WIDTH = Integer.parseInt(properties.getProperty("width"));
    public static final int HEIGHT = Integer.parseInt(properties.getProperty("height"));
    public static final String BASE_URL = properties.getProperty("base_url");
    public static final String USERNAME = properties.getProperty("username").trim();
    public static final String PASSWORD = properties.getProperty("password").trim();
    public static final String COMMON_EMAIL_PART = properties.getProperty("commonEmailPart").trim();

    private static Properties init_properties() {
        if (properties == null) {
            properties = new Properties();
            if (isServerRun()) {

                if (System.getenv(ENV_BROWSER_OPTIONS) != null) {
                    for (String option : System.getenv(ENV_BROWSER_OPTIONS).split(";")) {
                        String[] browserOptionArr = option.split("=");
                        properties.setProperty(browserOptionArr[0], browserOptionArr[1]);
                    }
                }

                if (System.getenv(ENV_WEB_OPTIONS) != null) {
                    for (String option : System.getenv(ENV_WEB_OPTIONS).split(";")) {
                        String[] webOptionArr = option.split("=");
                        properties.setProperty(webOptionArr[0], webOptionArr[1]);
                    }
                }

            } else {
                try {
                    FileInputStream fileInputStream = new FileInputStream("./src/test/resources/config.properties");
                    if (fileInputStream == null) {
                        System.out.println("ERROR: The \u001B[31mconfig.properties\u001B[0m file not found.");
                        System.out.println("You need to create it from config.properties.TEMPLATE file.");
                        System.exit(1);
                    }
                    properties.load(fileInputStream);
                } catch (IOException ignore) {
                }
            }
        }
        return properties;
    }

    public static boolean isServerRun() {
        return System.getenv("CI_RUN") != null;
    }

    static void captureScreenFile(Page page, String methodName, String className) {
        byte[] screenshotBytes = page.screenshot();

        Allure.getLifecycle().addAttachment(
                "screenshot", "image/png", "png", screenshotBytes
        );

        try {
            File screenshotFile = new File(String.format("screenshots/%s-%s.png", className, methodName));
            page.screenshot(new Page.ScreenshotOptions().setPath(screenshotFile.toPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
