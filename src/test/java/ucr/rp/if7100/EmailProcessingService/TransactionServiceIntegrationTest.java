package ucr.rp.if7100.EmailProcessingService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ucr.rp.if7100.EmailProcessingService.entities.AccountId;
import ucr.rp.if7100.EmailProcessingService.entities.Transaction;
import ucr.rp.if7100.EmailProcessingService.enums.TransactionType;
import ucr.rp.if7100.EmailProcessingService.services.AccountIdService;
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
    private AccountIdService accountIdService;

    @Test
    public void testSaveTransaction() {
        AccountId accountId = new AccountId.Builder()
                .withPhoneNumber("123456789")
                .build();
        accountIdService.saveAccountId(accountId);
        Transaction transaction = new Transaction.Builder()
                .withEmail("example@example.com")
                .withDate(new Date(2023, 6, 20))
                .withAmount(1000.00f)
                .withReference("REF123")
                .withDescription("Example transaction")
                .withCategory("Example category")
                .withBankName("BAC")
                .withTransactionType(TransactionType.EXPENSE)
                .withAccountId(accountId)
                .withRead(false)
                .build();

        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        Transaction retrievedTransaction = transactionService.getTransactionById(savedTransaction.getId());

        assertNotNull(retrievedTransaction);
        assertEquals(savedTransaction.getEmail(), retrievedTransaction.getEmail());

        transactionService.deleteTransaction(savedTransaction.getId());
    }

    @Test
    public void testUpdateTransaction() {


        AccountId accountId = new AccountId.Builder()
                .withPhoneNumber("123456789")
                .build();
        accountIdService.saveAccountId(accountId);
        Transaction.Builder builder = new Transaction.Builder();
        Transaction transaction = builder
                .withEmail("example@example.com")
                .withDate(new Date(2023, 6, 20))
                .withAmount(100.0f)
                .withReference("REF123")
                .withDescription("Example transaction")
                .withCategory("Example category")
                .withTransactionType(TransactionType.EXPENSE)
                .withBankName("BAC")
                .withAccountId(accountId)
                .withRead(false)
                .build();

        // Guardar la transacción
        transactionService.saveTransaction(transaction);

        // Actualizar los campos de la transacción
        builder.withAmount(200.0f);
        transaction = builder.build();

        Transaction updatedTransaction = transactionService.saveTransaction(transaction);

        assertEquals(200.0f, updatedTransaction.getAmount());

        transactionService.deleteTransaction(updatedTransaction.getId());

    }

    @Test
    public void testGetAllTransactions() {

        //Crear varias transacciones
        AccountId accountId = new AccountId.Builder()
                .withPhoneNumber("123456789")
                .build();
        accountIdService.saveAccountId(accountId);
        Transaction transaction1 = new Transaction.Builder()
                .withEmail("example@example.com")
                .withDate(new Date(2023, 6, 20))
                .withAmount(100.0f)
                .withReference("REF123")
                .withDescription("Example transaction")
                .withCategory("Example category")
                .withTransactionType(TransactionType.EXPENSE)
                .withBankName("BCR")
                .withAccountId(accountId)
                .withRead(false)
                .build();

        transactionService.saveTransaction(transaction1);

        Transaction transaction2 = new Transaction.Builder()
                .withEmail("example@example.com")
                .withDate(new Date(2023, 6, 20))
                .withAmount(100.0f)
                .withReference("REF123")
                .withDescription("Example transaction")
                .withCategory("Example category")
                .withTransactionType(TransactionType.EXPENSE)
                .withBankName("BAC")
                .withAccountId(accountId)
                .withRead(false)
                .build();

        transactionService.saveTransaction(transaction2);

        List<Transaction> allTransactions = transactionService.getAllTransactions();

        assertTrue(allTransactions.size() >= 2);

        transactionService.deleteTransaction(transaction1.getId());
        transactionService.deleteTransaction(transaction2.getId());

    }

    @Test
    public void testDeleteTransaction() {
        // Crear una transacción
        AccountId accountId = new AccountId.Builder()
                .withPhoneNumber("123456789")
                .build();
        accountIdService.saveAccountId(accountId);
        Transaction transaction = new Transaction.Builder()
                .withEmail("example@example.com")
                .withDate(new Date(2023, 6, 20))
                .withAmount(100.0f)
                .withReference("REF123")
                .withDescription("Example transaction")
                .withCategory("Example category")
                .withTransactionType(TransactionType.EXPENSE)
                .withBankName("BCR")
                .withAccountId(accountId)
                .withRead(false)
                .build();

        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        // Eliminar la transacción por su ID
        transactionService.deleteTransaction(savedTransaction.getId());

        // Intentar obtener la transacción eliminada por su ID (debería ser null)
        Transaction deletedTransaction = transactionService.getTransactionById(savedTransaction.getId());

        assertNull(deletedTransaction);
    }

}


