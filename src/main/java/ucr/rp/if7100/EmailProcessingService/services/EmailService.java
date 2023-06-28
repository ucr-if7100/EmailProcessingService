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
            emailFolder.open(Folder.READ_WRITE);

            List<Email> emails = convertToEmails(List.of(emailFolder.getMessages()));
            emailRepository.saveAll(emails);

            emailFolder.close(true);
            store.close();

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
                message.setFlag(Flags.Flag.SEEN, true);
            } catch (MessagingException | IOException ex) {
                System.err.println("Error on email extraction");
            }
        }

        return emails;
    }
    private Email convertToEmail(Message message) throws MessagingException, IOException {
        Email email = new Email.Builder()
                .withSubject(message.getSubject())
                .withFromAddress(InternetAddress.toString(message.getFrom()))
                .withToAddress(InternetAddress.toString(message.getRecipients(Message.RecipientType.TO)))
                .withSentDate(message.getSentDate().toString())
                .build();

        Object contentObject = message.getContent();

        if (contentObject instanceof Multipart multipart) {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/html") || bodyPart.isMimeType("text/plain")) {
                    email.setContent((String) bodyPart.getContent());
                } else if (bodyPart.isMimeType("multipart/alternative")) {
                    email.setContent(extractHtml((Multipart) bodyPart.getContent()));
                }
            }
        }

        return email;
    }

    private String extractHtml(Multipart multipart) throws MessagingException, IOException {
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart1 = multipart.getBodyPart(i);
            if (bodyPart1.isMimeType("text/html")) {
                return (String) bodyPart1.getContent();
            }
        }
        return null;
    }

    public List<Email> getEmails(){
        return  emailRepository.findAll();
    }
}
