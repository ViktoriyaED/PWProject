package tests;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ExceptionListener;
import utils.ProjectProperties;
import utils.ReportUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

@Listeners(ExceptionListener.class)
public abstract class BaseTest {
    private final Playwright playwright = Playwright.create();
    private Browser browser;
    private BrowserContext context;
    private Page page;
    public static Logger log = LogManager.getLogger();

    @BeforeSuite
    protected void beforeSuite() {
        log.info(ReportUtils.getReportHeader());
    }

    @BeforeClass
    protected void launchBrowser(ITestContext testContext) {
        switch (ProjectProperties.BROWSER_NAME) {
            case "chromium" -> this.browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(ProjectProperties.IS_HEADLESS).setSlowMo(ProjectProperties.IS_SLOW));
            case "firefox" -> this.browser = playwright.firefox().launch(
                    new BrowserType.LaunchOptions().setHeadless(ProjectProperties.IS_HEADLESS).setSlowMo(ProjectProperties.IS_SLOW));
            case "safari" -> this.browser = playwright.webkit().launch(
                    new BrowserType.LaunchOptions().setHeadless(ProjectProperties.IS_HEADLESS).setSlowMo(ProjectProperties.IS_SLOW));
            case "chrome" -> this.browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(ProjectProperties.IS_HEADLESS).setSlowMo(ProjectProperties.IS_SLOW));
            default -> System.out.println("Please enter the right browser name...");
        }
    }

    @BeforeMethod
    protected void createContextAndPage(Method method) {
        log.info("RUN " + this.getClass().getName() + "." + method.getName());
        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(ProjectProperties.WIDTH, ProjectProperties.HEIGHT)
                .setRecordVideoDir(Paths.get("videos/"))
                .setRecordVideoSize(1280, 720)
        );

        context.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
        page = context.newPage();
        log.info("CONTEXT AND PAGE CREATED");

        page.navigate(ProjectProperties.BASE_URL);
        login();
        log.info("LOGIN SUCCESSFUL");
    }

    @AfterMethod
    protected void closeContext(Method method, ITestResult testResult) throws IOException {
        log.info(ReportUtils.getTestStatistics(method, testResult));
        Tracing.StopOptions tracingStopOptions = null;
        String classMethodName = this.getClass().getName() + method.getName();

        if (!testResult.isSuccess()) {
            Allure.getLifecycle().addAttachment("screenshot", "image/png", "png",
                    page.screenshot(new Page.ScreenshotOptions()
                            .setFullPage(true))
            );
        }

        page.close();

        if (!testResult.isSuccess()) {
            tracingStopOptions = new Tracing.StopOptions()
                    .setPath(Paths.get("testTracing/" + classMethodName + ".zip"));
            log.info("Tracing saved");

            page.video().saveAs(Paths.get("videos/" + classMethodName + ".webm"));
            log.info("Video saved");
            page.video().delete();
        } else {
            page.video().delete();
        }

        if (!testResult.isSuccess()) {
            Allure.getLifecycle().addAttachment("video", "videos/webm", "webm",
                    Files.readAllBytes(Paths.get("videos/" + classMethodName + ".webm")));
            log.info("Video added to Allure report");

            Allure.getLifecycle().addAttachment("tracing", "archive/zip", "webm",
                    Files.readAllBytes(Paths.get("testTracing/" + classMethodName + ".zip")));
            log.info("Tracing added to Allure report");
        }

        context.tracing().stop(
                tracingStopOptions
        );

        context.close();
        log.info("CONTEXT AND PAGE CLOSED" + ReportUtils.END_LINE);
    }

    @AfterClass
    protected void closeBrowser() {
        browser.close();
        log.info("BROWSER CLOSED");
    }

    @AfterSuite
    protected void closePlaywright() {
        playwright.close();
        log.info("PLAYWRIGHT CLOSED");
    }

    private void login() {
        page.locator("//span[text()='Email']/../div/input").fill(ProjectProperties.USERNAME);
        page.locator("//input[@type='password']").fill(ProjectProperties.PASSWORD);
        page.locator("//button[@type='submit']").click();
    }

    public Page getPage() {
        return page;
    }

    public Playwright getPlaywright() {
        return playwright;
    }

    public void waitForPageLoad(String endPoint) {
        getPage().waitForURL(ProjectProperties.BASE_URL + endPoint);
    }
}