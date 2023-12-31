import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.TestsPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomeTest extends BaseTest {

    @Test
    public void testLoginNavigation() {

        assertThat(getPage()).hasURL(getBaseUrl() + "/home");
//        assertThat(getPage()).hasTitle("Trainer Academy");
    }

    @DataProvider
    public Object[][] sideMenuItems() {
        return new Object[][]{
                {"Home", getBaseUrl() + "/home"},
                {"Study guide", getBaseUrl() + "/study-guide"},
                {"Tests", getBaseUrl() + "/test-list"},
                {"Flashcards", getBaseUrl() + "/flashcard-packs"},
                {"Mnemonic cards", getBaseUrl() + "/mnemoniccard-list"},
                {"Performance", getBaseUrl() + "/performance"},
                {"Profile", getBaseUrl() + "/profile"}
        };
    }

    @Test(dataProvider = "sideMenuItems")
    public void testNavigateToSubMenuItems(String locator, String expectedUrl) {
        getPage().getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(locator)).click();

        assertThat(getPage()).hasURL(expectedUrl);
    }

    @Test
    public void testLocators() {
        HomePage homePage = new HomePage(getPage(), getPlaywright());
        assertThat(homePage.studyThisButton).isVisible();
        homePage.studyThisButton.click();
    }

    @Test
    public void testLocators1() {
        HomePage homePage = new HomePage(getPage(), getPlaywright());
        homePage.testsButton.click();

        TestsPage testsPage = new TestsPage(getPage(), getPlaywright());
        testsPage.chaptersButton.click();
        testsPage.domainsButton.click();
        testsPage.checkboxLocator("Body composition").click();
        testsPage.checkboxLocator("Exercise Metabolism").click();
        testsPage.selectAllButton.click();
        testsPage.timedButton.click();
        testsPage.tutorButton.click();
        testsPage.numberOfQuestionsTextbox.fill("5");
        testsPage.numberOfQuestionsTextbox.clear();
        testsPage.generateStartButton.click();
    }
}