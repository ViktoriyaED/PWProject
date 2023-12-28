package base;

import com.microsoft.playwright.*;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;


public class BaseTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    protected Page page;
    protected static Properties properties;

    private static final String ENV_WEB_OPTIONS = "WEB_OPTIONS";

    private static final String ENV_BROWSER_OPTIONS = "BROWSER_OPTIONS";


    @BeforeClass
    protected void launchBrowser() {

        init_properties();

        final String browserName = properties.getProperty("browser").trim();
        final boolean isHeadless = Boolean.parseBoolean(properties.getProperty("headless").trim());
        final double isSlow = Double.parseDouble(properties.getProperty("slowMo").trim());

        playwright = Playwright.create();

        switch (browserName) {
            case "chromium" -> browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(isHeadless).setSlowMo(isSlow));
            case "firefox" -> browser = playwright.firefox().launch(
                    new BrowserType.LaunchOptions().setHeadless(isHeadless).setSlowMo(isSlow));
            case "safari" -> browser = playwright.webkit().launch(
                    new BrowserType.LaunchOptions().setHeadless(isHeadless).setSlowMo(isSlow));
            case "chrome" -> browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(isHeadless).setSlowMo(isSlow));
            default -> System.out.println("Please enter the right browser name...");
        }
    }

    @BeforeMethod
    protected void createContextAndPage() {
        int width = Integer.parseInt(properties.getProperty("width"));
        int height = Integer.parseInt(properties.getProperty("height"));

        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(width, height));

        context.tracing().start(
                new Tracing.StartOptions()

                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );

        page = context.newPage();
        login();
    }

    @AfterMethod
    protected void closeContext(Method method, ITestResult testResult) {
        Tracing.StopOptions tracingStopOptions = null;
        String classMethodName = this.getClass().getName() + method.getName();
        if (!testResult.isSuccess()) {
            tracingStopOptions = new Tracing.StopOptions()
                    .setPath(Paths.get("testTracing/" + classMethodName + ".zip"));
        }
        context.tracing().stop(
                tracingStopOptions
        );

        context.close();
    }

    @AfterClass
    protected void closeBrowser() {
        browser.close();
        playwright.close();
    }

//    public void init_properties() {
//        try {
//            FileInputStream ip = new FileInputStream("./src/test/resources/config.properties");
//            properties = new Properties();
//            properties.load(ip);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static void init_properties() {
        if (properties == null) {
            properties = new Properties();
            if (isServerRun()) {
                System.out.println("***************************Ci runs*******************************************");

                if (System.getenv(ENV_BROWSER_OPTIONS) != null) {
                    for (String option : System.getenv(ENV_BROWSER_OPTIONS).split(";")) {
                        String[] browserOptionArr = option.split("=");
                        properties.setProperty(browserOptionArr[0], browserOptionArr[1]);
                        System.out.println("***************************browser_options*******************************************");
                        System.out.println(Arrays.toString(browserOptionArr));
//                        System.out.println(properties);
                    }
                }

                if (System.getenv(ENV_WEB_OPTIONS) != null) {
                    for (String option : System.getenv(ENV_WEB_OPTIONS).split(";")) {
                        String[] webOptionArr = option.split("=");
                        properties.setProperty(webOptionArr[0], webOptionArr[1]);
                        System.out.println("***************************WEB_options*******************************************");
                        System.out.println(Arrays.toString(webOptionArr));
//                        System.out.println(properties);
                    }
                }
                System.out.println(properties);

            } else {
                try {
                    InputStream inputStream = BaseTest.class.getClassLoader().getResourceAsStream("config.properties");
                    if (inputStream == null) {
                        System.out.println("ERROR: The \u001B[31mconfig.properties\u001B[0m file not found.");
                        System.out.println("You need to create it from config.properties.TEMPLATE file.");
                        System.exit(1);
                    }
                    properties.load(inputStream);
                } catch (IOException ignore) {
                }
            }
        }
    }

    static boolean isServerRun() {
        return System.getenv("CI_RUN") != null;
    }

    private void login() {
        final String baseUrl = properties.getProperty("url").toString();
        final String username = properties.getProperty("username");
//        final String password = properties.getProperty("password");
        final String password = properties.getProperty("password");

        page.navigate(baseUrl);
        page.locator("//span[text()='Email']/../div/input").fill(username);
        page.locator("//input[@type='password']").fill(password);
        page.locator("//button[@type='submit']").click();
    }
}