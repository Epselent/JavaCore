package Server;

import sample.ClientHandler;

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
                "where (login = '%s' and pass = '%s');", login, pass.hashCode());
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
    public static void addBlackList(ClientHandler from, String whomNick){
        String sql = String.format("insert into blacklist (nick, blackNick) values ('%s','%s');", from.getNick(), whomNick);
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void getBlackList(ClientHandler from){
        String sql = String.format("Select blackNick from blacklist where (nick = '%s');", from.getNick());
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                from.setBlackList(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void addUser(String login, String pass){
        String nick = login + "1";
        String sql = String.format("insert into auth (login, pass, nick) values ('%s','%s','%s');", login, pass.hashCode(), nick);
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     public static void disconnect(){
         try {
             connections.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

}
