import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager
{
    private Connection connection;
    private Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner) {
            this.connection = connection;
            this.scanner = scanner;
    }

    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter a Amount ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin ");
        String security_pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_number != 0){
                PreparedStatement ps = connection.prepareStatement("SELECT  * FROM accounts where account_number = ? and security_pin = ?");
                ps.setLong(1, account_number);
                ps.setString(2, security_pin);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    String credit_query = "UPDATE accounts SET balance = balance  + ? where account_number = ?";
                    PreparedStatement ps1 = connection.prepareStatement(credit_query);
                    ps1.setDouble(1, amount);
                    ps1.setLong(2, account_number);
                    int rowAffected = ps1.executeUpdate();
                    if(rowAffected > 0){
                        System.out.println("Rs."+amount + " has been credited successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }else{
                        System.out.println("Transaction failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }else{
                    System.out.println("invalid  security pin");

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void debit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter a Amount ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin ");
        String security_pin = scanner.nextLine();

        try {
                connection.setAutoCommit(false);
                if(account_number != 0){
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? and security_pin =?");
                    preparedStatement.setLong(1, account_number);
                    preparedStatement.setString(2, security_pin);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if(resultSet.next()){
                        double current_amount = resultSet.getDouble("balance");
                        if(current_amount >= amount){
                           String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                           PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                           preparedStatement1.setDouble(1, amount);
                           preparedStatement1.setLong(2, account_number);
                           int rowAffected = preparedStatement1.executeUpdate();
                           if(rowAffected >= 1){
                               System.out.println("Rs." + amount + " has been debited successfully");
                               connection.commit();
                               connection.setAutoCommit(true);
                               return;
                           }else {
                               System.out.println("Transaction failed !!!");
                               connection.rollback();
                               return;

                           }
                        }else{
                            System.out.println("insufficient balance !");
                        }
                    }
                }else{
                    System.out.println("insufficient balance !");
                }
        }catch (Exception e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number)  {
        scanner.nextLine();
        System.out.println("Enter a reciver account number : ");
        long reciver_account_number = scanner.nextLong();
        System.out.println("Enter a amount :");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter Security Pin ");
        String security_pin = scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(sender_account_number != 0 && reciver_account_number != 0){
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? and security_pin = ?");
                ps.setLong(1, reciver_account_number);
                ps.setString(2, security_pin);
                ResultSet resultSet = ps.executeQuery();
                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(current_balance >= amount){
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, sender_account_number);
                        PreparedStatement preparedStatement2 = connection.prepareStatement(credit_query);
                        preparedStatement2.setDouble(1, amount);
                        preparedStatement2.setLong(2, reciver_account_number);
                        int rowAffected = preparedStatement1.executeUpdate();
                        int rowAffected2 = preparedStatement2.executeUpdate();
                        if(rowAffected > 0 && rowAffected2 > 0) {
                            System.out.println("Transfer successfully");
                            System.out.println("Rs." + amount + " has been transferred successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transaction failed !!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }

                    }else{
                        System.out.println("insufficient balance !");
                    }

                }
            }else{
                System.out.println("account_number or reciver_account_number is invalid");
            }
        }catch (Exception e ){
            e.printStackTrace();
        }

    }

    public void getBalance(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter Security Pin ");
        String security_pin = scanner.nextLine();
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT balance  FROM accounts WHERE account_number = ? AND security_pin = ?");
            ps.setLong(1, account_number);
            ps.setString(2, security_pin);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                double balance = rs.getDouble("balance");
                System.out.println("Balance is "+balance);

            }else{
                System.out.println("Invalid security pin");
            }
        }catch (Exception e){

            e.printStackTrace();
        }
    }

}
