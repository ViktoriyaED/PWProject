package base;

import com.microsoft.playwright.*;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Properties;


public class BaseTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    protected Page page;
    protected Properties properties;


    @BeforeClass
    protected void launchBrowser() {
        init_properties();
        final String browserName = properties.getProperty("browser").toLowerCase().trim();

        playwright = Playwright.create();

        switch (browserName) {
            case "chromium" -> browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false));
            case "firefox" -> browser = playwright.firefox().launch(
                    new BrowserType.LaunchOptions().setHeadless(false));
            case "safari" -> browser = playwright.webkit().launch(
                    new BrowserType.LaunchOptions().setHeadless(false));
            case "chrome" -> browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
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

    public void init_properties() {
        try {
            FileInputStream ip = new FileInputStream("./src/test/resources/config.properties");
            properties = new Properties();
            properties.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void login() {

    }
}