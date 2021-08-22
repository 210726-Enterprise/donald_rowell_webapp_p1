package com.revature.models;

import com.revature.annotations.Column;
import com.revature.annotations.PrimaryKey;
import com.revature.annotations.Table;

import java.sql.Date;
import java.util.Scanner;

/**
 * The model used to store personal information of the user.
 */
@Table(tableName = "customer_information")
public class CustInfo {

    @PrimaryKey(columnName = "customer_id")
    private int id;                 // The customer_id in the database.
    @Column(columnName = "user_name")
    private final String userName;  // The username of the user
    @Column(columnName = "password")
    private final String password;  // The password of the user
    @Column(columnName = "first_name")
    private String fName;           // The first name of the user
    @Column(columnName = "last_name")
    private String lName;           // The last name of the user
    @Column(columnName = "birth_date")
    private Date birthDate;         // The birthdate of the user in the following format: YYYY-MM-DD
    @Column(columnName = "phone")
    private String phoneNum;        // The phone number of the user
    @Column(columnName = "email")
    private String email;           // The email address of the user

    /**
     * Constructor used so when a new account is created.
     *
     * @param userName username of the user
     * @param password password for the user's account
     */
    public CustInfo(String userName, String password){
        this.userName = userName;
        this.password = password;
        this.fName = "";
        this.lName = "";
        this.birthDate = Date.valueOf("1900-1-1");
        this.phoneNum = "";
        this.email = "";
    }


    /**
     * Constructor used by DAOImpl when user logs in. All-args constructor.
     *
     * @param id customer_id
     * @param userName username of user
     * @param password password for user's account
     * @param fName First name of user
     * @param lName Last name of user
     * @param birthDate Birthdate of user
     * @param phoneNum Phone number of user
     * @param email Email address of user
     */
    public CustInfo(int id, String userName, String password, String fName, String lName, Date birthDate, String phoneNum, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.birthDate = birthDate;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    /**
     * Setters for the private instance variables, except username and password, as those are final.
     */
    public void setID(int id){
        this.id = id;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = Date.valueOf(birthDate);
    }

    public void setPhoneNum(String phoneNum) {
        if(phoneNum.equals("")){return;}
        this.phoneNum = phoneNum;
    }

    public void setEmail(String email) {
        if(email.equals("")){return;}
        this.email = email;
    }

    /**
     * Getters for all the private instance variables.
     */
    public int getID(){
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Instance method used for updating the personal information of the user.
     *
     * @param scanner Object used to read user input
     */
    public void setUpInfo(Scanner scanner){
        String n;
        System.out.print("First Name (Required): ");
        n = scanner.nextLine();
        if(n.equals("")){
            System.out.println("Please input a first name");
            this.setUpInfo(scanner);
            return;
        }
        this.setfName(n);

        System.out.print("\nLast Name (Required): ");
        n = scanner.nextLine();
        if(n.equals("")){
            System.out.println("Please input a last name");
            this.setUpInfo(scanner);
            return;
        }
        this.setlName(n);

        try{
            System.out.print("\nBirth Date (YYYY-MM-DD)(Required): ");
            n = scanner.nextLine();
            if(n.length() < 8){
                System.out.println("Please input a valid birthdate in the following format: YYYY-MM-DD");
                this.setUpInfo(scanner);
                return;
            }
            this.setBirthDate(n);
        }catch(IllegalArgumentException e){
            System.out.println("Please input a valid birthdate in the following format: YYYY-MM-DD");
            this.setUpInfo(scanner);
            return;
        }

        System.out.print("\nPhone Number: ");
        this.setPhoneNum(scanner.nextLine());

        System.out.print("\nEmail: ");
        this.setEmail(scanner.nextLine());
        System.out.println("");
    }

    /**
     * Overridden toString() method that returns a string that states the personal information of the user.
     *
     * @return String
     */
    @Override
    public String toString(){
        String s = String.format("Name: %s %s\nBirth Date: %s\n", fName, lName, birthDate);
        if(!phoneNum.equals("")){
            s = s + String.format("Phone Number: %s\n", phoneNum);
        }
        if(!email.equals("")){
            s = s + String.format("Email: %s\n", email);
        }
        return s;
    }
}
