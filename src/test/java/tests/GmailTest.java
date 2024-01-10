package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.GmailUtils;
import utils.GmailUtils111;

public class GmailTest extends BaseTest {

    @Test
    public void testExtractGmailPassword() throws Exception {
//        int emailNumber = 1;
//        String expectedPassword = "nvwy602^~(";
//
//        String actualPassword = GmailUtils.extractPasswordFromEmail(GmailUtils.getGmailService(), emailNumber);
//        Assert.assertEquals(actualPassword,expectedPassword);
        GmailUtils.printLabelsInUserAccount(GmailUtils111.getGmailService());
    }
}