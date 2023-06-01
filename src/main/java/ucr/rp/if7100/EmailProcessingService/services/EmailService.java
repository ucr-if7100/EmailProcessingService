package ucr.rp.if7100.EmailProcessingService.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ucr.rp.if7100.EmailProcessingService.entities.Email;
import ucr.rp.if7100.EmailProcessingService.repositories.EmailRepository;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class EmailService {
    @Value("${custom.mail-host}")
    private String host;
    @Value("${custom.mail-user}")
    private String mailUser;
    @Value("${custom.mail-password}")
    private String mailPassword;
    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public List<Email> getInboxEmails() {
        try {
            Properties properties = buildProperties(this.host);
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("pop3s");
            store.connect(host, mailUser, mailPassword);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            List<Email> emails = convertToEmails(List.of(emailFolder.getMessages()));

            emailRepository.saveAll(emails);

            return emails;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private Properties buildProperties(String host) {
        Properties properties = new Properties();

        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");

        return properties;
    }

    private List<Email> convertToEmails(List<Message> messages) {
        List<Email> emails = new ArrayList<>();
        for (Message message : messages) {
            try {
                emails.add(convertToEmail(message));
            } catch (MessagingException | IOException ex) {
                System.err.println("Error on email extraction");
            }
        }

        return emails;
    }
    private Email convertToEmail(Message message) throws MessagingException, IOException {
        Email email = new Email();

        email.setSubject(message.getSubject());
        email.setFromAddress(InternetAddress.toString(message.getFrom()));
        email.setToAddress(InternetAddress.toString(message.getRecipients(Message.RecipientType.TO)));
        email.setSentDate(message.getSentDate().toString());

        Object contentObject = message.getContent();

        if (contentObject instanceof Multipart multipart) {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/html") || bodyPart.isMimeType("text/plain")) {
                    email.setContent((String) bodyPart.getContent());
                    System.out.println("Content: " + bodyPart.getContent());
                }
            }
        }

        return email;
    }
}
