package utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FromTerm;
import java.io.IOException;
import java.util.Properties;


public class GmailReceiver {

    private static Properties setPropertiesForMail(String email, String password){
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.host", "imap.gmail.com");
        properties.put("mail.debug", "false");
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.user", email);
        properties.put("mail.imap.password", password);

        return properties;
    }

    public static void getPassword(String email, String password, String fromEmailFilter) throws MessagingException, IOException {
        System.out.println(email + " " + password);
        Properties properties = setPropertiesForMail(email, password);
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        Store store = session.getStore("imaps");
        Folder folder = null;

        try {
            store.connect("imap.gmail.com", email, password);

            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            Message[] messages = folder.search(new FromTerm(new InternetAddress(fromEmailFilter)));

            if (messages.length > 0) {
                // Get the latest message
                Message message = messages[messages.length - 1];
//                System.out.println("Subject: " + message.getSubject());
                Object content = message.getContent();

                if (content instanceof Multipart) {
                    Multipart multipart = (Multipart) content;

                    // Iterate through the parts of the Multipart
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart bodyPart = multipart.getBodyPart(i);

                        // Check if the part is text/plain
                        if (bodyPart.isMimeType("text/plain")) {
                            String text = (String) bodyPart.getContent();
//                            System.out.println("Content: " + text);
                        }
                    }
                }
//
            // If the content is a multipart message, iterate through its parts
            Multipart multipart = (Multipart) content;

            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);

                // Check if the current part is a text/plain part
                if (bodyPart.isMimeType("text/plain")) {
                    String text = (String) bodyPart.getContent();
                    String[] lines = text.split("\n");

                    // Search for the line containing "Password:"
                    for (int j = 0; j < lines.length; j++) {
                        if (lines[j].contains("Password:")) {
                            // Get the password from the next line
                            String passwordValue = lines[j + 1].trim();
                            System.out.println(passwordValue);
                            break;  // Exit the loop when the password is found
                        }
                    }
                }
            }
            } else {
                System.out.println("No messages from " + fromEmailFilter);
            }


        } finally {
            // Close the folder and store in a finally block to ensure they are closed even if an exception occurs
            if (folder != null && folder.isOpen()) {
                folder.close(false);
            }

            if (store.isConnected()) {
                store.close();
            }
        }
    }
}

