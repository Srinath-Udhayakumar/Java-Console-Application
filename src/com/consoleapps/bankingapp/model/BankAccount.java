package com.consoleapps.bankingapp.model;

public class BankAccount{
    private final String userName;
    public String getUserName(){
        return userName;
    }

    private String password;
    public boolean isValid(String password){
        return this.password.equals(password);
    }
    public String getPassword() { return password; }

    private int balance;
    public int getBalance(){
        return balance;
    }


    public BankAccount(String userName, String password, int balance){
        this.balance = balance;
        this.userName = userName;
        this.password = password;
    }

    public void deposit(int amount){
        if(amount>0){
            balance+=amount;
            System.out.println("The deposited amount is : "+amount);
        }else System.out.println("The deposit amount should be positive");
    }

    public void withdraw(int amount){
        if(amount>99 && amount<=balance){
            balance-=amount;
            System.out.println("Withdrawn amount is : "+amount);
        }else if(amount>balance) System.out.println("Insufficient balance");
        else System.out.println("Please enter a positive amount");
    }

    public boolean changePassword(String oldPass, String newPass){
        if(newPass.length()!=4){
            System.out.println("Password should contains four characters");
            return false;
        }
        if(this.password.equals(oldPass)){
            this.password = newPass;
            return true;
        }else {
            return false;
        }
    }
}
