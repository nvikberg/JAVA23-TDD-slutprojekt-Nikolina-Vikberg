package org.example.java;

public class User {

    /* This class represents a user with credentials (id, pin, balance),
    it manages failed login attempts and locking the card after 3 failed attempts.*/

    private String id;
    private String pin;
    private double balance;
    private int failedAttempts;
    private boolean isLocked;

    public User(String id, String pin, double balance) {
        this.id = id;
        this.pin = pin;
        this.balance = balance;
        this.failedAttempts = 0;
        this.isLocked = false; //card is starting as - not locked
    }


    //getters och Setters
    public String getId() {
        return id;
    }
    public String getPin() {
        return pin;
    }
    public double getBalance() {
        return balance;
    }
    public int getFailedAttempts() {
        return failedAttempts;
    }
    public boolean isLocked() {
        return isLocked;
    }

    public void lockCard() {
        this.isLocked = true;
    }
    public void incrementFailedAttempts() {
        this.failedAttempts++;
        if (failedAttempts >= 3) {
            isLocked = true; //card locks after 3 failed attempts
            System.out.println("card is locked after 3 failed login attempts");
        }
    }
    public void resetFailedAttempts() {
        this.failedAttempts = 0;
    }
    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public void setBalanceForTest(double newBalance) {
        this.balance = newBalance;

    }
}
