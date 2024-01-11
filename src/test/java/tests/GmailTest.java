package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.GmailUtils;

public class GmailTest {

    @Test
    public void testExtractGmailPasswordOauth2() throws Exception {
        int emailNumber = 10;
        String expectedPassword = "LwUG097!@!";

        String actualPassword = GmailUtils.extractPasswordFromEmail(GmailUtils.getGmailService(), emailNumber);
        Assert.assertEquals(actualPassword,expectedPassword);
    }
}