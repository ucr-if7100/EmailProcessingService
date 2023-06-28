package ucr.rp.if7100.EmailProcessingService.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ucr.rp.if7100.EmailProcessingService.entities.AccountId;
import ucr.rp.if7100.EmailProcessingService.entities.Email;
import ucr.rp.if7100.EmailProcessingService.entities.Transaction;
import ucr.rp.if7100.EmailProcessingService.services.AccountIdService;
import ucr.rp.if7100.EmailProcessingService.services.EmailService;
import ucr.rp.if7100.EmailProcessingService.services.TransactionService;
import ucr.rp.if7100.EmailProcessingService.templates.BACTemplates;
import ucr.rp.if7100.EmailProcessingService.templates.BCRTemplates;
import ucr.rp.if7100.EmailProcessingService.templates.TemplateClassifier;

import java.text.ParseException;
import java.util.List;

@Component
public class TransactionJob {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AccountIdService accountIdService;

    @Scheduled(cron = "0 */2 * * * *")
    public void saveTransactionsJob(){
        try {
            List<Email> emails = emailService.getEmails();
            TemplateClassifier templateClassifier = new TemplateClassifier();
            String templateMethodName;
            Transaction transactionTemp;

            if(emails!=null){
                for (Email email: emails) {
                    templateMethodName = templateClassifier.templateClassifierTool(email.getContent());
                    transactionTemp = getTransaction(templateMethodName, email.getContent());

                    if (transactionTemp != null) {
                        boolean existingTransaction = transactionService.existsByEmailAndDateAndAmountAndReference(
                                transactionTemp.getEmail(), transactionTemp.getDate(), transactionTemp.getAmount(), transactionTemp.getReference());

                        if (!existingTransaction) {
                            AccountId existingAccountId = accountIdService.findByPhoneNumberAndLast4AndActNumber(
                                    transactionTemp.getAccountId().getPhoneNumber(),
                                    transactionTemp.getAccountId().getLast4(),
                                    transactionTemp.getAccountId().getActNumber());

                            if (existingAccountId == null) {
                                accountIdService.saveAccountId(transactionTemp.getAccountId());
                            }

                            Transaction transaction = new Transaction.Builder()
                                    .withEmail(transactionTemp.getEmail())//email
                                    .withDate(transactionTemp.getDate())//date
                                    .withAmount(transactionTemp.getAmount())//amount
                                    .withReference(transactionTemp.getReference())//reference
                                    .withDescription(transactionTemp.getDescription())//description
                                    .withCategory(transactionTemp.getCategory())//category
                                    .withTransactionType(transactionTemp.getTransactionType())//expense
                                    .withBankName(transactionTemp.getBankName())//bank
                                    .withAccountId(existingAccountId)//accountid
                                    .withRead(false)
                                    .build();

                            transactionService.saveTransaction(transaction);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error al ejecutar saveTransactionsJob", e);
        }
    }

    private Transaction getTransaction(String templateMethodName, String emailContent) throws ParseException {
        Transaction transaction = null;
        BACTemplates bacTemplates = new BACTemplates();
        BCRTemplates bcrTemplates = new BCRTemplates();

        switch (templateMethodName) {
            case "toolMailParsererSinpeTablexBac":
                transaction = bacTemplates.mailParsererSinpeTablexBac(emailContent);
                break;
            case "toolMailParsererLocalTxBac":
                transaction = bacTemplates.mailParsererLocalTxBac(emailContent);
                break;
            case "toolMailParsererSinpeBAC":
                transaction = bacTemplates.mailParsererSinpeBAC(emailContent);
                break;
            case "toolMailParsererSinpeBcr":
                transaction = bcrTemplates.mailParsererSinpeBcr(emailContent);
                break;
            default:
                break;
        }
        return transaction;
    }
}
