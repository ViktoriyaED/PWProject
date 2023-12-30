package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class TestsPage extends BaseLocator {

    public Locator domainsButton = text("Domains");
    public Locator chaptersButton = text("Chapters");
    public Locator selectAllButton = button("Select all");
    public Locator tutorButton = button("Tutor");
    public Locator timedButton = button("Timed");
    public Locator numberOfQuestionsTextbox = textbox("numberOfQuestions");
    public Locator generateStartButton = button("Generate & Start");

    public TestsPage(Page page, Playwright playwright) {
        super(page, playwright);
    }

    public Locator checkboxLocator(String checkboxText) {
        return text(checkboxText);
    }
}