package org.example.java;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ATMTest {

    private User user;
    private Bank bank;
    private ATM atm;

    //set ups for bank and user mocks
    @BeforeEach
    void setup() {
        //mocks of bank and user
        bank = mock(Bank.class);
        //user = mock(User.class);
        user = new User("Bailey", "9999", 500.00); //mock user and balance
        //when(user.getBalance()).thenReturn(500.00);
        //when(user.getPin()).thenReturn("9999");
        //when(user.getId()).thenReturn("Bailey");
        when(bank.getUserById("Bailey")).thenReturn(user); //return when the user is fetched by id
        when(bank.verifyPin(user, "9999")).thenReturn(true); // mock correct PIN validation

        atm = new ATM(bank); //passing the mock bank to atm

    }

    //test to see that getUserById returns the correct user
    @Test
    @DisplayName("Test getUserById returns correct user")
    public void testGetUserById() {
       // user = bank.getUserById("Bailey");
        assertNotNull(user, "user should not be null");
        System.out.println(user.getId());
        assertEquals("Bailey", user.getId(), "user should have the id Bailey");
    }


    //no user is found
    @Test
    @DisplayName("Test if atm handles user not found by user id")
    public void testUserNotFound() {
        when(bank.getUserById("nobody")).thenReturn(null);

        assertFalse(atm.insertCard("nobody"), "there should be no user with this name");
        assertFalse(atm.enterPin("9999"), "user should not be able to log in if user id doesnt exist");
    }


    //checking if a locked card is inserted
    @Test
    @DisplayName("Test if inserting locked card")
    public void isLockedCardInserted() {
        when(bank.isCardLocked("Bailey")).thenReturn(true); //mocking the behavior if the card is locked
        assertFalse(atm.insertCard("Bailey"),"card should be locked and insertion should not work" );
        System.out.println("card locked for user " + user.getId() + " : " +  bank.isCardLocked("Bailey"));
    }

    //checking if pin entered is correct
    @Test
    @DisplayName("Test if the pin is correct")
    public void isPinCorrect() {
        assertTrue(atm.insertCard("Bailey"), "card insert should be successful");
        assertTrue(atm.enterPin("9999"), "pin should be correct");
        assertNotNull(atm.getCurrentUser(), "user should be logged in after entering the correct pin");
        assertEquals("Bailey", atm.getCurrentUser().getId(), "Logged in user should be Bailey");
    }
       // boolean loggedIn = atm.enterPin("9999");
       // assertTrue(loggedIn, "login should be successful with right pin code");


    //checking if pin is incorrect
    @Test
    @DisplayName("Test if the pin in incorrect")
    public void isPinIncorrect() {
        assertTrue (atm.insertCard("Bailey"), "Should be correct user");
        assertFalse(atm.enterPin("8888"), "login should fail with wrong pin code");
    }

    //checking if card is locked after 3 attempts
    @Test
    @DisplayName("Test if the card will lock after 3 failed attempts")
    public void lockCardAfterThreeFailedLoginAttempts() {
      //  when(bank.getUserById("Bailey")).thenReturn(user);
        when(bank.isCardLocked("Bailey")).thenReturn(false);

        assertTrue(atm.insertCard("Bailey"), "card insert should be successful");

        //failed login aattemps with wrong pins
        boolean firstFailAttempt = atm.enterPin("7777");
        boolean secondFailAttempt = atm.enterPin("6666");
        boolean thirdFailAttempt = atm.enterPin("5555");

        assertFalse(firstFailAttempt, "1 login attempt should fail");
        assertFalse(secondFailAttempt, "2 login attempt should fail");
        assertFalse(thirdFailAttempt, "3 login attempt should fail");

        assertTrue(user.isLocked(), "users card should be locked after 3 failed attempts");
    }


    //check balance and show to user
    @Test
    @DisplayName("Test checking bank balance")
    public void checkBankBalance() {
        assertTrue(atm.insertCard("Bailey"), "card insert should be successful");
        assertTrue(bank.verifyPin(user, "9999"), "pin should be correct");
        double balance = atm.checkBalance();
        assertEquals(500.00, balance, "the balance should be 500.00");
    }

    //checks if bank balance is sufficient for withdraw and shows the bank balance after withdraw
    @Test
    @DisplayName("Test if bank balance is sufficient for a withdraw")
    public void bankBalanceIsSufficient() {
        //mocks
      //  when(bank.getUserById("Bailey")).thenReturn(user);
        assertTrue(atm.insertCard("Bailey"), "card insert should be successful");
        assertTrue(atm.enterPin("9999"), "pin should be correct");
        when(bank.verifyPin(user, "9999")).thenReturn(true);


        bank.getUserById("Bailey").setBalanceForTest(500.00);
        System.out.println("balance before withdraw " + user.getBalance());

        boolean withdrawYes = atm.withdraw(200.00);
        assertTrue(withdrawYes, "withdraw should go through with sufficient balance");
        System.out.println("Balance after withdraw " + user.getBalance());

        assertEquals(300.00, user.getBalance(), "Balance should be 300 after withdrawal");
    }

    //checks if balance is insufficient for withdraw and shows insufficient amount message to user
    @Test
    @DisplayName("Test if bank balance is insufficient")
    public void bankBalanceIsInsufficient() {
        assertTrue(atm.insertCard("Bailey"), "card insert should be successful");
        assertTrue(atm.enterPin("9999"), "pin should be correct");
        when(bank.verifyPin(user, "9999")).thenReturn(true);

        user.setBalanceForTest(100.00);
        System.out.println("balance before withdraw " + user.getBalance());

        boolean withdrawYes = atm.withdraw(200.00);

        assertFalse(withdrawYes, "withdraw should fail with insufficient balance");
        System.out.println("balance after withdraw attempt " + user.getBalance());

        assertEquals(100.00, user.getBalance(), "balance should be the same after failed withdrawal");
    }

    @Test
    @DisplayName("Test if atm allows withdrawing the exact balance")
    public void testWithdrawExactBalance() {
        assertTrue(atm.insertCard("Bailey"), "card insert should be successful");
        assertTrue(atm.enterPin("9999"), "pin should be correct");
        when(bank.verifyPin(user, "9999")).thenReturn(true);

        user.setBalanceForTest(500.00);
        System.out.println("Balance before withdraw " + user.getBalance());

        boolean withdrawalSuccess = atm.withdraw(500.00);
        System.out.println("Balance after withdraw " + user.getBalance());

        assertTrue(withdrawalSuccess, "should allow withdraw the exact balance");
        assertEquals(0.00, user.getBalance(), "users balance should be 0 after withdrawing everything");
    }


    //deposit to bank
    @Test
    @DisplayName("Test if money is deposited into the account")
    public void depositMoneyToBank() {
        //when(bank.getUserById("Bailey")).thenReturn(user);
        //atm.insertCard("Bailey");
        //atm.enterPin("9999");
        assertTrue(atm.insertCard("Bailey"), "card insert should be successful");
        assertTrue(atm.enterPin("9999"), "pin should be correct");
        when(bank.verifyPin(user, "9999")).thenReturn(true);

        user.setBalanceForTest(100.00);
        System.out.println("balance before deposit " + user.getBalance());

        boolean depositSuccess = atm.deposit(200.00);

        assertTrue(depositSuccess, "deposit should be successful");

        System.out.println("balance after deposit " + user.getBalance());

        assertEquals(300.00, user.getBalance(), "the balance should be 300 after depositing 200");
    }


    @Test
    @DisplayName("Test if the atm identifies the bank name")
    void testBankName() {
        try (MockedStatic<Bank> mockedBank = mockStatic(Bank.class)) {
            mockedBank.when(Bank::getBankName).thenReturn("banky bank");
            System.out.println( "the bank name is " + Bank.getBankName());
            assertEquals("banky bank", Bank.getBankName(), "the banks name should be banky bank");
        }
    }

        @Test
        @DisplayName("Test if the user can cancel the transaction at any point")
        public void testCancelTransaction() {
        //when(bank.getUserById("Bailey")).thenReturn(user);

            assertTrue(atm.insertCard("Bailey"), "card insert should be successful");
            assertTrue(atm.enterPin("9999"), "pin should be correct");
            when(bank.verifyPin(user, "9999")).thenReturn(true);

           // boolean withdrawSuccess = atm.withdraw(100.00);
           // assertTrue(withdrawSuccess, "withdrawl should succeed");

            atm.cancelTransaction();

          //  boolean withdrawAfterCancel = atm.withdraw(50.00);
          //  assertFalse(withdrawAfterCancel, "should fail after transaction is cancelled");
        }



}

// ctr alt L remormat code