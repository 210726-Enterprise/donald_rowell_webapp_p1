package com.revature.models;

import com.revature.annotations.Column;
import com.revature.annotations.PrimaryKey;
import com.revature.annotations.Table;

/**
 * The model used to store bank account activity and account password.
 */
@Table(tableName = "account_balances")
public class BankAccount {
    @PrimaryKey(columnName = "account_id")
    private int accountID;              // Account ID of bank account
    @Column(columnName = "account_name")
    private String name;                // Name of the bank account
    @Column(columnName = "balance")
    private double balance;             // The balance of the bank account

    /**
     * Constructor used by DAOImpl when user logs in. Sets the account name, accountID, and balance.
     *
     * @param name The name of the bank account
     * @param id The account_id
     * @param balance The balance of the bank account
     */
    public BankAccount(int id, String name, double balance){
        this.balance = balance;
        this.name = name;
        this.accountID = id;
    }

    public BankAccount(){
        this.accountID = 0;
        this.name = "default";
        this.balance = -1.0;
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
     * Setters.
     */
    public void setAccountID(int id){
        this.accountID = id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setName(String name) {
        this.name = name;
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
