package ucr.rp.if7100.EmailProcessingService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ucr.rp.if7100.EmailProcessingService.entities.Email;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class EmailProcessingServiceApplicationTests {

    @Test
    public void testCreateEmail() {
        Email email = new Email.Builder()
                .withSubject("Subject")
                .withFromAddress("from@mail.com")
                .withToAddress("to@mail.com")
                .withSentDate("23/08/2023")
                .withContent("Some content...")
                .build();

        assertEquals(email.getId(), UUID.fromString(email.getId()).toString());
        assertEquals(email.getSubject(), "Subject");
        assertEquals(email.getFromAddress(), "from@mail.com");
        assertEquals(email.getToAddress(), "to@mail.com");
        assertEquals(email.getSentDate(), "23/08/2023");
        assertEquals(email.getContent(), "Some content...");
    }
}
