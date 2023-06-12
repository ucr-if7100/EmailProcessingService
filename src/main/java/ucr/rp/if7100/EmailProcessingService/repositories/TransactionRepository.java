package ucr.rp.if7100.EmailProcessingService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucr.rp.if7100.EmailProcessingService.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
