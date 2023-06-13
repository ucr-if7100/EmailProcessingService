package ucr.rp.if7100.EmailProcessingService.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ucr.rp.if7100.EmailProcessingService.services.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class EmailJob {

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 */1 * * * *")
    public void saveEmailsJob(){
        try {
            emailService.getInboxEmails();
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error al ejecutar saveEmailsJob", e);
        }
    }
}