package org.example.java;
import java.util.HashMap;
import java.util.Map;

public class Bank implements BankInterface {

    /* This class respresents the Bank operationas that manage users and their accounts.
    It manages user data, check pins and card status (locked)
     */

    private Map<String, User> users = new HashMap<>(); //a map to hold users by their IDs

    //constructor to set up mock users in the bank
    public Bank() {
        //sample user setup with balance 500
        users.put("Bailey", new User("Bailey", "9999", 500.00));
    }

    //get user information by user id
    @Override
    public User getUserById(String id) {
        return users.get(id); //get user from map by ID
    }

    //checks if a users card is locked
    @Override
    public boolean isCardLocked(String userId) {
        User user = users.get(userId); //get user
        return user != null && user.isLocked(); //if user is locked it returns truer
    }

    // just to get the bankname
    public static String getBankName() {
        return "bank"; //returning the mock banks namee
    }

   public boolean verifyPin(User user, String pin) {
       if (user.getPin().equals(pin)) {
           user.resetFailedAttempts();
           return true;
       } else {
           user.incrementFailedAttempts();
           return false;
       }
    }
}
