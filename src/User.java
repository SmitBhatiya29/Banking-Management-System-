import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    private Connection connection;
    private Scanner scanner;

    User( Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();
        System.out.println("Fullname : ");
        String fullname = scanner.nextLine();
        System.out.println("Email : ");
        String email = scanner.nextLine();
        System.out.println("Password : ");
        String password = scanner.nextLine();

        if(user_exist(email)){
            System.out.println("Email already exists");
            return;
        }

        String register_query = "INSERT INTO User (full_name,email,password) VALUES (?,?,?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1,fullname);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows == 1){
                System.out.println("User registered successfully");
            }else {
                System.out.println("register failed");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String Login(){
        scanner.nextLine();
        System.out.println("Email : ");
        String email = scanner.nextLine();
        System.out.println("Password : ");
        String password = scanner.nextLine();
        String login_query = "SELECT * FROM User WHERE email = ? AND password = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return email;
            }else{
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean user_exist(String email){
        String query = "SELECT * FROM User WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
