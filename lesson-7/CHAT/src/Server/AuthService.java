package Server;

import java.sql.*;

public class AuthService {
    private static Connection connections;
    private static Statement stmt;

    public static void connect (){
        try {
            Class.forName("org.sqlite.JDBC");
            connections = DriverManager.getConnection("jdbc:sqlite:authorization.db");
            stmt = connections.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static String getNickLoginAndPass( String login, String pass){
        String sql = String.format("select nick from auth\n" +
                "where (login = '%s' and pass = '%s');", login, pass);
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
     public static void disconnect(){
         try {
             connections.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

}
