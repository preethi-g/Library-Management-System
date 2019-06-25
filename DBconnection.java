
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author si179
 */
public class DBconnection {
     static String url = "jdbc:postgresql://localhost:5432/lms";
   static String userName = "postgres";
   static String password = "postgres";
    static Connection getConnection() 
    {
        try{
        return DriverManager.getConnection(url,userName,password);
    }catch(SQLException e)
    {
        e.printStackTrace();
    }
        return null;
}
}
