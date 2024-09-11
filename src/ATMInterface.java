import java.util.ArrayList;
import java.util.Scanner;

class Account{
    private final String id;
    private final int pin;
    private double balance;

    public Account(String id, int pass, double balance) {
        this.id = id;
        this.pin = pass;
        this.balance = balance;
    }
    public boolean withdraw(double amount){
        if (balance < amount)return false;
        balance -= amount;
        return true;
    }
    public void deposit(double amount){
        balance += amount;
    }

    public int getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public String getId() {
        return id;
    }
}

class BankDatabase{
    public static ArrayList<Account> accounts = new ArrayList<>();
    static {
        accounts.add(new Account("card1", 1234, 1000));
        accounts.add(new Account("card2", 4231, 1000));
        accounts.add(new Account("card3", 6789, 1000));
    }
    public Account getAccount(String id){
        for (Account account:accounts){
            if (account.getId().equals(id)){
                return account;
            }
        }
        return null;
    }
    public void updateAcc(Account accNew){
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId().equals(accNew.getId())){
                accounts.set(i, accNew);
                break;
            }
        }
    }
}

class ATM{
    private static final BankDatabase database = new BankDatabase();
    private Account currAccount;
    private int getChoice(){
        System.out.println("Options Menu:");
        System.out.println("""
                1- Withdraw
                2- Deposit
                3- Check balance
                4- Eject card""");
        return new Scanner(System.in).nextInt();
    }
    private int login(){
        System.out.print("Please insert the card or type EXIT: ");
        String id = new Scanner(System.in).nextLine();
        if (id.equals("EXIT")){
            return -1;
        }
        Account account = database.getAccount(id);
        if (account == null) {
            System.out.println("Error account doesn't exist!");
            return -2;
        }

        int attempts = 3;
        System.out.print("Please enter the password: ");
        int pin = new Scanner(System.in).nextInt();
        while (pin != account.getPin() && attempts > 0){
            System.out.println("Wrong password!");
            System.out.print("Please enter the password again: ");
            pin = new Scanner(System.in).nextInt();
            attempts--;
        }

        if (pin != account.getPin()){
            System.out.println("Max login attempts reached ejecting card!");
            return -2;
        }
        currAccount = account;
        return 0;
    }
    public void run(){
        int state = 0;
        while (state != -1){
            state = login();
            if (state == -1 || state == -2) continue;

            while (true){
                int choice = getChoice();

                if (choice == 1){
                    System.out.print("Enter the amount to withdraw: ");
                    double amount = new Scanner(System.in).nextDouble();
                    if (currAccount.withdraw(amount)){
                        System.out.println("Money withdrawn successfully!");
                        database.updateAcc(currAccount);
                    }else System.out.println("No sufficient funds!");

                }
                else if (choice == 2) {
                    System.out.print("Enter the amount to deposit: ");
                    double amount = new Scanner(System.in).nextDouble();
                    currAccount.deposit(amount);
                    database.updateAcc(currAccount);
                    System.out.println("Money deposited successfully!");
                }else if (choice == 3){
                    System.out.println(STR."Your balance= \{currAccount.getBalance()}");
                } else if (choice == 4) {
                    System.out.println("Ejecting card...");
                    break;
                }else {
                    System.out.println("Invalid choice please numbers from 1 to 4!");
                }
            }
        }
    }
}

public class ATMInterface {
    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.run();
    }
}