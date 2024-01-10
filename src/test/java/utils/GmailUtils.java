package utils;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.*;

import static tests.BaseTest.log;

public class GmailUtils {

    public static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final List<String> SCOPES = List.of(GmailScopes.MAIL_GOOGLE_COM);
    public static final String APPLICATION_NAME = "PWProject";
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String USER_ID = "me";
    private static final String COMMON_EMAIL_PART = "mytestrail+";
    private static final String EMAIL_END_PART = "@gmail.com";
    private static final String QUERY = "subject:You have been invited";
    private static String googleAuthUrl;


    public static Gmail getGmailService() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        return service;
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GmailUtils.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found:" + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecret =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecret, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get(TOKENS_DIRECTORY_PATH).toFile()))
                .setAccessType("offline")
                .build();

//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
//        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");


//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, secrets, scopes)
//                .setDataStoreFactory(dataStore).build();
//
        Credential credents = flow.loadCredential("user");
        String redirect_url = null;

        // Checking if the given user is not authorized
        if (credents == null) {
            // Creating a local receiver
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
//            LocalServerReceiver receiver = new LocalServerReceiver();
            try {
                // Getting the redirect URI
                String redirectUri = receiver.getRedirectUri();

                // Creating a new authorization URL
                AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();

                // Setting the redirect URI
                authorizationUrl.setRedirectUri(redirectUri);

                // Building the authorization URL
                String url = authorizationUrl.build();

                // Logging a short message
                log.info("Creating the authorization URL : " + url);

                //This url will be fetched right after, as a button callback (target:_blank)
                //by using :FacesContext.getCurrentInstance().getExternalContext().redirect(googleAuthUrl);
                googleAuthUrl = url;


                // Receiving authorization code
                String code = receiver.waitForCode();

                // Exchanging it for an access token
                TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();

                // Storing the credentials for later access
                credents = flow.createAndStoreCredential(response, USER_ID);


            } finally {
                // Releasing resources
                receiver.stop();
            }
        }

        // Setting up the calendar service client
//        client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, jsonFactory, credents).setApplicationName(APPLICATION_NAME)
//                .build();


//        return credential;
        return credents;
    }

    public static String extractPasswordFromEmail(Gmail service, int numericPart) throws IOException {
        String email = COMMON_EMAIL_PART + numericPart + EMAIL_END_PART;
        String combinedQuery = "to:" + email + " " + QUERY;

        ListMessagesResponse response = service.users().messages().list(USER_ID).setQ(combinedQuery).execute();
        if (response.getMessages() != null && !response.getMessages().isEmpty()) {
            String messageId = response.getMessages().get(0).getId();
            Message message = service.users().messages().get("me", messageId).execute();
            String body = new String(message.getPayload().getParts().get(0).getBody().decodeData());

            String[] lines = body.split("\n");
            String passwordValue = null;

            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains("Password:")) {
                    passwordValue = lines[i + 1].trim();
                }
            }
            return passwordValue;
        } else {
            return null;
        }
    }

    public static void printLabelsInUserAccount(Gmail service) throws IOException {
        ListLabelsResponse listResponse = service.users().labels().list(USER_ID).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.isEmpty()) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        int emailNumber = 1;

        GmailUtils.extractPasswordFromEmail(getGmailService(), emailNumber);
    }
}