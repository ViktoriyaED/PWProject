package tests;

import org.testng.annotations.Test;
import pages.HomePage;
import pages.TestsPage;

public class TestsTest extends BaseTest {

    @Test
    public void testLocators() {
        HomePage homePage = new HomePage(getPage(), getPlaywright());
        homePage.testsButton.click();

        TestsPage testsPage = new TestsPage(getPage(), getPlaywright());
        testsPage.chaptersButton.click();
        testsPage.domainsButton.click();
        testsPage.checkboxLocator("Sint voluptatem ...").click();
        testsPage.checkboxLocator("Fuga id mollitia ...").click();
        testsPage.checkboxLocator("Voluptate nulla r...").click();
        testsPage.selectAllButton.click();
        testsPage.timedButton.click();
        testsPage.tutorButton.click();
        testsPage.numberOfQuestionsTextbox.fill("5");
        testsPage.numberOfQuestionsTextbox.clear();
        testsPage.generateStartButton.click();
    }
}