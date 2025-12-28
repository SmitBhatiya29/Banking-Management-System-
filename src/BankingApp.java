import java.sql.Connection;
import java.sql.DriverManager;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class BankingApp {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/BMS";
        String username = "root";
        String password = "Smit@001";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,password);
            Scanner sc = new Scanner(System.in);
            User user = new User(connection,sc);
            AccountManager accountManager = new AccountManager(connection,sc);
            Accounts accounts = new Accounts(connection,sc);
            System.out.println("Connected to database");

            String email;
            long  account_number;

            while(true){
                System.out.println("Welcome to Banking App");
                System.out.println();
                System.out.println("1. register");
                System.out.println("2. login");
                System.out.println("3. exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                switch(choice){
                    case 1:
                        user.register();
                        break;
                    case 2 :
                        email = user.Login();
                        if(email != null){
                            System.out.println();
                            System.out.println("User Logged In .. !");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open a new Acoount ");
                                System.out.println("2. Exit");
                                if(sc.nextInt() == 1){
                                    account_number = accounts.open_acoount(email);
                                    System.out.println("Account number opened successfully");
                                    System.out.println("Your account Number is " + account_number);
                                }else{
                                    break;
                                }
                            }
                            account_number = accounts.getAccountNumber(email);
                            int choice1 = 0;
                            while(choice1 != 5){
                                System.out.println();
                                System.out.println("1. Debit Money ");
                                System.out.println("2. Credit Money ");
                                System.out.println("3. Transfer Money ");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Logout");
                                System.out.print("Enter your choice: ");
                                choice1 = sc.nextInt();
                                switch(choice1){
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter a valid choice" );
                                        break;

                                }
                            }

                        }else{
                            System.out.println("Invalid email");
                        }


                    case 3:
                        System.out.println("Thank you for using Banking App");
                        System.out.println("Exit from Banking App");
                        return;
                    default:
                        System.out.println("Enter Valid choice");
                        break;
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}