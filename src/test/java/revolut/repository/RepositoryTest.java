package revolut.repository;

import com.revolut.challenge.exception.InvalidRequestException;
import com.revolut.challenge.model.Account;
import com.revolut.challenge.repository.InMemoryRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Fazel on 12/12/2019.
 */
public class RepositoryTest {

    private InMemoryRepository repository;

    @Before
    public void init() {
        repository = new InMemoryRepository();
    }

    @Test
    public void createNewAccountAndStoreInMemory_whenCorrectDataGiven() {
        Account givenAccount = createSampleAccount(100, 5000, "Fazel Farnia");
        repository.createAccount(givenAccount);
    }

    @Test(expected = InvalidRequestException.class)
    public void createNewAccountAndStoreInMemory_whenInvalidDataGiven() {
        Account givenAccount = createSampleAccount(100, 0, "Fazel Farnia");
        repository.createAccount(givenAccount);
    }

    @Test
    public void updateAccountInRepository_whenAccountNumberIsSameAndDifferentOtherValue() {
        Account beforeUpdateAccount = createSampleAccount(100, 4000, "Fazel Farnia");
        repository.createAccount(beforeUpdateAccount);
        Account afterUpdateAccountGiven = createSampleAccount(100, 555, "Zaman");
        repository.createAccount(afterUpdateAccountGiven);
        Account expectedAccountAfterUpdate = repository.loadByAccountNumber(beforeUpdateAccount.getAccountNumber());
        assertEquals(expectedAccountAfterUpdate, afterUpdateAccountGiven);

    }

    @Test
    public void checkRepositoryIsEmpty_whenNoAccountAdded() {
        assertTrue(repository.loadAllAccounts().isEmpty());
    }

    @Test
    public void checkRepositoryItems_whenAccountsAdded() {
        repository.createAccount(createSampleAccount(100, 5000, "Fazel Farnia"));
        repository.createAccount(createSampleAccount(200, 6000, "Reza Koosha"));
        List<Account> accountList = repository.loadAllAccounts();
        assertFalse(accountList.isEmpty());
        assertEquals(2, accountList.size());
        assertEquals(100, accountList.get(0).getAccountNumber().intValue());
        assertEquals(6000, accountList.get(1).getBalance().intValue());
    }

    @Test
    public void loadAccountFromRepository_whenCorrectAccountNumberGiven() {
        Account givenAccount = createSampleAccount(300, 7000, "Fazel Farnia");
        repository.createAccount(givenAccount);
        Account expectedAccount = repository.loadByAccountNumber(givenAccount.getAccountNumber());
        assertEquals(expectedAccount, givenAccount);
    }

    @Test
    public void loadAccountFromRepository_whenInvalidAccountNumberGiven() {
        Account expectedAccount = repository.loadByAccountNumber(444);
        assertNull(expectedAccount);
    }

    private Account createSampleAccount(Integer accountNumber, Integer balance, String ownerName) {
        Account account = new Account();
        account.setBalance(balance);
        account.setAccountNumber(accountNumber);
        account.setOwnerName(ownerName);
        return account;
    }
}
