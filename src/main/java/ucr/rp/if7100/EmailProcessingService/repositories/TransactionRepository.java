package ucr.rp.if7100.EmailProcessingService.repositories;

import ucr.rp.if7100.EmailProcessingService.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
