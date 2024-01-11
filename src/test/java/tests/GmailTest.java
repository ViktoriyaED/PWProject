package tests;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.GmailUtils;

@Listeners(utils.ExceptionListener.class)
public class GmailTest {

    @Test
    public void testExtractGmailPasswordOauth2() throws Exception {
        String expectedPassword = "LwUG097!@!";

        String actualPassword = GmailUtils.extractPasswordFromEmail(GmailUtils.getGmailService(), 10);
        Assert.assertEquals(actualPassword,expectedPassword);
    }
}