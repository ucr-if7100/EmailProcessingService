package ucr.rp.if7100.EmailProcessingService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ucr.rp.if7100.EmailProcessingService.entities.AccountId;
import ucr.rp.if7100.EmailProcessingService.services.AccountIdService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AccountIdServiceIntegrationTests {

    @Autowired
    private AccountIdService accountIdService;

    @Test
    public void testSaveAccountId() {
        AccountId accountId = new AccountId.Builder()
                .withPhoneNumber("12345678")
                .build();

        AccountId savedAccountId = accountIdService.saveAccountId(accountId);

        AccountId retrievedAccountId = accountIdService.getAccountIdById(savedAccountId.getId());

        assertNotNull(retrievedAccountId);
        assertEquals(savedAccountId.getId(), retrievedAccountId.getId());

        accountIdService.deleteAccountId(savedAccountId.getId());

    }

    @Test
    public void testUpdateAccountId() {
        // Crear una entidad de AccountId existente en la base de datos
        AccountId.Builder builder = new AccountId.Builder();
        AccountId accountId = builder
                .withPhoneNumber("12345678")
                .build();

        accountIdService.saveAccountId(accountId);

        // Actualizar los campos de la entidad
        accountId = builder.withPhoneNumber("87654321").build();

        AccountId updatedAccountId = accountIdService.saveAccountId(accountId);

        assertEquals("87654321", updatedAccountId.getPhoneNumber());

        accountIdService.deleteAccountId(updatedAccountId.getId());
    }

    @Test
    public void testGetAllAccountIds() {
        AccountId accountId1 = new AccountId.Builder()
                .withPhoneNumber("12345678")
                .build();

        AccountId accountId2 = new AccountId.Builder()
                .withPhoneNumber("87654321")
                .build();

        // Guardar las entidades en la base de datos
        accountIdService.saveAccountId(accountId1);
        accountIdService.saveAccountId(accountId2);

        // Obtener todas las entidades de AccountId
        List<AccountId> allAccountIds = accountIdService.getAllAccountIds();

        // Verificar que se hayan guardado correctamente todas las entidades
        assert (2 <= allAccountIds.size());
    }

    @Test
    public void testDeleteAccountId() {
        // Crear una entidad de AccountId existente en la base de datos
        AccountId accountId = new AccountId.Builder()
                .withPhoneNumber("12345678")
                .build();

        AccountId savedAccountId = accountIdService.saveAccountId(accountId);

        // Eliminar la entidad por su ID
        accountIdService.deleteAccountId(savedAccountId.getId());

        // Intentar obtener la entidad eliminada por su ID (debería ser null)
        AccountId deletedAccountId = accountIdService.getAccountIdById(savedAccountId.getId());

        assertNull(deletedAccountId);
    }

}
