package tests;

import base.BaseTest;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomeTest extends BaseTest {

    @Test
    public void testLoginNavigation() {

        assertThat(page).hasURL("https://study.traineracademy.org/home");
        assertThat(page).hasTitle("Trainer Academy");
    }

    @DataProvider
    public Object[][] sideMenuItems() {
        return new Object[][]{
                {"Home", "https://study.traineracademy.org/home"},
                {"Study guide", "https://study.traineracademy.org/study-guide"},
                {"Tests", "https://study.traineracademy.org/test-list"},
                {"Flashcards", "https://study.traineracademy.org/flashcard-packs"},
                {"Mnemonic cards", "https://study.traineracademy.org/mnemoniccard-list"},
                {"Performance", "https://study.traineracademy.org/performance"},
                {"Profile", "https://study.traineracademy.org/profile"}
        };
    }

    @Test(dataProvider = "sideMenuItems")
    void testNavigateToSubMenuItems(String locator, String expectedUrl) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(locator)).click();

        assertThat(page).hasURL(expectedUrl);
    }
}
