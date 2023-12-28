package base;

import com.microsoft.playwright.Page;

public abstract class BasePage {
    private Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    protected Page getPage() {
        return page;
    }
}
