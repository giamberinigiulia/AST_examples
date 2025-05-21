package testing.example.bank;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BankAccountTest {

	@Test
	void testIdIsAutomaticallyAssignedAsPositiveNumber() {
		// setup
		BankAccount bankAccount = new BankAccount();
		// verify
		assertThat(bankAccount.getId()).isPositive();
	}

	@Test
	void testIdsAreIncremental() {
		assertThat(new BankAccount().getId())
			.isLessThan(new BankAccount().getId());
	}

	@Test
	void testDepositWhenAmountIsCorrectShouldIncreaseBalance() {
		// setup
		BankAccount bankAccount = new BankAccount();
		bankAccount.setBalance(5);
		// exercise
		bankAccount.deposit(10);
		// verify
		assertThat(bankAccount.getBalance()).isEqualTo(15);
		// or with offset
		assertThat(bankAccount.getBalance())
			.isCloseTo(14.9, byLessThan(0.1));
	}

	@Test
	void testDepositWhenAmountIsNegativeShouldThrow() {
		BankAccount bankAccount = new BankAccount();
		assertThatThrownBy(() -> bankAccount.deposit(-1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Negative amount: -1.0");
		// further assertions after the exception is thrown
		assertThat(bankAccount.getBalance()).isZero();
	}

	@Test
	void testWithdrawWhenAmountIsNegativeShouldThrow() {
		BankAccount bankAccount = new BankAccount();
		assertThatThrownBy(() -> bankAccount.withdraw(-1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Negative amount: -1.0");
		// further assertions after the exception is thrown
		assertThat(bankAccount.getBalance()).isZero();
	}

	@Test
	void testWithdrawWhenBalanceIsUnsufficientShouldThrow() {
		BankAccount bankAccount = new BankAccount();
		assertThatThrownBy(() -> bankAccount.withdraw(10))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Cannot withdraw 10.0 from 0.0");
		assertThat(bankAccount.getBalance()).isZero();
	}

	@Test
	void testWithdrawWhenBalanceIsSufficientShouldDecreaseBalance() {
		// setup
		BankAccount bankAccount = new BankAccount();
		bankAccount.setBalance(10);
		// exercise
		bankAccount.withdraw(3); // the method we want to test
		// verify
		assertThat(bankAccount.getBalance()).isEqualTo(7);
	}

}