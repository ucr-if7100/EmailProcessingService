package ucr.rp.if7100.EmailProcessingService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucr.rp.if7100.EmailProcessingService.entities.Transaction;

import java.util.Date;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    boolean existsByEmailAndDateAndAmountAndReference(String email, Date date, float amount, String reference);

}
