package ucr.rp.if7100.EmailProcessingService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ucr.rp.if7100.EmailProcessingService.entities.Bank;
import ucr.rp.if7100.EmailProcessingService.services.BankService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class BankServiceIntegrationTests {

    @Autowired
    private BankService bankService;

    @Test
    public void testSaveBank() {
        Bank bank = new Bank.Builder()
                .withName("Bank 1")
                .build();

        Bank savedBank = bankService.saveBank(bank);

        Bank retrievedBank = bankService.getBankById(savedBank.getId());

        assertNotNull(retrievedBank);
        assertEquals(savedBank.getId(), retrievedBank.getId());
        assertEquals("Bank 1", retrievedBank.getName());

        bankService.deleteBank(savedBank.getId());
    }

    @Test
    public void testUpdateBank() {
        Bank.Builder builder = new Bank.Builder();
        Bank bank = builder
                .withName("Bank 1")
                .build();

        bankService.saveBank(bank);

        bank = builder
                .withName("Updated Bank")
                .build();

        Bank updatedBank = bankService.saveBank(bank);

        assertEquals("Updated Bank", updatedBank.getName());

        bankService.deleteBank(updatedBank.getId());
    }

    @Test
    public void testGetAllBanks() {
        Bank bank1 = new Bank.Builder()
                .withName("Bank 1")
                .build();

        Bank bank2 = new Bank.Builder()
                .withName("Bank 2")
                .build();

        bankService.saveBank(bank1);
        bankService.saveBank(bank2);

        List<Bank> allBanks = bankService.getAllBanks();

        assert(2 <= allBanks.size());
    }

    @Test
    public void testDeleteBank() {
        Bank bank = new Bank.Builder()
                .withName("Bank 1")
                .build();

        Bank savedBank = bankService.saveBank(bank);

        bankService.deleteBank(savedBank.getId());

        Bank deletedBank = bankService.getBankById(savedBank.getId());

        assertNull(deletedBank);
    }
}

