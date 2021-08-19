package com.revature.models;

/**
 * User model with nested BankAccount and CustInfo objects
 */
public class User {
    private BankAccount account;    // Nested BankAccount object which describes the user's bank account.
    private CustInfo ci;            // Nested CustInfo object which stores the user's personal information.

    /**
     * Constructor used when a new user register. Populates the CustInfo object and leaves the BankAccount reference pointing to null
     *
     * @param userName username of the user
     * @param password password for the user's account
     */
    public User(String userName, String password){
        ci = new CustInfo(userName, password);
    }

    /**
     * Constructor used when user is closing their bank account. This resets the user's account reference to point back at null again.
     *
     * @param ci CustInfo object that stores all the user's personal information
     */
    public User(CustInfo ci){
        this.ci = ci;
    }

    /**
     * Used by ServiceImpl when user logs into their account. This repopulates their user object with their related information.
     *
     * @param ci CustInfo object with the user's peronal information
     * @param account BankAccount object with the user's bank account information
     */
    public User(CustInfo ci, BankAccount account){
        this.ci = ci;
        this.account = account;
    }

    /**
     * Getter's used to manipulate the user's customer information and bank account.
     */
    public CustInfo getCi(){
        return ci;
    }

    public BankAccount getAccount(){
        return account;
    }

    /**
     * Method used when user opens a new bank account. This method gives the account variable value.
     * @param name name of bank account
     * @param money initial balance of bank account
     */
    public void openAccount(String name, double money){
        account = new BankAccount(name, money);
    }
}
