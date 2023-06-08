package ucr.rp.if7100.EmailProcessingService;

//import org.junit.jupiter.api.Test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ucr.rp.if7100.EmailProcessingService.entities.AccountId;
import ucr.rp.if7100.EmailProcessingService.entities.Bank;
import ucr.rp.if7100.EmailProcessingService.entities.Transaction;
import ucr.rp.if7100.EmailProcessingService.services.AccountIdService;
import ucr.rp.if7100.EmailProcessingService.services.BankService;
import ucr.rp.if7100.EmailProcessingService.services.TransactionService;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class TransactionServiceIntegrationTest {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private BankService bankService;
    @Autowired
    private AccountIdService accountIdService;

    @Test
    public void testSaveTransaction() {
        Transaction transaction = new Transaction();
        // Configurar los atributos de la transacción
        transaction.setEmail("example@example.com");
        transaction.setDate(new Date(2023, 6, 20));
        transaction.setAmount(100.0f);
        transaction.setReference("REF123");
        transaction.setDescription("Example transaction");
        transaction.setCategory("Example category");
        transaction.setExpense(true);

        Bank bank = new Bank();
        bank.setName("Bank 1");
        bank = bankService.saveBank(bank);

        AccountId accountId = new AccountId();
        accountId.setPhoneNumber("123456789");
        accountId = accountIdService.saveAccountId(accountId);

        transaction.setBank(bank);
        transaction.setAccountId(accountId);

        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        Transaction retrievedTransaction = transactionService.getTransactionById(savedTransaction.getEmail());

        assertNotNull(retrievedTransaction);
        assertEquals(savedTransaction.getEmail(), retrievedTransaction.getEmail());

        transactionService.deleteTransaction(savedTransaction.getEmail());
    }

        @Test
        public void testUpdateTransaction() {
            Transaction transaction = new Transaction();
            // Configurar los atributos de la transacción
            transaction.setEmail("example@example.com");
            transaction.setDate(new Date(2023, 6, 20));
            transaction.setAmount(100.0f);
            transaction.setReference("REF123");
            transaction.setDescription("Example transaction");
            transaction.setCategory("Example category");
            transaction.setExpense(true);

            Bank bank = new Bank();
            bank.setName("Bank 1");
            bank = bankService.saveBank(bank);

            AccountId accountId = new AccountId();
            accountId.setPhoneNumber("123456789");
            accountId = accountIdService.saveAccountId(accountId);

            transaction.setBank(bank);
            transaction.setAccountId(accountId);

            Transaction savedTransaction = transactionService.saveTransaction(transaction);

            // Actualizar los campos de la transacción
            transaction.setAmount(200.0f);

            Transaction updatedTransaction = transactionService.saveTransaction(transaction);

            assertEquals(200.0f, updatedTransaction.getAmount());

            transactionService.deleteTransaction(updatedTransaction.getEmail());
        }

        @Test
        public void testGetAllTransactions() {
            // Crear varias transacciones
            Transaction transaction = new Transaction();
            // Configurar los atributos de la transacción
            transaction.setEmail("example@example.com");
            transaction.setDate(new Date(2023, 6, 20));
            transaction.setAmount(100.0f);
            transaction.setReference("REF123");
            transaction.setDescription("Example transaction");
            transaction.setCategory("Example category");
            transaction.setExpense(true);

            Bank bank = new Bank();
            bank.setName("Bank 1");
            bank = bankService.saveBank(bank);

            AccountId accountId = new AccountId();
            accountId.setPhoneNumber("123456789");
            accountId = accountIdService.saveAccountId(accountId);

            transaction.setBank(bank);
            transaction.setAccountId(accountId);

            transactionService.saveTransaction(transaction);

            transaction = new Transaction();
            // Configurar los atributos de la transacción
            transaction.setEmail("example2@example.com");
            transaction.setDate(new Date(2023, 6, 20));
            transaction.setAmount(100.0f);
            transaction.setReference("REF123");
            transaction.setDescription("Example transaction");
            transaction.setCategory("Example category");
            transaction.setExpense(true);


            bank = bankService.saveBank(bank);
            accountId = accountIdService.saveAccountId(accountId);

            transaction.setBank(bank);
            transaction.setAccountId(accountId);

            transactionService.saveTransaction(transaction);

            List<Transaction> allTransactions = transactionService.getAllTransactions();

            assertTrue(allTransactions.size() >= 2);

        }

        @Test
        public void testDeleteTransaction() {
            // Crear una transacción existente en la base de datos
            Transaction transaction = new Transaction();
            // Configurar los atributos de la transacción
            transaction.setEmail("example@example.com");
            transaction.setDate(new Date(2023, 6, 20));
            transaction.setAmount(100.0f);
            transaction.setReference("REF123");
            transaction.setDescription("Example transaction");
            transaction.setCategory("Example category");
            transaction.setExpense(true);

            Bank bank = new Bank();
            bank.setName("Bank 1");
            bank = bankService.saveBank(bank);

            AccountId accountId = new AccountId();
            accountId.setPhoneNumber("123456789");
            accountId = accountIdService.saveAccountId(accountId);

            transaction.setBank(bank);
            transaction.setAccountId(accountId);

            Transaction savedTransaction = transactionService.saveTransaction(transaction);

            // Eliminar la transacción por su ID
            transactionService.deleteTransaction(savedTransaction.getEmail());

            // Intentar obtener la transacción eliminada por su ID (debería ser null)
            Transaction deletedTransaction = transactionService.getTransactionById(savedTransaction.getEmail());

            assertNull(deletedTransaction);
        }

}


