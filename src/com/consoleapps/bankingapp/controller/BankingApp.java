package com.consoleapps.bankingapp.controller;
import java.util.*;
import java.sql.*;

import com.consoleapps.bankingapp.dao.TransactionDAO;
import com.consoleapps.bankingapp.db.DBConnectionUtil;
import com.consoleapps.bankingapp.model.BankAccount;

public class BankingApp {

    private static final Scanner scan = new Scanner(System.in);
    private static final String adminPass = "Naan dhaan da LEO";

    public static void main(String[] arg){
        int enter ;
        while(true){
            try{
                System.out.println("Admin login (or) Sign in (or) Sign up (or) EXIT to quit");
                System.out.println("Enter 1 for Sign In");
                System.out.println("Enter 2 for Sign Up");
                System.out.println("Enter 3 for Admin Login");
                System.out.println("Enter 4 for Exit");
                enter = Integer.parseInt(scan.nextLine().trim());
            }catch (NumberFormatException e){
                System.out.println("Invalid input. Please enter a number");
                continue;
            }
            switch (enter) {
                case 1:
                    System.out.println("Please enter the User Name");
                    String id = scan.nextLine();
                    Optional<BankAccount> optAcc = findAccInDB(id);
                    if(optAcc.isEmpty()){
                        System.out.println("User Invalid. Please Try again!");
                        continue;
                    }
                    while(true){
                        System.out.println("Please enter your password");
                        BankAccount yourAcc = optAcc.get();
                        if (yourAcc.isValid(scan.nextLine())) {
                            System.out.println("Login Successful. Welcome \"" + id + "\"");
                            bankingMenu(yourAcc);
                        } else{
                            System.out.println("Invalid Password. Try again");
                            continue;
                        }
                        break;
                    }
                    break;
                case 2:
                    System.out.println("Please enter User name");
                    String name = scan.nextLine().trim();
                    if (findAccInDB(name).isPresent()) {
                        System.out.println("User already exists. Try Signing in!");
                    } else {
                        String pass;
                        System.out.println("Please enter your password");
                        while (true) {
                            pass = scan.nextLine();
                            if (pass.length() != 4) {
                                System.out.println("Password should contains four characters. Please try again");
                                continue;
                            }
                            break;
                        }
                        int iniDep;
                        System.out.println("Please enter initial deposit amount");
                        while (true) {
                            try{
                                iniDep = Integer.parseInt(scan.nextLine());
                                if (iniDep < 1000) {
                                    System.out.println("Initial deposit must be higher than $1000. Please Try again");
                                    continue;
                                }
                                break;
                            }catch(NumberFormatException e){
                                System.out.println("Invalid amount entered.");
                            }
                        }
                        try{
                            BankAccount newAcc = new BankAccount(name,pass,iniDep);
                            insertNewAcc(newAcc);
                            System.out.println("Account created successfully");
                        }catch (SQLException e){
                            System.out.println("Error: Failed to create account in database");
                        }
                        break;
                    }
                    break;
                case 3:
                    System.out.println("Welcome Parthiban");
                    String pass;
                    System.out.println("Enter the password");
                    while(true){
                        pass = scan.nextLine().trim();
                        if (!pass.equalsIgnoreCase(adminPass)){
                            System.out.println("Incorrect password. Please try again");
                            continue;
                        }
                        break;
                    }
                    adminLogin();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Please enter a valid input");
            }
            if(enter==4){
                System.out.println("Thank you contacting us..!");
                break;
            }
        }
    }

    public static void bankingMenu(BankAccount yourAcc){
        while (true){
            System.out.println("Welcome to BlueVerse Bank");
            System.out.println("Please enter your choice");
            System.out.println("Enter 1 for Deposit");
            System.out.println("Enter 2 for Withdrawal");
            System.out.println("Enter 3 for Check your balance");
            System.out.println("Enter 4 for Change your password");
            System.out.println("Enter 5 to see transaction details");
            System.out.println("Enter 6 for Sign out");
            int button ;
            try{
                button = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number");
                continue;
            }
            try{
                switch (button){
                    case 1:
                        int amount ;
                        while(true){
                            System.out.println("Please enter the deposit amount");
                            amount = Integer.parseInt(scan.nextLine());
                            if(amount<=0){
                                System.out.println("Please enter a higher amount");
                            }else{
                                yourAcc.deposit(amount);
                                updateAccBal(yourAcc);
                                TransactionDAO.insertTransaction(yourAcc.getUserName(), amount,"DEPOSIT");
                                break;
                            }
                        }
                        break;
                    case 2:
                        int withdraw ;
                        while (true){
                            System.out.println("Please enter the withdraw amount");
                            withdraw = Integer.parseInt(scan.nextLine());
                            if(withdraw<100){
                                System.out.println("Please enter a higher amount to withdraw");
                                continue;
                            }
                            if(yourAcc.getBalance() >= withdraw){
                                yourAcc.withdraw(withdraw);
                                updateAccBal(yourAcc);
                                TransactionDAO.insertTransaction(yourAcc.getUserName(), withdraw,"WITHDRAW");
                                break;
                            }else{
                                System.out.println("Insufficient balance");
                                break;
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Your balance is : "+yourAcc.getBalance());
                        break;
                    case 4:
                        System.out.println("Enter your old password");
                        String old = scan.nextLine();
                        System.out.println("Enter new password");
                        String newPass = scan.nextLine();
                        if(yourAcc.changePassword(old,newPass)){
                            updateAccPass(yourAcc);
                            System.out.println("Password changed successfully");
                        }
                        else System.out.println("Your password is incorrect");
                        break;
                    case 5:
                        System.out.println("Your transaction history");
                        int i = 1;
                        for(String trans : TransactionDAO.getTransactionsByUserName(yourAcc.getUserName())){
                            System.out.println((i++)+") "+trans);
                        }
                        break;
                    case 6:
                        System.out.println("Thank you for your time with us!. \""+yourAcc.getUserName()+"\"");
                        return;
                    default:
                        System.out.println("Please enter a valid choice");
                }
            } catch (SQLException e) {
                System.err.println("DB Error during operation");
            }catch (NumberFormatException e){
                System.out.println("Invalid input");
            }
        }
    }
    public static void adminLogin(){
        while (true){
            System.out.println("Welcome Admin");
            System.out.println("Please enter your choice");
            System.out.println("Enter 1 for checking number of accounts registered");
            System.out.println("Enter 2 for checking User details");
            System.out.println("Enter 3 for Deposited money");
            System.out.println("Enter 4 to sign out");
            int button ;
            try{
                button = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number");
                continue;
            }
            switch (button){
                case 1:
                    System.out.println("No of accounts registered : "+countAccInDB());
                    break;
                case 2:
                    displayAllUserDetailFromDB();
                    break;
                case 3:
                    System.out.println("Total deposited money "+getTotalBalInDB());
                    break;
                case 4:
                    System.out.println("Thank you Admin");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    private static Optional<BankAccount> findAccInDB(String userName){
        String sql = "SELECT user_name, password, balance FROM account WHERE user_name = ?";
        try(Connection conn = DBConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, userName);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        String user = rs.getString("user_name");
                        String pass = rs.getString("password");
                        int bal = rs.getInt("balance");
                        return Optional.of(new BankAccount(user,pass,bal));
                    }
                }
            }catch (SQLException e){
                System.err.println("DB Connection/Query Error"+e.getMessage());
        }
        return Optional.empty();
    }
    public static void insertNewAcc(BankAccount account) throws SQLException{
        String sql = "INSERT INTO account (user_name,password,balance) VALUES(?,?,?)";
        try(Connection conn = DBConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, account.getUserName());
            ps.setString(2,account.getPassword());
            ps.setInt(3,account.getBalance());
            ps.executeUpdate();
        }
    }
    public static void updateAccBal(BankAccount account) throws SQLException{
        String sql = "UPDATE account SET balance = ? WHERE user_name = ?";
        try(Connection conn = DBConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,account.getBalance());
            ps.setString(2, account.getUserName());
            ps.executeUpdate();
        }
    }
    public static void updateAccPass(BankAccount account) throws SQLException{
        String sql = "UPDATE account SET password = ? WHERE user_name = ?";
        try(Connection conn = DBConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, account.getPassword());
            ps.setString(2, account.getUserName());
            ps.executeUpdate();
        }
    }
    private static int countAccInDB(){
        String sql = "SELECT count(user_name) AS total FROM account";
        try(Connection conn = DBConnectionUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
                if(rs.next()){
                    return rs.getInt("total");
                }
        }catch (SQLException e){
            System.err.println("DB Error: "+e.getMessage());
        }
        return 0;
    }
    private static int getTotalBalInDB(){
        String sql = "SELECT sum(balance) AS total FROM account";
        try(Connection conn = DBConnectionUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next()){
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("DB Error: "+e.getMessage());
        }
        return 0;
    }
    private static void displayAllUserDetailFromDB(){
        String sql = "SELECT user_name, balance FROM account";
        try(Connection conn = DBConnectionUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                String user = rs.getString("user_name");
                int bal = rs.getInt("balance");
                System.out.println("User name: \""+user+"\"   Holding amount---> $"+bal);
            }
        }catch (SQLException e){
            System.err.println("DB Error: "+e.getMessage());
        }
    }
}