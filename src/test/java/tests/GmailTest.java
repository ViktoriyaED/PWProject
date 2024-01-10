package tests;

import org.testng.annotations.Test;
import utils.GmailReceiving;
import utils.ProjectProperties;

public class GmailTest extends BaseTest {

    @Test
    public void testExtractGmailPassword() throws Exception {
//        int emailNumber = 1;
//        String expectedPassword = "nvwy602^~(";
//
//        String actualPassword = GmailUtils.extractPasswordFromEmail(GmailUtils.getGmailService(), emailNumber);
////        Assert.assertEquals(actualPassword,expectedPassword);
//        GmailUtils.printLabelsInUserAccount(GmailUtils111.getGmailService());
        GmailReceiving.getPassword(ProjectProperties.USERNAME, ProjectProperties.PASSWORD, "pw.new.test.23@gmail.com");
    }
}