package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.Random;

abstract class BasePage {
    private final Playwright playwright;
    private final Page page;

    protected BasePage(Page page, Playwright playwright) {
        this.playwright = playwright;
        this.page = page;
    }

    protected Playwright getPlaywright() {
        return playwright;
    }

    protected Page getPage() {
        return page;
    }

    public static int getRandomNumber(Locator list) {
        if (list.count() == 0) {
            return 0;
        }
        System.out.println("List size = " + list.count());
        int random = new Random().nextInt(1, list.count());
        System.out.println("randomInt = " + random);
        return random;
    }

    public static void clickRandomElement(Locator list) {
        int randomValue = getRandomNumber(list);
        list.nth(randomValue).click();
    }
}