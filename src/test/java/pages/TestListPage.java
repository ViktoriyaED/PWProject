package pages;

import com.microsoft.playwright.Dialog;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

public class TestListPage extends BaseLocator {

    public Locator domainsButton = text("Domains");
    public Locator chaptersButton = text("Chapters");
    public Locator selectAllButton = button("Select all");
    public Locator tutorButton = button("Tutor");
    public Locator timedButton = button("Timed");
    public Locator generateAndStartButton = button("Generate & Start");
    public Locator numberOfQuestionsInputField = getPage().locator("input[name = 'numberOfQuestions']");
    public Locator listCheckboxes = generateListOfElements("//div//form/div/div[3]//button");

    public TestListPage(Page page, Playwright playwright) {
        super(page, playwright);
    }

    public TestListPage clickDomainsButton() {
        if (!domainsButton.isChecked()) {
            domainsButton.click();
        }
        return this;
    }

    public TestListPage clickChaptersButton() {
        chaptersButton.click();
        return this;
    }

    public TestListPage clickTutorButton() {
        tutorButton.click();
        return this;
    }

    public TestListPage fillNumberOfQuestionsInputField(String number) {
        numberOfQuestionsInputField.fill(number);
        return this;
    }

    public TestsPage clickGenerateStartButton() {
        generateAndStartButton.click();
        return new TestsPage(getPage(), getPlaywright());
    }


    public TestListPage handleDialogAndCancel() {
        if (getPage().getByRole(AriaRole.DIALOG).isVisible()) {
            getPage().onDialog(Dialog::dismiss);
            getPage().getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cancel")).click();
        }
        return this;
    }

    public TestListPage clickRandomCheckbox() {
        BasePage.clickRandomElement(listCheckboxes);
        return this;
    }
}