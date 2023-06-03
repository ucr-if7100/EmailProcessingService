package ucr.rp.if7100.EmailProcessingService.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucr.rp.if7100.EmailProcessingService.entities.Email;
import ucr.rp.if7100.EmailProcessingService.services.EmailService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmailController {
    final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/emails/")
    public List<Email> emails() {
        return emailService.getInboxEmails();
    }
}
