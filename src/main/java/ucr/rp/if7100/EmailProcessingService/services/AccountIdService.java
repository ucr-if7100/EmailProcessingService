package ucr.rp.if7100.EmailProcessingService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucr.rp.if7100.EmailProcessingService.entities.AccountId;
import ucr.rp.if7100.EmailProcessingService.repositories.AccountIdRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccountIdService {
    private final AccountIdRepository accountIdRepository;

    @Autowired
    public AccountIdService(AccountIdRepository accountIdRepository) {
        this.accountIdRepository = accountIdRepository;
    }

    public List<AccountId> getAllAccountIds() {
        return accountIdRepository.findAll();
    }

    public AccountId getAccountIdById(Long id) {
        Optional<AccountId> optionalAccountId = accountIdRepository.findById(id);
        return optionalAccountId.orElse(null);
    }

    public AccountId saveAccountId(AccountId accountId) {
        return accountIdRepository.save(accountId);
    }

    public void deleteAccountId(Long id) {
        accountIdRepository.deleteById(id);
    }
}

