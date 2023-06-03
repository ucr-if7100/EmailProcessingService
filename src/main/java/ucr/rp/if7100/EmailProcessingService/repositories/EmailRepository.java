package ucr.rp.if7100.EmailProcessingService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucr.rp.if7100.EmailProcessingService.entities.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> { }
