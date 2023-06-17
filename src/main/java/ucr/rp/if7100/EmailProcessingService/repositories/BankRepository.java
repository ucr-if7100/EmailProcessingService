package ucr.rp.if7100.EmailProcessingService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucr.rp.if7100.EmailProcessingService.entities.Bank;

public interface BankRepository extends JpaRepository<Bank, String> {
}
