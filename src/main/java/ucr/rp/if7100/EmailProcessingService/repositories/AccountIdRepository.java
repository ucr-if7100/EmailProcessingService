package ucr.rp.if7100.EmailProcessingService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ucr.rp.if7100.EmailProcessingService.entities.AccountId;

public interface AccountIdRepository extends JpaRepository<AccountId, String> {
    AccountId findByPhoneNumberAndLast4AndActNumber(String phone, String last4, String actNumber);
}
