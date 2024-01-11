package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.GmailReceiver;
import utils.GmailUtils;
import utils.ProjectProperties;

public class GmailTest extends BaseTest {

    @Test
    public void testExtractGmailPassword() throws Exception {
        int emailNumber = 1;
        String expectedPassword = "nvwy602^~(";

        String actualPassword = GmailUtils.extractPasswordFromEmail(GmailUtils.getGmailService(), emailNumber);
        Assert.assertEquals(actualPassword,expectedPassword);
//        GmailReceiver.getPassword("pwtester.new@gmail.com", "kbtb ozhw zkjl nkoi", "pw.new.test.23@gmail.com");
    }
}