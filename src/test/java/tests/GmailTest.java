package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.GmailUtils;

public class GmailTest extends BaseTest {

    @Test
    public void testExtractGmailPassword() throws Exception {
        int emailNumber = 1;
        String expectedPassword = "nvwy602^~(";

        String actualPassword = GmailUtils.extractPasswordFromEmail(GmailUtils.getGmailService(), emailNumber);
        Assert.assertEquals(actualPassword,expectedPassword);
    }
}