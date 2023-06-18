package ucr.rp.if7100.EmailProcessingService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucr.rp.if7100.EmailProcessingService.entities.Transaction;
import ucr.rp.if7100.EmailProcessingService.events.Event;
import ucr.rp.if7100.EmailProcessingService.events.TransactionCreatedEvent;
import ucr.rp.if7100.EmailProcessingService.repositories.TransactionRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    @Value("${kafka.topic}")
    private String topic;
    private final TransactionRepository transactionRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final KafkaTemplate<String, Event<?>> kafkaTemplate;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, ApplicationEventPublisher eventPublisher, KafkaTemplate<String, Event<?>> kafkaTemplate) {
        this.transactionRepository = transactionRepository;
        this.eventPublisher = eventPublisher;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(String id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction.orElse(null);
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    //public void deleteTransaction(Long id) {
    public void deleteTransaction(String id) {
        transactionRepository.deleteById(id);
    }


    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        Transaction createdTransaction= transactionRepository.save(transaction);
        TransactionCreatedEvent transactionCreatedEvent = new TransactionCreatedEvent();
        transactionCreatedEvent.setId(UUID.randomUUID().toString());
        transactionCreatedEvent.setCorrelationalId(UUID.randomUUID().toString());
        transactionCreatedEvent.setDate(new Date());
        transactionCreatedEvent.setPayload(transaction);
        eventPublisher.publishEvent(transactionCreatedEvent);
        return createdTransaction;
    }

    @EventListener
    public void handlePersonCreated(TransactionCreatedEvent event) {
        TransactionCreatedEvent transactionCreatedEvent = event;
        // Enviar el objeto Person a Kafka
        kafkaTemplate.send(topic, transactionCreatedEvent);
    }
}
