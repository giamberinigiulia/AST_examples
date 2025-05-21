package testing.example.bank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BankTest {

	private Bank bank;

	// the collaborator of Bank that we manually instrument and inspect
	private List<BankAccount> bankAccounts;

	@BeforeEach
	void setup() {
		bankAccounts = new ArrayList<BankAccount>();
		bank = new Bank(bankAccounts);
	}

	@Test
	@DisplayName("openNewBankAccount should store a new account with a positive id")
	void testOpenNewAccount() {
		int newAccountId = bank.openNewBankAccount(0);
		assertAll("open new account",
			() -> assertThat(newAccountId).isPositive(),
			() -> assertThat(bankAccounts).
					hasSize(1).
					extracting(BankAccount::getId).
					contains(newAccountId)
		);
	}

	@Test
	void testDepositWhenAccountIsNotFoundShouldThrow() {
		assertThatThrownBy(() -> bank.deposit(1, 10))
			.isInstanceOf(NoSuchElementException.class)
			.hasMessage("No account found with id: 1");
	}

	@Test
	void testDepositWhenAccountIsFoundShouldIncrementBalance() {
		// setup
		BankAccount testAccount = createTestAccount(10);
		bankAccounts.add(testAccount);
		// exercise
		bank.deposit(testAccount.getId(), 5);
		// verify
		assertThat(testAccount.getBalance()).isEqualTo(15);
	}

	@Test
	void testWithdrawWhenAccountIsNotFoundShouldThrow() {
		assertThatThrownBy(() -> bank.withdraw(1, 10))
			.isInstanceOf(NoSuchElementException.class)
			.hasMessage("No account found with id: 1");
	}

	@Test
	void testWithdrawWhenAccountIsFoundShouldDecrementBalance() {
		// setup
		BankAccount testAccount = createTestAccount(10);
		bankAccounts.add(testAccount);
		// exercise
		bank.withdraw(testAccount.getId(), 5);
		// verify
		assertThat(testAccount.getBalance()).isEqualTo(5);
	}

	/**
	 * Utility method for creating a BankAccount for testing.
	 */
	private BankAccount createTestAccount(double initialBalance) {
		BankAccount bankAccount = new BankAccount();
		bankAccount.setBalance(initialBalance);
		return bankAccount;
	}
}