package com.revature.models;

/**
 * The model used to store bank account activity and account password.
 */
public class BankAccount {
    private final String name;          // Name of the bank account
    private int accountID;              // Account ID of bank account
    private double balance;             // The balance of the bank account

    /**
     * Constructor used by DAOImpl when user logs in. Sets the account name, accountID, and balance.
     *
     * @param name The name of the bank account
     * @param id The account_id
     * @param balance The balance of the bank account
     */
    public BankAccount(String name, int id, double balance){
        this.balance = balance;
        this.name = name;
        this.accountID = id;
    }

    /**
     * Constructor used when the account is first opened.
     *
     * @param name The name of the bank account
     * @param balance The initial balance of the bank account
     */
    public BankAccount(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.accountID = 0;
    }

    /**
     * Getters for all the private instance variables.
     */
    public String getName(){
        return this.name;
    }

    public double getBalance() {
        return balance;
    }

    public int getAccountID() {
        return accountID;
    }

    /**
     * Setter used to set accountID.
     */
    public void setAccountID(int id){
        this.accountID = id;
    }


    /**
     * Instance method used to deposit into a specific account
     *
     * @param money The amount of money being deposited into the account
     */
    public void deposit(double money) {
        if(money < 0) {
            System.out.println("Cannot deposit negative money.");
            return;
        }
        this.balance += money;
    }

    /**
     * Instance method used to withdraw money from a specific account
     *
     * @param money The amount of money being withdrawn from the account
     */
    public void withdraw(double money) {
        if(money < 0) {
            System.out.println("Cannot withdraw negative money.");
            return;
        }
        if(money > balance) {
            System.out.println("Cannot withdraw more than balance.");
            return;
        }
        this.balance -= money;
    }

    /**
     * Overridden toString() method that returns a string that states the current open account number, name, and balance.
     *
     * @return String that states the current open account number, name, and balance.
     */
    @Override
    public String toString(){
        return String.format("Account Number: %d\nAccount Name: %s\nCurrent Balance: $%.2f", accountID, name, balance);
    }
}
