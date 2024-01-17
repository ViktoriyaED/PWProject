package tests;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.TestsPage;
import utils.ProjectProperties;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TestListTest extends BaseTest {

    @Test(description = "TC1344-01 The single non-active Checkbox can be checked.")
    @Description("Objective: To verify that a non-active checkbox ca be successfully checked.")
    @Story("Tests")
    @TmsLink("l3twyfx5esxv")
    @Owner("ViktoriyaD")
    public void testTutorModeWithRandomCheckboxInDomain() {
        String expectedNumberOfQuestions = "5";
        String testTutorEndPoint = "/test-tutor";

        TestsPage testsPage = new HomePage(getPage(), getPlaywright())
                .clickTestsButton()
                .handleDialogAndCancel()
                .clickDomainsButton()
                .clickRandomCheckbox()
                .clickTutorButton()
                .fillNumberOfQuestionsInputField(expectedNumberOfQuestions)
                .clickGenerateStartButton();

        waitForPageLoad(testTutorEndPoint);
        assertThat(testsPage.headerQuestionsCount).hasText("/" + expectedNumberOfQuestions);
        assertThat(getPage()).hasURL(ProjectProperties.BASE_URL + testTutorEndPoint);
    }

    @Test
    public void testTutorModeWithRandomCheckboxInChapter() {
        String expectedNumberOfQuestions = "5";
        String testTutorEndPoint = "/test-tutor";

        TestsPage testsPage = new HomePage(getPage(), getPlaywright())
                .clickTestsButton()
                .handleDialogAndCancel()
                .clickChaptersButton()
                .clickRandomCheckbox()
                .clickTutorButton()
                .fillNumberOfQuestionsInputField(expectedNumberOfQuestions)
                .clickGenerateStartButton();

        waitForPageLoad(testTutorEndPoint);
        assertThat(testsPage.headerQuestionsCount).hasText("/" + expectedNumberOfQuestions);
        assertThat(getPage()).hasURL(ProjectProperties.BASE_URL + testTutorEndPoint);
    }
}