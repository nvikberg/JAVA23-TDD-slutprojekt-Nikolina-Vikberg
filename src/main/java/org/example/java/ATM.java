package org.example.java;

public class ATM {
/*
    This class represents the atm machine, it manages interactions with the user
    it allows users to insert car, enter pin code, check balance,
    deposit and withdraw money and cancel transactions
 */
    private Bank bank;
    private User currentUser;


    public ATM(Bank bank) {
        this.bank = bank;
    }

    //insert card
    public boolean insertCard(String userId) {
        this.currentUser = bank.getUserById(userId); //gets user from the bank from id
        System.out.println("inserting card for user  " + userId);
        return currentUser != null && !bank.isCardLocked(userId);//false is no user or card is locked
    }

    //check if user exist and if the entered pin  match with the users pin code
    public boolean enterPin(String pin) {
        if (this.currentUser == null) {
            System.out.println("no user is logged in");
            return false;
        }

        if (currentUser.getPin().equals(pin)) {
            currentUser.resetFailedAttempts();
            return true;//login successful
        } else {
            currentUser.incrementFailedAttempts();
            if (currentUser.getFailedAttempts() >= 3) { //if there has been 3 failed attempts card will be locked
                System.out.println("card is now locked for " + currentUser);
                currentUser.lockCard();
            }
            return false; //if login failed then return false
        }
    }

    public double checkBalance() {
            if (this.currentUser == null) {
                throw new IllegalStateException("User must be logged in first.");
            }
        System.out.println("balance is " + currentUser.getBalance());
        return currentUser.getBalance();
    }

    //will return true if withdraw is successful, if there is sufficient balance
    public boolean withdraw(double amount) {
        if (this.currentUser == null) {
            throw new IllegalStateException("user must be logged in to withdraw");
        }
        if (currentUser.getBalance() < amount) {
            // currentUser.getBalance()
            System.out.println("insufficient amount in the account for a withdraw");
            return false;
        }
        //currentUser.withdraw(amount);
      //  System.out.println( currentUser.getBalance() + " " + amount);
        currentUser.withdraw(amount);
        return true;
    }

    public boolean deposit(double amount) {
        if (this.currentUser == null) {
            throw new IllegalStateException("user must be logged in first.");
        }
        currentUser.deposit(amount);
        System.out.println(amount + " deposited");
        return true;

    }

    public void cancelTransaction() {
        if (currentUser == null) {
            throw new IllegalStateException("No user logged in to cancel");
        }
        currentUser = null;
        System.out.println("Transaction cancelled. Please take your card...");
    }

    public User getCurrentUser() {
        return currentUser;
    }
}


