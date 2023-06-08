package ucr.rp.if7100.EmailProcessingService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucr.rp.if7100.EmailProcessingService.entities.Bank;
import ucr.rp.if7100.EmailProcessingService.repositories.BankRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BankService {
    private final BankRepository bankRepository;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    public Bank getBankById(Long id) {
        Optional<Bank> optionalBank = bankRepository.findById(id);
        return optionalBank.orElse(null);
    }

    public Bank saveBank(Bank bank) {
        return bankRepository.save(bank);
    }

    public void deleteBank(Long id) {
        bankRepository.deleteById(id);
    }
}

