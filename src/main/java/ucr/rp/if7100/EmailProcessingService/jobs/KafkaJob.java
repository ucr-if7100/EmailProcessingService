package ucr.rp.if7100.EmailProcessingService.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ucr.rp.if7100.EmailProcessingService.entities.Transaction;
import ucr.rp.if7100.EmailProcessingService.services.TransactionService;

import java.util.List;

@Component
public class KafkaJob {

    @Autowired
    private TransactionService transactionService;

    @Scheduled(cron = "0 */2 * * * *")
    public void sendTransactionJob(){
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();

            if(transactions!=null){

                for (Transaction transaction : transactions) {
                    if(!transaction.isReadStatus()){
                        transactionService.publishEvent(transaction);
                        markReadTransaction(transaction);
                    }
                }
            }
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error al ejecutar sendTransactionJob", e);
        }
    }

    public void markReadTransaction(Transaction transaction){
        transaction.setReadStatus(true);
        transactionService.saveTransaction(transaction);
    }
}
